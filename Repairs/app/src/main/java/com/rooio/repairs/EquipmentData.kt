package com.rooio.repairs

import org.json.JSONObject

class EquipmentData {

    var id: String = ""
    var name: String = ""
    var location: String = ""
    var manufacturer: String = ""
    var modelNumber: String = ""
    var serialNumber: String = ""
    var lastServiceDate: String = ""
    var lastServiceBy: String = ""
    var notes: String = ""
    var serviceLocation: String = ""
    var type: EquipmentType = EquipmentType.HVAC
    var serviceCategory: String = ""

    constructor(jsonObject: JSONObject) {
        this.id = jsonObject.getString("id")
        this.name = jsonObject.getString("display_name")
        this.location = jsonObject.getString("location")
        this.manufacturer = jsonObject.getString("manufacturer")
        this.modelNumber = jsonObject.getString("model_number")
        this.serialNumber = jsonObject.getString("serial_number")
        this.lastServiceDate = jsonObject.getString("last_service_date")
        this.lastServiceBy = jsonObject.getString("last_service_by")
        this.notes = jsonObject.getString("notes")
        this.serviceLocation = jsonObject.getString("service_location")
        this.type = createEnum(jsonObject.getInt("type"))
        this.serviceCategory = jsonObject.getString("service_category")

    }

    constructor(name: String, type: EquipmentType) {
        this.name = name
        this.type = type
    }

    private fun createEnum(i: Int): EquipmentType {
        when (i) {
            1 -> return EquipmentType.HVAC
            2 -> return EquipmentType.LIGHTING_AND_ELECTRICAL
            3 -> return EquipmentType.PLUMBING
            4 -> return EquipmentType.APPLIANCE
            else -> throw IllegalArgumentException("\$i does not correspond to an equipment type")
        }
    }
}
