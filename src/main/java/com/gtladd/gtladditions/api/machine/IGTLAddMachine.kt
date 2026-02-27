package com.gtladd.gtladditions.api.machine

interface IGTLAddMachine {
    var limitedDuration: Int
        get() = 5
        set(duration) {}

    var isMultipleMode: Boolean
        get() = true
        set(mode) {}

    fun useModes(): Boolean {
        return true
    }
}
