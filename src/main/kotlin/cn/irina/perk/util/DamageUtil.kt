package cn.irina.perk.util

import org.bukkit.entity.Animals
import org.bukkit.entity.Entity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player

/**
 * @Author Irina
 * @Date 2025/12/12 12:46
 */

object DamageUtil {
    /**
     * 踩坑
     * 如若对Entity造成伤害的话不可直接转换为可以被造成伤害的实体类
     * 需先判断实体是否处于Player Animals Mob三大类
     */
    fun damage(target: Entity, damage: Double) {
        if (damage <= 0) return
        
        if (target is Mob || target is Animals || target is Player) target.damage(damage)
    }
}