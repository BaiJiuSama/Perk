package cn.irina.perk.event

import cn.irina.perk.Main
import cn.irina.perk.perks.parm.IAttack
import cn.irina.perk.perks.parm.IDefense
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

/**
 * @Author Irina
 * @Date 2025/12/12 12:45
 */

class CombatListener: Listener {
    private val instance = Main.instance
    private val dataManager = instance.dataManager
    private val perkManager = instance.perkManager
    
    @EventHandler
    fun onFight(evt: EntityDamageByEntityEvent) {
        val attacker = evt.damager as? Player ?: return
        val victim = evt.entity
        if (victim == attacker) return
        
        val aUid = attacker.uniqueId
        trackSkill(aUid, evt, false)
        
        if (victim !is Player) return
        val vUid = victim.uniqueId
        trackSkill(vUid, evt, true)
    }
    
    private fun getCurrentPerks(uuid: UUID): MutableList<String> {
        val data = dataManager.getData(uuid) ?: return mutableListOf()
        return data.currentPerks
    }
    
    private fun trackSkill(uuid: UUID, evt: EntityDamageByEntityEvent, isDefense: Boolean) {
        val attacker = if (isDefense) evt.damager else evt.damager as? Player ?: return
        val victim = if (isDefense) evt.entity as? Player ?: return else evt.entity
        
        getCurrentPerks(uuid).forEach { p ->
            val perk = perkManager.getPerk(p) ?: return@forEach
            
            val perkObj = perk::class.java.getDeclaredConstructor().newInstance()
            when (isDefense) {
                true -> if (perkObj is IDefense) (perk as IDefense).onDefense(victim as Player, attacker, evt)
                
                false -> if (perkObj is IAttack) (perk as IAttack).onAttack(attacker as Player, victim, evt)
            }
        }
    }
}