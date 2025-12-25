package cn.irina.perk.perks.impl.attack

import cn.irina.perk.Main
import cn.irina.perk.model.Perk
import cn.irina.perk.model.parm.IAttack
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent

/**
 * @Author Irina
 * @Date 2025/12/17 20:15
 */

class AttackSpeedBoost: Perk(), IAttack, Listener {
    override fun id(): String = "attack_speed_boost"
    
    override fun name(): String = "格斗好手"
    
    override fun description(): String = "每次攻击将会额外获得0.02攻速 (死亡重置)"
    
    override fun onAttack(myself: Player, victimEntity: Entity, event: EntityDamageByEntityEvent) {
        myself.getAttribute(Attribute.GENERIC_ATTACK_SPEED)!!.baseValue += 0.02
    }
    
    private val dataManager = Main.instance.dataManager
    @EventHandler
    private fun onDeath(evt: PlayerDeathEvent) {
        val myself = evt.entity
        val pd = dataManager.getData(myself.uniqueId) ?: return
        
        if (!pd.currentPerks.contains(this.id())) return
        myself.getAttribute(Attribute.GENERIC_ATTACK_SPEED)!!.baseValue = 4.0
    }
}