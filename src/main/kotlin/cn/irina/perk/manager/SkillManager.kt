package cn.irina.perk.manager

import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @Author Irina
 * @Date 2025/11/26 20:36
 */

class SkillManager {
    companion object {
        /**
         * 此Map中的Key应为每个特殊Skill的ID
         * 如: FirePerk - FIRE, LightingPerk - LIGHTING
         */
        private val skillActives = ConcurrentHashMap<String, MutableList<UUID>>()
    }
    
    val skills = listOf("FIRE")
    init { skills.forEach { s -> skillActives[s] = mutableListOf() } }
    
    private fun getList(name: String): MutableList<UUID>? = skillActives[name.uppercase()]
    
    fun addPlayer(skillListName: String, uuid: UUID): Boolean {
        val list = getList(skillListName) ?: return false
        if (list.contains(uuid)) return true
        
        list.add(uuid)
        return true
    }
    
    fun removePlayer(skillListName: String, uuid: UUID): Boolean {
        val list = getList(skillListName) ?: return false
        if (!list.contains(uuid)) return true
        
        list.remove(uuid)
        return true
    }
    
    fun isActive(skillListName: String, uuid: UUID): Boolean {
        val list = getList(skillListName) ?: return false
        return list.contains(uuid)
    }
}