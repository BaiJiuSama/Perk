package cn.irina.perk.data

/**
 * @Author Irina
 * @Date 2025/10/27 20:17
 */

data class PlayerData(
    val uuid: String,
    val name: String,
    val currentPerks: List<String>,
    val createAt: Long
)
