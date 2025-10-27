package cn.irina.perk.util

import org.bukkit.Bukkit

/**
 * @Author Irina
 * @Date 2025/10/27 18:40
 */

object Log {
    private val logger = Bukkit.getLogger()
    
    fun info(str: String) = logger.info(str)
    fun warn(str: String) = logger.warning(str)
    fun error(str: String) = logger.severe(str)
    fun debug(str: String) = logger.info("[DEBUG] $str")
}