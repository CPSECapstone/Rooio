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
        MAINTENANCE,
        INSTALLATION,
        ALL;

        fun getInt(): Int {
            return when (this) {
                REPAIR -> 1
                MAINTENANCE -> 2
                INSTALLATION -> 3
                ALL -> 4
            }
        }
    }
    enum class OptionType  {
        COST,
        JOBS,
        TOTAL_COST,
        TOTAL_JOBS;


        fun getInt(): Int {
            return when (this) {
                COST -> 1
                JOBS -> 2
                TOTAL_COST -> 3
                TOTAL_JOBS -> 4
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