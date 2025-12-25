package cn.irina.perk.perks.impl.attack

import cn.irina.perk.model.Perk
import cn.irina.perk.model.parm.IAttack
import cn.irina.perk.util.DamageUtil
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * @Author Irina
 * @Date 2025/12/16 13:36
 */

class LightingPerk: Perk(), IAttack {
    override fun id(): String = "lighting"
    
    override fun name(): String = "闪雷"
    
    override fun description(): String = "攻击将对目标降下闪电并造成4❤的伤害"
    

    override fun onAttack(myself: Player, victimEntity: Entity, event: EntityDamageByEntityEvent) {
        DamageUtil.damage(victimEntity, 8.0)
        
        victimEntity.world.strikeLightningEffect(victimEntity.location)
    }
}