package cn.irina.perk.model

import java.util.UUID

/**
 * @Author Irina
 * @Date 2025/10/27 20:17
 */

data class PlayerData(
    val uuid: UUID,
    val name: String,
    var currentPerks: MutableList<Perk>,
    val createAt: Long
)
