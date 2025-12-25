package cn.irina.perk.perks.impl.attack

import cn.irina.perk.model.Perk
import cn.irina.perk.model.parm.IAttack
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * @Author Irina
 * @Date 2025/12/9 19:26
 */

class FirePerk: Perk(), IAttack {
    override fun id(): String = "fire"
    
    override fun name(): String = "焰火"
    
    override fun description(): String = "选中时玩家每次攻击都会触发5s的火焰附加"
    
    override fun onAttack(myself: Player, victimEntity: Entity, event: EntityDamageByEntityEvent) {
        victimEntity.fireTicks += 500
    }
}