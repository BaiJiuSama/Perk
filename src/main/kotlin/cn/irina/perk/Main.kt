package cn.irina.perk

import cn.irina.perk.manager.ConfigManager
import cn.irina.perk.manager.MongoManager
import cn.irina.perk.manager.PerkManager
import cn.irina.perk.perks.AbstractPerk
import cn.irina.perk.util.Log
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import org.simpleyaml.configuration.file.YamlFile
import revxrsal.commands.Lamp
import revxrsal.commands.bukkit.BukkitLamp
import revxrsal.commands.bukkit.actor.BukkitCommandActor

class Main : JavaPlugin() {
    companion object {
        @JvmStatic
        lateinit var instance: Main
        
        lateinit var perkManager: PerkManager
        lateinit var configManager: ConfigManager
        lateinit var mongoManager: MongoManager
        
        lateinit var cfg: YamlFile
        
        private lateinit var lamp: Lamp<BukkitCommandActor>
    }
    
    override fun onEnable() {
        instance = this
        
        Log.info("[Main] | 加载中...")
        
        val startTime = System.currentTimeMillis()
        
        // Init Task
        runCatching {
            initManager()
            initListener()
            initCommand()
        }.onFailure {
            Log.error("[Main] | 加载失败")
            it.printStackTrace()
            
            Runtime.getRuntime().halt(0)
        }
        
        // If init task has OK, Then start load task
        load()
        
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
        perkManager = PerkManager(this)
        configManager = ConfigManager(this)
        mongoManager = MongoManager(this)
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
