package com.rooio.repairs

enum class GraphType {
    EQUIPMENT,
    DASHBOARD;

    fun getInt(): Int {
        return when (this) {
            EQUIPMENT -> 1
            DASHBOARD -> 2
        }
    }

    enum class JobType  {
        REPAIR,
        INSTALLATION,
        MAINTENANCE,
        ALL;

        fun getInt(): Int {
            return when (this) {
                REPAIR -> 1
                INSTALLATION -> 2
                MAINTENANCE -> 3
                ALL -> 4
            }
        }
    }
    enum class OptionType  {
        COST,
        JOBS;

        fun getInt(): Int {
            return when (this) {
                COST -> 1
                JOBS -> 2
            }
        }
    }

    enum class TimeType  {
        MONTH,
        YEAR;

        fun getInt(): Int {
            return when (this) {
                MONTH -> 1
                YEAR -> 2
            }
        }
    }
}