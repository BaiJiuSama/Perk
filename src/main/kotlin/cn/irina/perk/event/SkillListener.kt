package cn.irina.perk.event

import cn.irina.perk.Main
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * @Author Irina
 * @Date 2025/10/28 18:43
 */

class SkillListener: Listener {
    private val instance = Main.instance
    private val skillManager = instance.skillManager
    private val skills = skillManager.skills
    
    @EventHandler
    fun onAttack(evt: EntityDamageByEntityEvent) {
        val victim = evt.entity
        val attacker = evt.damager as? Player ?: return
        
        triggerAttackSkill(attacker, victim)
    }
    
    /**
     * 此方法用于触发玩家特殊天赋
     * 逻辑:
     * 先循环特殊天赋列表, 找出天赋列表中玩家是否处于其中
     * 若处于其中, 则通过when筛选对应天赋ID的特殊天赋列表
     * 之后再对victim实体造成额外效果等
     * 若玩家拥有多个天赋, 此方法可以完整查找并触发所有含有该玩家的天赋效果
     */
    private fun triggerAttackSkill(attacker: Player, victim: Entity) = skills.forEach { skill ->
        if (!skillManager.isActive(skill, attacker.uniqueId)) return@forEach
        when (skill.uppercase()) {
            "FIRE" -> victim.fireTicks += 100
            
            else -> return@forEach
        }
    }
}