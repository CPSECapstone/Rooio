package com.rooio.repairs

import android.content.Intent
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.Request
import org.json.JSONArray
import java.util.HashMap
import androidx.arch.core.util.Function

//Location activity that holds all the logic shared between pages
abstract class AddLocation : NavigationBar() {

    enum class AddLocationType {
        LOGIN,
        SETTINGS;
    }

    private lateinit var addLocation: Button
    private lateinit var errorMessage: TextView
    private lateinit var loadingPanel: ProgressBar
    private lateinit var autocomplete: AutoCompleteTextView
    private val url = "service-locations/"
    private lateinit var type: AddLocationType

    //Initializes UI variables
    fun initializeCommonVariables(type: AddLocationType) {
        addLocation = findViewById(R.id.addLocation)
        errorMessage = findViewById(R.id.errorMessage)
        loadingPanel = findViewById(R.id.loadingPanel)
        this.type = type
        initializeAutoComplete()
    }

    private fun initializeAutoComplete() {
        if (type == AddLocationType.LOGIN) {
            autocomplete = findViewById(R.id.autocomplete_login)
            autocomplete.setAdapter(PlaceAutoCompleteAdapter(this, android.R.layout.simple_list_item_1))
        } else {
            autocomplete = findViewById(R.id.autocomplete_setting)
            autocomplete.setAdapter(PlaceAutoCompleteAdapter(this, android.R.layout.simple_list_item_1))
        }
    }

    //Handles when a user adds a location
    fun onAddLocation() {
        loadingPanel.visibility = View.GONE
        addLocation.visibility = View.VISIBLE
        addLocation.setOnClickListener {
            errorMessage.text = ""
            val locationInput = autocomplete.text.toString()
            val request = JsonRequest(false, url, null, checkResponseFunc, checkErrorFunc, true)
            checkLocationInfo(locationInput, request)
        }
    }

    //Checks if a location is not empty and then gets all the locations from the API
    private fun checkLocationInfo(inputAddress: String, request: JsonRequest) {
        if (inputAddress != "") {
            loadingPanel.visibility = View.VISIBLE
            addLocation.visibility = View.GONE
            requestJson(Request.Method.GET, JsonType.ARRAY, request)
        } else errorMessage.setText(R.string.invalid_address)
    }

    //Handles an error if the API is unable to retrieve phone numbers for the account
    @JvmField
    val checkErrorFunc = Function<String, Void?> { error: String? ->
        loadingPanel.visibility = View.GONE
        addLocation.visibility = View.VISIBLE
        errorMessage.text = error
        null
    }

    //Checks if the location is already in the system and tries to add it
    @JvmField
    val checkResponseFunc = Function<Any, Void?> { response: Any ->
        val jsonArray = response as JSONArray
        val locationInput = autocomplete.text.toString()
        val params = HashMap<Any?, Any?>()
        params["physical_address"] = locationInput
        val request = JsonRequest(false, url, params, locationResponseFunc, locationErrorFunc, true)
        addLocation(checkAlreadyAdded(locationInput, jsonArray), request)
        null
    }

    //Goes through each location returned from the API and checks if it is equal to the current
    private fun checkAlreadyAdded(addressInput: String, jsonArray: JSONArray): Boolean {
        for (i in 0 until jsonArray.length()) {
            val location = jsonArray.getJSONObject(i)
            val address = location["physical_address"] as String
            if (address == addressInput) {
                return true
            }
        }
        return false
    }

    //Takes in a boolean if it has been added to the system and makes a call to the API if it has not
    private fun addLocation(added: Boolean, request: JsonRequest) {
        if (!added) {
            requestJson(Request.Method.POST, JsonType.OBJECT, request)
        } else {
            loadingPanel.visibility = View.GONE
            addLocation.visibility = View.VISIBLE
            errorMessage.setText(R.string.already_added_location)
        }
    }



    //Response that reloads the location list view with the new location
    @JvmField
    var locationResponseFunc = Function<Any, Void?> {
        loadingPanel.visibility = View.GONE
        addLocation.visibility = View.VISIBLE
        if (type == AddLocationType.LOGIN) startActivity(Intent(this@AddLocation, LocationLogin::class.java))
        else startActivity(Intent(this@AddLocation, LocationSettings::class.java))
        null
    }

    //Error checking for if there are any issues with requests and API
    @JvmField
    var locationErrorFunc = Function<String, Void?> { error: String? ->
        loadingPanel.visibility = View.GONE
        addLocation.visibility = View.VISIBLE
        errorMessage.text = error
        null
    }
}