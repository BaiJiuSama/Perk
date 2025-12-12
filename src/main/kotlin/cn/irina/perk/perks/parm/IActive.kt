package cn.irina.perk.perks.parm

import org.bukkit.entity.Player

/**
 * @Author Irina
 * @Date 2025/12/12 12:39
 */

interface IActive {
    fun execute(myself: Player)
    fun cancel(myself: Player)
}