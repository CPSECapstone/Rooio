package com.rooio.repairs

import java.lang.IllegalArgumentException

enum class JobType (private val value: String) {

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

        fun
        getIntRepr(): Int {
        return when (this) {
                PENDING -> 0
                DECLINED -> 1
                SCHEDULED -> 2
                COMPLETED -> 3
                STARTED -> 5
                PAUSED -> 6
                CANCELLED -> 4
                IN_PROGRESS -> 7
        }
        }
        }