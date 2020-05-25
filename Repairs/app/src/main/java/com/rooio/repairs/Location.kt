package com.rooio.repairs

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import org.json.JSONArray
import org.json.JSONException
import java.util.*

//Location class that shares methods
abstract class Location : NavigationBar(), OnItemClickListener {
    enum class LocationType {
        LOGIN,
        SETTINGS;
    }

    private lateinit var addAnother: TextView
    private lateinit var locationListView: ListView
    private lateinit var errorMessage: TextView
    private lateinit var locationBox: ConstraintLayout
    private lateinit var loadingPanel: ProgressBar
    private lateinit var type: LocationType

    //Static lists that hold the address list and location ids, move to REST API?
    companion object {
        @JvmStatic var addressList = ArrayList<String>()
        @JvmStatic var locationIds = ArrayList<String>()
    }

    //Initializes UI variables
    fun initializeCommonVariables(type: LocationType) {
        addAnother = findViewById(R.id.addAnother)
        locationListView = findViewById(R.id.locationListView)
        errorMessage = findViewById(R.id.errorMessage)
        locationBox = findViewById(R.id.locationBox)
        loadingPanel = findViewById(R.id.loadingPanel)
        locationListView.onItemClickListener = this
        this.type = type
    }

    //Calls the API initially to load the locations into the page
    fun loadLocations(request: JsonRequest) {
        loadingPanel.visibility = View.VISIBLE
        requestJson(Request.Method.GET, JsonType.ARRAY, request)
    }

    //Fills the list view with locations that the API sends back
    var responseFunc = Function<Any, Void?> { response: Any ->
        loadingPanel.visibility = View.GONE
        try {
            val jsonArray = response as JSONArray
            addElements(jsonArray)
        } catch (e: JSONException) {
            errorMessage.setText(R.string.error_server)
        }
        null
    }

    //Returns an error if the user is not authorized to get this information
    var errorFunc = Function<String, Void?> { error: String? ->
        loadingPanel.visibility = View.GONE
        errorMessage.text = error
        null
    }

    //Sends user to add a location when the button is clicked
    fun onAddAnother() {
        addAnother.setOnClickListener {
            if (type == LocationType.LOGIN) startActivity(Intent(this@Location, AddLocationLogin::class.java))
            else startActivity(Intent(this@Location, AddLocationSettings::class.java))
        }
    }

    //Starts the next page when a location is clicked on
    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        Toast.makeText(this, "You chose " + addressList[position], Toast.LENGTH_SHORT).show()
        userLocationID = locationIds[position]

        val prefs = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(employeeId + "__userLocationId", locationIds[position])
        editor.apply()

        if (type == LocationType.LOGIN) startActivity(Intent(this@Location, PreferredProvidersLogin::class.java))
        else startActivity(Intent(this@Location, ChangeLocationSettings::class.java))
    }

    //Adds locations to the list view using a custom adapter, and increases the size every time a
    // location is added
    @Throws(JSONException::class)
    private fun addElements(response: JSONArray) {
        addressList.clear()
        locationIds.clear()
        for (i in 0 until response.length()) {
            val restaurant = response.getJSONObject(i)
            val physicalAddress = restaurant.getString("physical_address")
            addressList.add(physicalAddress)
            locationIds.add(restaurant.getString("id"))
            if (i > 0) {
                val params = locationBox.layoutParams
                params.height += 90
                locationBox.layoutParams = params
                val size = locationListView.layoutParams
                size.height += 90
                locationListView.layoutParams = size
            }
        }
        val customAdapter = LocationCustomAdapter(this, addressList)
        if (addressList.size != 0) locationListView.adapter = customAdapter
    }
}