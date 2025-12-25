package cn.irina.perk.util

import cn.irina.perk.Main
import cn.irina.perk.model.PlayerData
import cn.irina.perk.model.parm.IActive
import org.bukkit.entity.Player

/**
 * @Author Irina
 * @Date 2025/12/25 19:34
 */

object PerkUtil {
    private val instance = Main.instance
    private val perkManager = instance.perkManager
    private val dataManager = instance.dataManager

    private fun getData(target: Player): PlayerData? {
        val data = dataManager.getData(target.uniqueId)
        if (data == null) {
            CC.send(target, "&c你的数据未被加载!")
            target.kickPlayer("Player is joined, But it data is not loaded!")
            return null
        }
        
        return data
    }
    
    fun active(target: Player, id: String): Boolean {
        val data = this.getData(target) ?: return false
        if (data.currentPerks.contains(id)) {
            CC.send(target, "&c此效果已携带, 不可重复携带!")
            return false
        }
        
        val selectPerk = perkManager.getPerk(id)
        if (selectPerk == null) {
            CC.send(target, "Unknown name of that perk!")
            return false
        }
        
        data.currentPerks.add(id)
        if (selectPerk is IActive) selectPerk.execute(target)
        return true
    }
    
    fun close(target: Player, id: String): Boolean {
        val data = this.getData(target) ?: return false
        if (!data.currentPerks.contains(id)) return true
        
        val selectPerk = perkManager.getPerk(id)
        if (selectPerk == null) {
            CC.send(target, "Unknown name of that perk!")
            return false
        }
        
        data.currentPerks.remove(id)
        if (selectPerk is IActive) selectPerk.cancel(target)
        return true
    }
}