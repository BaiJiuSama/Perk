package cn.irina.perk.perks.impl.defense

import cn.irina.perk.model.Perk
import cn.irina.perk.model.parm.IDefense
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * @Author Irina
 * @Date 2025/12/17 20:21
 */

class DefensePerk: Perk(), IDefense {
    override fun id(): String = "defense"
    
    override fun name(): String = "神御"
    
    override fun description(): String = "减免50%的伤害"
    
    override fun onDefense(myself: Player, attackEntity: Entity, event: EntityDamageByEntityEvent) {
        val defaultDamage = event.damage
        event.damage = defaultDamage * 0.5
    }
}