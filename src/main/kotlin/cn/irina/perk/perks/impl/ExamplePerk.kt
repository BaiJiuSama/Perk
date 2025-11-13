package cn.irina.perk.perks.impl

import cn.irina.perk.model.Perk
import cn.irina.perk.util.CC
import org.bukkit.entity.Player

/**
 * @Author Irina
 * @Date 2025/11/12 20:23
 */

class ExamplePerk: Perk() {
    override fun id(): String = "example_perk"
    
    override fun name(): String = "示例天赋"
    
    override fun description(): String = "这是一个示例天赋, 会增加玩家的血量属性"
    
    override fun execute(player: Player) {
        val oldHealth = player.health
        val newHealth = oldHealth + 4
        player.health = newHealth
        player.sendMessage(CC.color("示例天赋触发, 原先血量: $oldHealth 当前血量: ${player.health}"))
    }
    
    override fun cancel(player: Player) {
        val oldHealth = player.health
        val newHealth = oldHealth - 4
        player.health = newHealth
        player.sendMessage(CC.color("示例天赋触发, 原先血量: $oldHealth 当前血量: ${player.health}"))
    }
}