package cn.irina.perk.event

import cn.irina.perk.util.ConcurrentHashSet
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.UUID

/**
 * @Author Irina
 * @Date 2025/10/28 18:43
 */

class SkillListener: Listener {
    val lighting = ConcurrentHashSet.create<UUID>() //攻击时触发闪电
    val wither = ConcurrentHashSet.create<UUID>() //攻击时触发凋零效果
    
    @EventHandler
    fun onAttack(evt: EntityDamageByEntityEvent) {
        
    }
}