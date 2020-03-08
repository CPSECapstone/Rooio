package com.rooio.repairs

import org.json.JSONArray
import org.json.JSONObject

// Class for handling preferred provider data sent from the API
// Each attribute corresponds to the API equivalent
class ProviderData(jsonObject: JSONObject) {

    var id: Int = jsonObject.get("id") as Int
    var name: String = jsonObject.get("name") as String
    var email: String = jsonObject.get("email") as String
    var phone: String = jsonObject.get("phone") as String
    var website: String = jsonObject.get("website") as String
    var logo: String = try { jsonObject.get("logo") as String }
    catch (e: Exception){ "" }
    var physicalAddress: String = jsonObject.get("physical_address") as String
    var incorporated: String = jsonObject.get("incorporated") as String
    var contractor_license_number: String = jsonObject.get("contractor_license_number") as String
    var bio: String = jsonObject.get("bio") as String
    var startingHourlyRate: String = jsonObject.get("starting_hourly_rate") as String
    var certified: Boolean = jsonObject.get("certified") as Boolean
    var skills: ArrayList<String> = ArrayList()

    init {
        val response = jsonObject.get("skills") as JSONArray
        for (i in 0 until response.length()) {
            val skill = response.get(i) as String
            skills.add(skill)
        }
    }
}