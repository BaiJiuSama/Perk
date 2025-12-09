package cn.irina.perk.perks.impl

import cn.irina.perk.Main
import cn.irina.perk.model.Perk
import cn.irina.perk.util.CC
import org.bukkit.entity.Player

/**
 * @Author Irina
 * @Date 2025/12/9 19:26
 */

class FirePerk: Perk() {
    override fun id(): String = "fire"
    
    override fun name(): String = "焰火"
    
    override fun description(): String = "选中时玩家每次攻击都会触发5s的火焰附加"
    
    private val skillManager = Main.instance.skillManager
    override fun execute(player: Player) {
        if (skillManager.addPlayer(this.id(), player.uniqueId)) CC.send(player, "&aSuccessfully!")
        else CC.send(player, "&cError to add this skill!")
    }
    
    override fun cancel(player: Player) {
        if (skillManager.removePlayer(this.id(), player.uniqueId)) CC.send(player, "&aSuccessfully!")
        else CC.send(player, "&cError to remove this skill!")
    }
}