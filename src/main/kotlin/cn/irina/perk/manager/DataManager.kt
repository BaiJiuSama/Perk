package cn.irina.perk.manager

import cn.irina.perk.Main
import cn.irina.perk.data.PlayerData
import cn.irina.perk.perks.AbstractPerk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * @Author Irina
 * @Date 2025/10/27 20:28
 */

class DataManager: CoroutineScope {
    private val instance = Main.instance
    private val mongoManager = instance.mongoManager
    
    private val job = Job()
    override val coroutineContext = job + Dispatchers.Default
    
    private val cache = ConcurrentHashMap<UUID, PlayerData>()
    
    fun getData(uuid: UUID): PlayerData = cache[uuid]!!
    
    fun loadData(uuid: UUID) {
        launch {
            val data = mongoManager.getData(uuid)
            
            cache[uuid] = PlayerData(
                uuid = UUID.fromString(data.getString("uuid")),
                name = data.getString("name"),
                currentPerks = data.getList("currentPerks", AbstractPerk ::class.java),
                createAt = data.getLong("createAt"),
            )
        }
    }
    
    fun saveData(pd: PlayerData) = launch { mongoManager.saveData(pd) }
    fun cleanCache(uuid: UUID) = cache.remove(uuid)
    
    fun onClose() {
        Bukkit.getOnlinePlayers().forEach { p ->
            val uuid = p.uniqueId
            saveData(getData(uuid))
            cleanCache(uuid)
        }
    }
}