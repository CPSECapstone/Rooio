package com.rooio.repairs

enum class StatusType(private val value: Int) {
    Pending(0),
    Declined(1),
    Accepted(2),
    Completed(3),
    Cancelled(4),
    Started(5),
    Paused(6);

    fun getInt() : Int{
        return value
    }

    override fun toString(): String {
        return when(this){
            Pending -> "Pending"
            Declined -> "Declined"
            Accepted -> "Accepted"
            Completed -> "Completed"
            Cancelled -> "Cancelled"
            Started -> "Started"
            Paused -> "Paused"
        }
    }
}