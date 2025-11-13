package cn.irina.perk.event

import cn.irina.perk.Main
import cn.irina.perk.util.CC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.time.LocalTime

/**
 * @Author Irina
 * @Date 2025/10/28 13:54
 */

class DataListener: Listener, CoroutineScope {
    companion object {
        private const val PREFIX = Main.PREFIX
        
        private val instance = Main.instance
        private val dataManager = instance.dataManager
    }
    
    private val job = Job()
    override val coroutineContext = job + Dispatchers.Default
    
    @EventHandler
    fun onJoin(evt: PlayerJoinEvent) {
        val player = evt.player
        runCatching {
            launch {
                CC.send(player, "${PREFIX}数据加载中...")
                if (dataManager.loadData(player.uniqueId)) CC.send(player, "${PREFIX}加载完毕!")
                else player.kickPlayer("${PREFIX}数据加载错误!")
            }
        }.onFailure {
            it.printStackTrace()
            CC.send(player, "${PREFIX}&c错误时间 &f${LocalTime.now()}&c, 请将此消息报告至管理员!")
        }
    }
    
    @EventHandler
    fun onQuit(evt: PlayerQuitEvent) {
        val player = evt.player
        val uuid = player.uniqueId
        
        dataManager.saveData(dataManager.getData(uuid) ?: return)
        dataManager.cleanCache(uuid)
    }
}