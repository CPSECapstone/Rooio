package com.rooio.repairs

enum class EquipmentType (private val value: String) {
    HVAC("HVAC"),
    PLUMBING("Plumbing"),
    LIGHTING_AND_ELECTRICAL("Lighting and Electrical"),
    GENERAL_APPLIANCE("General Appliance");

    override fun toString(): String {
        return value
    }

    fun getIntRepr(): Int {
        return when (this) {
            HVAC -> 1
            PLUMBING -> 2
            LIGHTING_AND_ELECTRICAL -> 3
            GENERAL_APPLIANCE -> 4
        }
    }
}