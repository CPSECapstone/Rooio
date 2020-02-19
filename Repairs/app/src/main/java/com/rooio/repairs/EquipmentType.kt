package com.rooio.repairs

enum class EquipmentType (val value: String) {
    HVAC("Hvac"),
    PLUMBING("Plumbing"),
    LIGHTING_AND_ELECTRICAL("Lighting and Electrical"),
    GENERAL_APPLIANCE("General Appliance");

    override fun toString(): String {
        return value
    }

    fun getIntRepr(): Int {
        when (this) {
            HVAC -> return 1
            PLUMBING -> return 2
            LIGHTING_AND_ELECTRICAL -> return 3
            GENERAL_APPLIANCE -> return 4
        }
    }
}