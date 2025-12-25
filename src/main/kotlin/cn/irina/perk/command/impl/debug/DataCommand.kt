package cn.irina.perk.command.impl.debug

import cn.irina.perk.Main
import cn.irina.perk.model.PlayerData
import cn.irina.perk.util.CC
import cn.irina.perk.util.PerkUtil
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.CommandPlaceholder
import revxrsal.commands.annotation.Optional
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.FallbackPrefix

/**
 * @Author Irina
 * @Date 2025/11/11 18:54
 */

@FallbackPrefix("irina")
@Command("perk data")
class DataCommand {
    private val instance = Main.instance
    private val dataManager = instance.dataManager
    private val perkManager = instance.perkManager
    
    private val line = CC.commandColorLine
    private val helpMsg = listOf(
        line,
        "",
        "&7查看个人数据",
        "&f/perk data myData",
        "",
        "&7添加个人天赋",
        "&f/perk data add <PERK NAME>",
        "",
        "&7移除个人天赋",
        "&f/perk data remove <PERK NAME>",
        "",
        line
    )
    
    private fun getData(player: Player): PlayerData? {
        val data = dataManager.getData(player.uniqueId)
        if (data == null) {
            player.sendMessage(CC.color("&c无数据!"))
            return null
        }
        
        return data
    }
    
    @CommandPlaceholder
    fun onCommand(actor: BukkitCommandActor) { helpMsg.forEach { actor.sendRawMessage(CC.color(it)) } }
    
    @Subcommand("myData")
    fun onCheckAll(actor: BukkitCommandActor) {
        val player = actor.requirePlayer()
        val data = getData(player) ?: return
        
        listOf(
            "UUID: ${data.uuid}",
            "NAME: ${data.name}",
            "PERKS: ${data.currentPerks}",
            "CREATED AT: ${data.createAt}"
        ).forEach { actor.sendRawMessage(it) }
    }
    
    private val perkUtil = PerkUtil
    @Subcommand("add")
    fun onAdd(actor: BukkitCommandActor, @Optional perkName: String) {
        val player = actor.requirePlayer()
        
        if (perkUtil.active(player, perkName)) player.sendMessage(CC.color("&aSuccessfully"))
        else CC.send(player, "ERROR!")
    }
    
    @Subcommand("remove")
    fun onRemove(actor: BukkitCommandActor, @Optional perkName: String) {
        val player = actor.requirePlayer()
        if (perkUtil.close(player, perkName)) player.sendMessage(CC.color("&aSuccessfully"))
        else CC.send(player, "ERROR!")
    }
}