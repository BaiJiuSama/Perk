package cn.irina.perk.model

import java.util.*

/**
 * @Author Irina
 * @Date 2025/10/27 20:17
 */

data class PlayerData(
    val uuid: UUID,
    val name: String,
    var currentPerks: MutableList<String>,
    val createAt: Long
)
