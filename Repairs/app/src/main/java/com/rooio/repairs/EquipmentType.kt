package com.rooio.repairs

enum class EquipmentType (private val value: String) {
    HVAC("HVAC"),
    LIGHTING_AND_ELECTRICAL("Lighting and Electrical"),
    PLUMBING("Plumbing"),
    GENERAL_APPLIANCE("General Appliance");

    override fun toString(): String {
        return value
    }

    fun getIntRepr(): Int {
        return when (this) {
            HVAC -> 1
            LIGHTING_AND_ELECTRICAL -> 2
            PLUMBING -> 3
            GENERAL_APPLIANCE -> 4
        }
    }
}