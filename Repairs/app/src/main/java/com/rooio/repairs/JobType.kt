package com.rooio.repairs

enum class JobType(private val value: String) {

    PENDING("Pending"),
    SCHEDULED("Scheduled"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    DECLINED("Declined"),
    STARTED("Started"),
    PAUSED("Paused");

    override fun toString(): String {
        return value
    }

    fun getInt(): Int {
        return when (this) {
            PENDING -> 0
            DECLINED -> 1
            SCHEDULED -> 2
            COMPLETED -> 3
            CANCELLED -> 4
            STARTED -> 5
            PAUSED -> 6
            IN_PROGRESS -> 7
        }
    }

    fun getColor(): Int {
        return when (this) {
            PENDING -> R.color.Purple
            DECLINED -> R.color.lightGray
            SCHEDULED -> R.color.Blue
            COMPLETED -> R.color.lightGray
            STARTED -> R.color.colorPrimary
            PAUSED -> R.color.Yellow
            CANCELLED -> R.color.lightGray
            IN_PROGRESS -> R.color.colorPrimary
        }
    }
}