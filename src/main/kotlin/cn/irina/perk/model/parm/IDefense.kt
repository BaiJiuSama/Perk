package cn.irina.perk.model.parm

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * @Author Irina
 * @Date 2025/12/11 12:36
 */

interface IDefense {
    fun onDefense(myself: Player, attackEntity: Entity, event: EntityDamageByEntityEvent)
}