package cn.irina.perk.perks.impl

import cn.irina.perk.model.Perk
import cn.irina.perk.util.CC
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

/**
 * @Author Irina
 * @Date 2025/11/12 20:23
 */

class ExamplePerk: Perk() {
    override fun id(): String = "example_perk"
    
    override fun name(): String = "示例天赋"
    
    override fun description(): String = "这是一个示例天赋, 会增加玩家的最大血量属性"
    
    override fun execute(player: Player) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue += 4
        player.sendMessage(CC.color("&e示例天赋触发, 当前血量: ${player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue}"))
    }
    
    override fun cancel(player: Player) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue -= 4
        player.sendMessage(CC.color("&e示例天赋触发, 当前血量: ${player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue}"))
    }
}