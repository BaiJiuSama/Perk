package cn.irina.perk.command.impl.admin

import cn.irina.perk.Main
import cn.irina.perk.util.CC
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.CommandPlaceholder
import revxrsal.commands.annotation.Optional
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.FallbackPrefix

/**
 * @Author Irina
 * @Date 2025/11/15 16:04
 */

@FallbackPrefix("irina")
@Command("perk info")
class PerkCommand {
    private val instance = Main.instance
    private val perkManager = instance.perkManager
    
    private val line = CC.commandColorLine
    
    @CommandPlaceholder
    fun onCommand(actor: BukkitCommandActor, @Optional name: String) {
        val player = actor.requirePlayer()
        
        val perk = perkManager.getPerk(name)
        if (perk == null) {
            player.sendMessage(CC.color("&c未知的天赋!"))
            return
        }
        
        listOf(
            line,
            "",
            "&7名称: &f${perk.name()}",
            "&7内部ID: &f${perk.id()}",
            "&7描述: ",
            "&f${perk.description()}",
            "",
            line
        ).map { player.sendMessage(CC.color(it)) }
    }
}