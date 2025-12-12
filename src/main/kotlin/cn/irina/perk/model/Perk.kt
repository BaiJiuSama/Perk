package cn.irina.perk.model

/**
 * @Author Irina
 * @Date 2025/10/27 18:57
 */

abstract class Perk {
    abstract fun id(): String // 内部命名
    abstract fun name(): String // 名称
    abstract fun description(): String // 描述
    
    override fun toString(): String = "Perk[ID: ${id()}, Name: ${name()}, Description: ${description()}]"
}