package com.gtladd.gtladditions.utils

object CollectionUtil {

    fun <T> Array<T?>.allNull() = !this.any { it != null }
    fun <T> Array<T?>.add(t: T): Int {
        for ((i, element) in this.withIndex()) if (element == null) {
            this[i] = t
            return i
        }
        return -1
    }
}
