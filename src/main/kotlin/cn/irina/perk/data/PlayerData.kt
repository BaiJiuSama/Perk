package cn.irina.perk.data

import cn.irina.perk.perks.AbstractPerk
import java.util.UUID

/**
 * @Author Irina
 * @Date 2025/10/27 20:17
 */

data class PlayerData(
    val uuid: UUID,
    val name: String,
    val currentPerks: List<AbstractPerk>,
    val createAt: Long
)
