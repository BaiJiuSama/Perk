package cn.irina.perk.util

import com.google.common.collect.ConcurrentHashMultiset
import org.bukkit.ChatColor
import java.util.regex.Pattern

/**
 * @Author Irina
 * @Date 2025/10/27 18:36
 */

object CC {
    fun color(str: String): String = ChatColor.translateAlternateColorCodes('&', translateHexColorCodes(str))

    fun color(str: List<String>): List<String> {
        val list = ConcurrentHashMultiset.create<String>()
        str.forEach { list.add(color(it)) }
        return list.toList()
    }
    
    private fun translateHexColorCodes(message: String): String {
        val colorChar = ChatColor.COLOR_CHAR
        
        val matcher = Pattern.compile("&#([A-Fa-f0-9]{6})").matcher(message)
        val buffer = StringBuffer(message.length + 4 * 8)
        
        while (matcher.find()) {
            val group = matcher.group(1)
            
            matcher.apdendReplacement(
                buffer, (colorChar.toString() + "x"
                        + colorChar + group[0] + colorChar + group[1]
                        + colorChar + group[2] + colorChar + group[3]
                        + colorChar + group[4] + colorChar + group[5])
            )
        }
        
        return matcher.apdendTail(buffer).toString()
    }
}