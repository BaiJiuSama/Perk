package cn.irina.perk.command.impl.debug

import cn.irina.perk.Main
import cn.irina.perk.model.PlayerData
import cn.irina.perk.util.CC
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.CommandPlaceholder
import revxrsal.commands.annotation.Optional
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.actor.BukkitCommandActor
import java.util.UUID

/**
 * @Author Irina
 * @Date 2025/11/11 18:54
 */

@Command("perk data")
class DataCommand {
    private val instance = Main.instance
    private val dataManager = instance.dataManager
    private val perkManager = instance.perkManager
    
    private val line = "&f&m                        "
    private val helpMsg = listOf(
        "",
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
        line,
        ""
    )
    
    private fun getPlayer(actor: BukkitCommandActor): Player? {
        if (actor is Player)
            return actor
        else
            actor.sendRawMessage(CC.color("&c非玩家目标!"))
            return null
    }
    
    private fun getData(player: Player): PlayerData? {
        val data = dataManager.getData(player.uniqueId)
        if (data == null) {
            player.sendMessage(CC.color("&c无数据!"))
            return null
        }
        
        return data
    }
    
    @CommandPlaceholder
    fun onCommand(actor: BukkitCommandActor) { helpMsg.map { actor.sendRawMessage(CC.color(it)) } }
    
    @Subcommand("myData")
    fun onCheckAll(actor: BukkitCommandActor) {
        val player = getPlayer(actor) ?: return
        val data = getData(player) ?: return
        
        listOf(
            "UUID: ${data.uuid}",
            "NAME: ${data.name}",
            "PERKS: ${data.currentPerks}",
            "CREATED AT: ${data.createAt}"
        ).map { actor.sendRawMessage(it) }
    }
    
    @Subcommand("add")
    fun onAdd(actor: BukkitCommandActor, @Optional perkName: String) {
        val player = getPlayer(actor) ?: return
        val data = getData(player) ?: return
        
        val selectPerk = perkManager.getPerk(perkName) ?: return
        selectPerk.execute(player)
        data.currentPerks.add(selectPerk)
        
        player.sendMessage(CC.color("&aSuccessfully"))
    }
    
    @Subcommand("remove")
    fun onRemove(actor: BukkitCommandActor, @Optional perkName: String) {
        val player = getPlayer(actor) ?: return
        val data = getData(player) ?: return
        val currentPerks = data.currentPerks
        val selectPerk = perkManager.getPerk(perkName) ?: return
        
        if (!currentPerks.contains(selectPerk)) {
            player.sendMessage(CC.color("&c无效的天赋!"))
            return
        }
        
        selectPerk.cancel(player)
        data.currentPerks.remove(selectPerk)
        
        player.sendMessage(CC.color("&aSuccessfully"))
    }
}