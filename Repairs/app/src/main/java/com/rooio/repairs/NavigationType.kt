package com.rooio.repairs

enum class NavigationType (private val value: String) {
    DASHBOARD("DASHBOARD"),
    SETTINGS("SETTINGS"),
    EQUIPMENT("EQUIPMENT"),
    JOBS("JOBS");

    override fun toString(): String {
        return value
    }

    fun getIntRepr(): Int {
        return when (this) {
            NavigationType.DASHBOARD -> 1
            NavigationType.SETTINGS -> 2
            NavigationType.EQUIPMENT -> 3
            NavigationType.JOBS -> 4
        }
    }
}