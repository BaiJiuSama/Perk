package cn.irina.perk.manager

import cn.irina.perk.model.Perk
import cn.irina.perk.util.Log
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentHashMap.newKeySet

/**
 * @Author Irina
 * @Date 2025/10/27 18:55
 */

class PerkManager {
    private val perkMap = ConcurrentHashMap<String, Perk>()
    private val perks = newKeySet<Perk>()
    
    fun getPerkMap(): ConcurrentHashMap<String, Perk> = perkMap
    fun getPerks(): List<Perk> = perks.toList()
    fun getPerk(id: String): Perk? = perkMap[id]
    
    fun load(classes: List<Class<out Perk>>) {
        Log.info("[Perk] | 加载中...")
        
        classes.forEach {
            runCatching {
                val perk = it.getConstructor().newInstance() as Perk
                perkMap[perk.id()] = perk
                perks.add(perk)
                Log.info("[Perk] | 加载天赋: ${perk.name()}")
            }.onFailure { c ->
                Log.error("[Perk] | 无法加载天赋类: ${c.javaClass.simpleName}")
                c.printStackTrace()
            }
        }
        
        Log.info("[Perk] | 加载完毕")
    }
}