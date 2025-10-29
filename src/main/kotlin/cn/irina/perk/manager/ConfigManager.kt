package cn.irina.perk.manager

import cn.irina.perk.Main
import org.simpleyaml.configuration.file.YamlFile
import java.io.File

/**
 * @Author Irina
 * @Date 2025/10/27 19:54
 */

class ConfigManager {
    private val instance = Main.instance
    var file: File = File(instance.dataFolder, "config.yml").also {
        if (!it.exists()) {
            instance.saveResource("config.yml", false)
        }
    }
    
    var config : YamlFile = load(file)
    
    private fun load(file: File) = YamlFile(file).apply {
        createOrLoadWithComments()
    }
}