package cn.irina.perk

import cn.irina.perk.command.CommandExceptionHandler
import cn.irina.perk.manager.ConfigManager
import cn.irina.perk.manager.DataManager
import cn.irina.perk.manager.MongoManager
import cn.irina.perk.manager.PerkManager
import cn.irina.perk.model.Perk
import cn.irina.perk.util.ClassUtil
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
import revxrsal.commands.annotation.Command
import revxrsal.commands.bukkit.BukkitLamp
import revxrsal.commands.bukkit.actor.BukkitCommandActor

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
        
        runCatching {
            load()
            
            initCommand()
            initListener()
        }.onFailure {
            it.printStackTrace()
            Log.error("[Main] | 加载失败")
            
            Runtime.getRuntime().halt(0)
        }
        
        val endTime = System.currentTimeMillis()
        val finalTime = (endTime - startTime) / 1000.0
        Log.info("[Main] | 本次启动时长: $finalTime 秒")
    }
    
    private fun load() {
        perkManager = PerkManager()
        configManager = ConfigManager()
        
        val perks = Reflections("cn.irina.perk.perks.impl")
            .getSubTypesOf(Perk::class.java)
            .toList()
        perkManager.load(perks)
        
        cfg = configManager.config
        
        mongoManager = MongoManager(cfg)
        launch { mongoManager.load() }
        
        dataManager = DataManager()
    }
    
    override fun onDisable() {
    
    }
    
    private fun initCommand() {
        runCatching {
            Log.info("[Command] | 开始加载...")
            
            val objects = ClassUtil.getClassesInPackage(this, "cn.irina.perk.command.impl")
            if (objects == null) {
                Log.info("[Command] | 无指令类需注册!")
                return
            }
            
            val fillObjects = objects.filter { Command::class.java.isAssignableFrom(it!!) }
            
            lamp = BukkitLamp.builder(this)
                .exceptionHandler(CommandExceptionHandler())
                .build()
            
            lamp.register(fillObjects)
            objects.map { Log.info("[Command] | 加载指令: ${it!!.simpleName}") }
            
            Log.info("[Command] | 加载完毕")
        }.onFailure {
            it.printStackTrace()
            Log.error("[Command] | 加载指令类失败!")
            
            Runtime.getRuntime().halt(0)
        }
    }
    
    private fun initListener() {
        Log.info("[Listener] | 开始加载...")
        val classes = ClassUtil.getClassesInPackage(this, "cn.irina.perk")
        
        if (classes == null) {
            Log.info("[Listener] | 监听类为空!")
            return
        }
        
        classes.forEach { c ->
            if (!Listener::class.java.isAssignableFrom(c!!)) return@forEach
            
            runCatching {
                Bukkit.getPluginManager().registerEvents(c.getConstructor().newInstance() as Listener, this)
                Log.info("[Listener] | 加载事件: ${c.simpleName}")
            }.onFailure {
                Log.error("[Listener] | 无法加载 ${c.simpleName}")
                it.printStackTrace()
            }
        }
        
        Log.info("[Listener] | 加载完毕")
    }
}
