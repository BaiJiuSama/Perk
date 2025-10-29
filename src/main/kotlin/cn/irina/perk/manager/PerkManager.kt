package cn.irina.perk.manager

import cn.irina.perk.perks.AbstractPerk
import cn.irina.perk.util.ConcurrentHashSet
import cn.irina.perk.util.Log
import java.util.concurrent.ConcurrentHashMap

/**
 * @Author Irina
 * @Date 2025/10/27 18:55
 */

class PerkManager {
    private val perkMap = ConcurrentHashMap<String, AbstractPerk>()
    private val perks = ConcurrentHashSet.create<AbstractPerk>()
    
    fun getPerkMap(): ConcurrentHashMap<String, AbstractPerk> = perkMap
    fun getPerks(): List<AbstractPerk> = perks.toList()
    fun getPerk(id: String): AbstractPerk? = perkMap[id]
    
    fun load(classes: List<Class<out AbstractPerk>>) {
        Log.info("[Perk] | 加载中...")
        
        classes.forEach {
            runCatching {
                val perk = it.getConstructor().newInstance() as AbstractPerk
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