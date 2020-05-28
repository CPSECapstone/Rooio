package com.rooio.repairs

enum class StatusType(private val value: Int) {
    PENDING(0),
    DECLINED(1),
    ACCEPTED(2),
    COMPLETED(3),
    CANCELLED(4),
    STARTED(5),
    PAUSED(6);

    fun getInt() : Int{
        return value
    }

    override fun toString(): String {
        return when(this){
            PENDING -> "Pending"
            DECLINED -> "Declined"
            ACCEPTED -> "Accepted"
            COMPLETED -> "Completed"
            CANCELLED -> "Cancelled"
            STARTED -> "Started"
            PAUSED -> "Paused"
        }
    }
}