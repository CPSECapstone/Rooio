package com.rooio.repairs

import org.json.JSONArray
import org.json.JSONObject

class JobData(jsonObject: JSONObject) {

    var id: String
    var status: JobType
    var statusTimeValue: String
    var equipmentList: ArrayList<EquipmentData>
    var serviceType: ServiceType
    var strRepr: String
    var serviceLocation: JSONObject
    var estimatedArrivalTime: String

    init {
        this.id = jsonObject.getString("id")
        this.status = createJobType(jsonObject.getInt("status"))
        this.statusTimeValue = jsonObject.getString("status_time_value")
        this.equipmentList = createEquipmentList(jsonObject.getJSONArray("equipment"))
        this.serviceType = createServiceType(jsonObject.getInt("service_type"))
        this.strRepr = jsonObject.getString("__str__")
        this.serviceLocation = jsonObject.getJSONObject("service_location")
        this.estimatedArrivalTime = jsonObject.getString("estimated_arrival_time")
    }

    private fun createJobType(i: Int): JobType {
        when (i) {
            0 -> return JobType.PENDING
            1 -> return JobType.DECLINED
            2 -> return JobType.SCHEDULED
            3 -> return JobType.COMPLETED
            4 -> return JobType.CANCELLED
            5 -> return JobType.STARTED
            6 -> return JobType.PAUSED
            7 -> return JobType.IN_PROGRESS
            else -> throw IllegalArgumentException("\$i does not correspond to an equipment type")
        }
    }

    private fun createEquipmentList(jsonArray: JSONArray): ArrayList<EquipmentData> {
        val list: ArrayList<EquipmentData> = ArrayList()
        for (i in 0 until jsonArray.length()) {
            val equipment = EquipmentData(jsonArray.getJSONObject(i))
            list.add(equipment)
        }
        return list
    }

    private fun createServiceType(i: Int): ServiceType {
        when(i) {
            1 -> return ServiceType.REPAIR
            2 -> return ServiceType.INSTALLATION
            3 -> return ServiceType.MAINTENANCE
            else -> throw IllegalArgumentException("\$i does not correspond to a service type")
        }
    }
}