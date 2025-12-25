package cn.irina.perk.model.parm

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * @Author Irina
 * @Date 2025/12/11 12:26
 */

interface IAttack {
    fun onAttack(myself: Player, victimEntity: Entity, event: EntityDamageByEntityEvent)
}