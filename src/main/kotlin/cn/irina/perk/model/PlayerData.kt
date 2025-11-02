package cn.irina.perk.model

import cn.irina.perk.model.Perk
import java.util.UUID

/**
 * @Author Irina
 * @Date 2025/10/27 20:17
 */

data class PlayerData(
    val uuid: UUID,
    val name: String,
    val currentPerks: List<Perk>,
    val createAt: Long
)
