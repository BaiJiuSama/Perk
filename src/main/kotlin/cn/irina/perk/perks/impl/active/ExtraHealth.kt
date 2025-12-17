package cn.irina.perk.perks.impl.active

import cn.irina.perk.model.Perk
import cn.irina.perk.perks.parm.IActive
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

/**
 * @Author Irina
 * @Date 2025/12/17 18:55
 */

class ExtraHealth: Perk(), IActive {
    override fun id(): String = "extra_health"
    
    override fun name(): String = "额外生命"
    
    override fun description(): String = "携带时额外获得2❤血量"
    
    private val healthAttribute = Attribute.GENERIC_MAX_HEALTH
    override fun execute(myself: Player) {
        myself.getAttribute(healthAttribute)!!.baseValue += 4
    }
    
    override fun cancel(myself: Player) {
        myself.getAttribute(healthAttribute)!!.baseValue -= 4
    }
}