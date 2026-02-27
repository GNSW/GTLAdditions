package com.gtladd.gtladditions.utils

object CollectionUtil {

    fun <T> Array<T?>.allNull() = !this.any { it != null }
    fun <T> Array<T?>.add(t: T): Int {
        for (i in 0..<this.size) if (this[i] == null) {
            this[i] = t
            return i
        }
        return -1
    }
}
