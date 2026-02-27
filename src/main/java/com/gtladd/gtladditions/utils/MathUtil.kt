package com.gtladd.gtladditions.utils

import org.gtlcore.gtlcore.utils.NumberUtils

import com.google.common.math.DoubleMath
import com.google.common.primitives.Ints

import java.math.RoundingMode
import kotlin.math.*
import kotlin.random.Random

object MathUtil {

    infix fun Int.pow(x: Number): Int = this.toDouble().pow(x.toDouble()).toInt()
    infix fun Long.pow(x: Number): Long = this.toDouble().pow(x.toDouble()).toLong()
    infix fun Int.ln(long: Long): Int = this * ln(long.toDouble()).toInt()
    infix fun Number.maxToInt(x: Number): Int = max(this.toInt(), x.toInt())
    infix fun Number.maxToLong(x: Number): Long = max(this.toLong(), x.toLong())
    infix fun Number.maxToDouble(x: Number): Double = max(this.toDouble(), x.toDouble())
    infix fun Number.minToInt(x: Number): Int = min(this.toInt(), x.toInt())
    infix fun Number.minToLong(x: Number): Long = min(this.toLong(), x.toLong())
    infix fun Number.minToDouble(x: Number): Double = min(this.toDouble(), x.toDouble())
    infix fun sqrt(i: Int): Double = sqrt(i.toDouble())
    infix fun cbrt(i: Int): Double = cbrt(i.toDouble())
    infix fun ln(i: Int): Double = ln(i.toDouble())
    infix fun exp(i: Int): Long = exp(i.toDouble()).toLong()
    infix fun Long.safePlus(l: Long): Long = NumberUtils.saturatedAdd(this, l)
    infix fun Long.safeMultiply(l: Long) = NumberUtils.saturatedMultiply(this, l)
    val Long.safeToInt: Int get() = Ints.saturatedCast(this)
    fun Double.format(int: Int): String = "%.${int}f".format(this)
    val Double.safeToLong: Long get() = when {
        this > Long.MAX_VALUE -> Long.MAX_VALUE
        this < Long.MIN_VALUE -> Long.MIN_VALUE
        else -> DoubleMath.roundToLong(this, RoundingMode.DOWN)
    }
    fun Int.isRange(min: Int, max: Int): Boolean = this in min..max
    fun Int.safeLimitPlus(plus: Int, limit: Int): Int = (this + plus) minToInt limit
    fun Int.safeLimitMinus(minus: Int, limit: Int): Int = (this - minus) maxToInt limit
    val Int.red: Int get() = (this shr 16) and 0xff
    val Int.green: Int get() = (this shr 8) and 0xff
    val Int.blue: Int get() = this and 0xff
    fun Int.random(): Int = Random.nextInt(this + 1)
}
