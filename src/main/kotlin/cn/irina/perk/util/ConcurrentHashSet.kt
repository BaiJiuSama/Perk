package cn.irina.perk.util

import java.util.concurrent.ConcurrentHashMap

/**
 * 高性能线程安全的 HashSet 实现
 *
 * 基于 ConcurrentHashMap 实现，提供优秀的并发性能和内存效率
 * 支持高并发读写操作，无需外部同步
 *
 * @author Irina
 * @date 2025/10/10
 */
class ConcurrentHashSet<E> private constructor(
    private val map: ConcurrentHashMap<E, Boolean>
) : MutableSet<E> {

    companion object {
        private const val PRESENT = true

        /**
         * 创建空的 ConcurrentHashSet
         */
        @JvmStatic
        fun <E> create(): ConcurrentHashSet<E> {
            return ConcurrentHashSet(ConcurrentHashMap())
        }

        /**
         * 创建指定初始容量的 ConcurrentHashSet
         */
        @JvmStatic
        fun <E> create(initialCapacity: Int): ConcurrentHashSet<E> {
            return ConcurrentHashSet(ConcurrentHashMap(initialCapacity))
        }

        /**
         * 从已有集合创建 ConcurrentHashSet
         */
        @JvmStatic
        fun <E> create(elements: Collection<E>): ConcurrentHashSet<E> {
            val set = ConcurrentHashSet<E>(ConcurrentHashMap(elements.size))
            set.addAll(elements)
            return set
        }

        /**
         * 创建指定初始容量和负载因子的 ConcurrentHashSet
         */
        @JvmStatic
        fun <E> create(initialCapacity: Int, loadFactor: Float): ConcurrentHashSet<E> {
            return ConcurrentHashSet(ConcurrentHashMap(initialCapacity, loadFactor))
        }

        /**
         * 创建指定并发级别的 ConcurrentHashSet
         */
        @JvmStatic
        fun <E> create(initialCapacity: Int, loadFactor: Float, concurrencyLevel: Int): ConcurrentHashSet<E> {
            return ConcurrentHashSet(ConcurrentHashMap(initialCapacity, loadFactor, concurrencyLevel))
        }
    }

    // === MutableSet 接口实现 ===

    override val size: Int
        get() = map.size

    override fun isEmpty(): Boolean = map.isEmpty()

    override fun contains(element: E): Boolean = map.containsKey(element)

    override fun containsAll(elements: Collection<E>): Boolean {
        return elements.all { map.containsKey(it) }
    }

    override fun iterator(): MutableIterator<E> = map.keys.iterator()

    override fun add(element: E): Boolean {
        return map.put(element, PRESENT) == null
    }

    override fun addAll(elements: Collection<E>): Boolean {
        var modified = false
        for (e in elements) {
            if (add(e)) modified = true
        }
        return modified
    }

    override fun remove(element: E): Boolean {
        return map.remove(element) != null
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        var modified = false
        for (e in elements) {
            if (remove(e)) modified = true
        }
        return modified
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        val set = elements.toSet()
        return map.keys.removeIf { it !in set }
    }

    override fun clear() {
        map.clear()
    }

    // === 高性能并发操作 ===

    /**
     * 原子性地添加元素（仅当元素不存在时）
     * @return true 如果元素被成功添加
     */
    fun addIfAbsent(element: E): Boolean {
        return map.putIfAbsent(element, PRESENT) == null
    }

    /**
     * 原子性地移除元素（仅当元素存在时）
     * @return true 如果元素被成功移除
     */
    fun removeIfPresent(element: E): Boolean {
        return map.remove(element, PRESENT)
    }

    /**
     * 批量添加元素（优化的并行版本）
     */
    fun addAllParallel(elements: Collection<E>) {
        elements.parallelStream().forEach { add(it) }
    }

    /**
     * 获取快照（不会阻塞其他操作）
     */
    fun snapshot(): Set<E> {
        return HashSet(map.keys)
    }

    /**
     * 获取并发安全的只读视图
     */
    fun asReadOnlySet(): Set<E> {
        return map.keys
    }

    // === 工具方法 ===

    override fun toString(): String = map.keys.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Set<*>) return false
        return map.keys == other
    }

    override fun hashCode(): Int = map.keys.hashCode()
}

/**
 * Kotlin DSL 扩展函数
 */
fun <E> concurrentHashSetOf(vararg elements: E): ConcurrentHashSet<E> {
    return ConcurrentHashSet.create(elements.toList())
}

fun <E> emptyConcurrentHashSet(): ConcurrentHashSet<E> {
    return ConcurrentHashSet.create()
}

fun <E> Collection<E>.toConcurrentHashSet(): ConcurrentHashSet<E> {
    return ConcurrentHashSet.create(this)
}
