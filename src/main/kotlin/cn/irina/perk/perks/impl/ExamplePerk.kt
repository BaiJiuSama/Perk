package cn.irina.perk.perks.impl

import cn.irina.perk.model.Perk
import cn.irina.perk.perks.parm.IActive
import cn.irina.perk.util.CC
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

/**
 * @Author Irina
 * @Date 2025/11/12 20:23
 */

class ExamplePerk: Perk(), IActive {
    override fun id(): String = "example_perk"
    
    override fun name(): String = "示例天赋"
    
    override fun description(): String = "这是一个示例天赋, 会增加玩家的最大血量属性"
    
    override fun execute(myself: Player) {
        myself.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue += 4
        myself.sendMessage(CC.color("&e示例天赋触发, 当前血量: ${myself.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue}"))
    }
    
    override fun cancel(myself: Player) {
        myself.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue -= 4
        myself.sendMessage(CC.color("&e示例天赋触发, 当前血量: ${myself.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.baseValue}"))
    }
}