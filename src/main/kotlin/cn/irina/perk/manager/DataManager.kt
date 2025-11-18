package cn.irina.perk.manager

import cn.irina.perk.Main
import cn.irina.perk.model.Perk
import cn.irina.perk.model.PlayerData
import cn.irina.perk.util.Log
import kotlinx.coroutines.*
import org.bukkit.Bukkit
import java.util.*
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
    
    fun getData(uuid: UUID): PlayerData? = cache[uuid]
    
    suspend fun loadData(uuid: UUID): Boolean = withContext(Dispatchers.IO) {
        try {
            val data = mongoManager.getData(uuid)
            val playerData = PlayerData(
                uuid = UUID.fromString(data.getString("uuid")),
                name = data.getString("name"),
                currentPerks = data.getList("currentPerks", Perk::class.java),
                createAt = data.getLong("createAt"),
            )
            cache[uuid] = playerData
            
            return@withContext true
        } catch (e: Exception) {
            Log.error("无法加载玩家数据")
            e.printStackTrace()
            return@withContext false
        }
    }
    
    suspend fun saveData(pd: PlayerData) = withContext(Dispatchers.IO) { mongoManager.saveData(pd) }
    fun cleanCache(uuid: UUID) = cache.remove(uuid)
    
    fun onClose() = launch {
        Bukkit.getOnlinePlayers().forEach { p ->
            val uuid = p.uniqueId
            saveData(getData(uuid) ?: return@forEach)
            cleanCache(uuid)
        }
    }
}