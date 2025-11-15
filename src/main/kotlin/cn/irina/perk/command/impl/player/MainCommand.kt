package cn.irina.perk.command.impl.player

import cn.irina.perk.util.CC
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.CommandPlaceholder
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.FallbackPrefix

/**
 * @Author Irina
 * @Date 2025/11/12 20:02
 */

@FallbackPrefix("irina")
@Command("perk")
class MainCommand {
    private val line = CC.commandColorLine
    private val helpMsg = listOf(
        line,
        "",
        "&7一切的开始",
        "&f/perk",
        "",
        "&7数据相关",
        "&f/perk data",
        "",
        "&7天赋信息",
        "&f/perk info <NAME>",
        "",
        line
    )
    
    @CommandPlaceholder
    fun onCommand(actor: BukkitCommandActor) { helpMsg.map { actor.sendRawMessage(CC.color(it)) } }
}