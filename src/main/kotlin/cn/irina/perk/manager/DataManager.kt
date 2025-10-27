package cn.irina.perk.manager

import cn.irina.perk.Main
import cn.irina.perk.data.PlayerData
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * @Author Irina
 * @Date 2025/10/27 20:28
 */

class DataManager(private val instance: Main) {
    private val cache = ConcurrentHashMap<UUID, PlayerData>()
}