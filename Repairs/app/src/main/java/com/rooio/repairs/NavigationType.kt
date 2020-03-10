package com.rooio.repairs

enum class NavigationType (private val value: String) {
    DASHBOARD("DASHBOARD"),
    SETTINGS("SETTINGS"),
    EQUIPMENT("EQUIPMENT"),
    JOBS("JOBS");

//    override fun toString(): String {
////        return value
////    }
////
////    fun getIntRepr(): Int {
////        return when (this) {
////            DASHBOARD -> 1
////            SETTINGS -> 2
////            EQUIPMENT -> 3
////            JOBS -> 4
////        }
////    }
}