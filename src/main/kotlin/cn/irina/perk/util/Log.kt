package cn.irina.perk.util

import org.bukkit.Bukkit

/**
 * @Author Irina
 * @Date 2025/10/27 18:40
 */

object Log {
    private val logger = Bukkit.getLogger()
    private const val PREFIX = "[Perk] "
    
    fun info(str: String) = logger.info(PREFIX + str)
    fun warn(str: String) = logger.warning(PREFIX + str)
    fun error(str: String) = logger.severe(PREFIX + str)
    fun debug(str: String) = logger.info("$PREFIX[DEBUG] $str")
}