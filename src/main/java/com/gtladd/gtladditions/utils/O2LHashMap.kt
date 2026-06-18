package com.gtladd.gtladditions.utils

import com.gtladd.gtladditions.utils.MathUtil.safePlus
import it.unimi.dsi.fastutil.Hash
import it.unimi.dsi.fastutil.HashCommon.arraySize
import it.unimi.dsi.fastutil.HashCommon.mix
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap

class O2LHashMap<K> : Object2LongOpenHashMap<K> {
    private var strategy: Hash.Strategy<in K>? = null

    constructor(strategy: Hash.Strategy<in K>) {
        this.strategy = strategy
    }

    constructor(e: Int) : super(e)

    private fun addToValue(pos: Int, incr: Long): Long {
        val oldValue = value[pos]
        if (oldValue == Long.MAX_VALUE) return oldValue
        value[pos] = oldValue safePlus incr
        return oldValue
    }

    override fun getLong(k: Any?): Long {
        var pos: Int
        if (k == null) {
            if (containsNullKey) return value[n]
        } else {
            var curr: K
            val key = this.key
            if (key[(mix(strategy?.hashCode(k as K) ?: k.hashCode()) and mask).also { pos = it }].also { curr = it } != null) {
                do if (strategy?.equals(curr, k as K) ?: (curr == k)) return value[pos]
                while (key[((pos + 1) and mask).also { pos = it }].also { curr = it } != null)
            }
        }
        return defRetValue
    }

    override fun getOrDefault(k: Any?, defaultValue: Long): Long {
        var pos: Int
        if (k == null) {
            if (containsNullKey) return value[n]
        } else {
            var curr: K
            val key = this.key
            if (key[(mix(strategy?.hashCode(k as K) ?: k.hashCode()) and mask).also { pos = it }].also { curr = it } != null) {
                do if (strategy?.equals(curr, k as K) ?: (curr == k)) return value[pos]
                while (key[((pos + 1) and mask).also { pos = it }].also { curr = it } != null)
            }
        }
        return defaultValue
    }

    override fun containsKey(k: K): Boolean {
        var pos: Int
        if (k == null) {
            return containsNullKey
        } else {
            var curr: K
            val key = this.key
            if (key[(mix(strategy?.hashCode(k as K) ?: k.hashCode()) and mask).also { pos = it }].also { curr = it } != null) {
                do if (strategy?.equals(curr, k as K) ?: (curr == k)) return true
                while (key[((pos + 1) and mask).also { pos = it }].also { curr = it } != null)
            }
        }
        return false
    }

    override fun addTo(k: K, incr: Long): Long {
        var pos: Int
        if (k == null) {
            if (containsNullKey) return addToValue(n, incr)
            pos = n
            containsNullKey = true
        } else {
            var curr: K
            val key = this.key
            if (key[(mix(strategy?.hashCode(k) ?: k.hashCode()) and mask).also { pos = it }].also { curr = it } != null) {
                do if (strategy?.equals(curr, k) ?: (curr == k)) return addToValue(pos, incr)
                while (key[((pos + 1) and mask).also { pos = it }].also { curr = it } != null)
            }
        }
        key[pos] = k
        value[pos] = defRetValue + incr
        if (size++ >= maxFill) rehash(arraySize(size + 1, f))
        return defRetValue
    }
}
