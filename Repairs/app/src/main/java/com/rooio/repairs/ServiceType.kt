package com.rooio.repairs

enum class ServiceType(private val value: Int) {
    REPAIR(1),
    INSTALLATION(2),
    MAINTENANCE(3);

    override fun toString(): String {
        return when(this){
            REPAIR -> "Repair"
            INSTALLATION -> "Installation"
            MAINTENANCE -> "Maintenance"
        }
    }
}