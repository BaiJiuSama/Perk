package cn.irina.perk.perks

import org.bukkit.entity.Player

/**
 * @Author Irina
 * @Date 2025/10/27 18:57
 */

abstract class AbstractPerk {
    abstract fun id(): String // 内部命名
    abstract fun name(): String // 名称
    abstract fun description(): String // 描述
    
    abstract fun execute(player: Player) // 被选择后对玩家属性的修改
}