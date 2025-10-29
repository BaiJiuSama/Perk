package cn.irina.perk

import cn.irina.perk.manager.ConfigManager
import cn.irina.perk.manager.DataManager
import cn.irina.perk.manager.MongoManager
import cn.irina.perk.manager.PerkManager
import cn.irina.perk.perks.AbstractPerk
import cn.irina.perk.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import org.simpleyaml.configuration.file.YamlFile
import revxrsal.commands.Lamp
import revxrsal.commands.bukkit.BukkitLamp
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import kotlin.coroutines.CoroutineContext

class Main: JavaPlugin(), CoroutineScope {
    companion object {
        const val PREFIX = "&8[&bI&fRINA&8] &f| "
        
        @JvmStatic
        lateinit var instance: Main
        
        lateinit var cfg: YamlFile
        private lateinit var lamp: Lamp<BukkitCommandActor>
    }
    
    lateinit var perkManager: PerkManager
    lateinit var configManager: ConfigManager
    lateinit var mongoManager: MongoManager
    lateinit var dataManager: DataManager
    
    private val job = Job()
    override val coroutineContext = job + Dispatchers.Default
    
    override fun onEnable() {
        instance = this
        
        Log.info("[Main] | 加载中...")
        
        val startTime = System.currentTimeMillis()
        
        // Init Task
        runCatching {
            initManager()
            initCommand()
        }.onFailure {
            Log.error("[Main] | 加载失败")
            it.printStackTrace()
            
            Runtime.getRuntime().halt(0)
        }
        
        // If init task has OK, Then start load task
        load()
        initListener()
        
        val endTime = System.currentTimeMillis()
        val finalTime = (endTime - startTime) / 1000.0
        Log.info("[Main] | 本次启动时长: $finalTime 秒")
    }
    
    private fun load() {
        val perks = Reflections("cn.irina.perk.perks.impl")
            .getSubTypesOf(AbstractPerk::class.java)
            .toList()
        
        perkManager.load(perks)
        
        cfg = configManager.config
        
        launch { mongoManager.load() }
    }
    
    override fun onDisable() {
    
    }
    
    private fun initCommand() {
        Log.info("[Command] | 开始加载...")
        
        lamp = BukkitLamp.builder(this).build()
        lamp.register()
        
        Log.info("[Command] | 加载完毕")
    }
    
    private fun initManager() {
        perkManager = PerkManager()
        configManager = ConfigManager()
        mongoManager = MongoManager()
        dataManager = DataManager()
    }
    
    private fun initListener() {
        Log.info("[Listener] | 开始加载...")
        val classes = Reflections("cn.irina.perk")
            .getSubTypesOf(Listener::class.java)
        
        classes.forEach { c ->
            runCatching {
                Bukkit.getPluginManager().registerEvents(c.getConstructor().newInstance(), this)
                Log.info("[Listener] | ${c.simpleName}")
            }.onFailure {
                Log.error("[Listener] | 无法加载 ${c.simpleName}")
                it.printStackTrace()
            }
        }
        
        Log.info("[Listener] | 加载完毕")
    }
}
