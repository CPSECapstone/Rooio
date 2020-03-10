package com.rooio.repairs


import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.arch.core.util.Function
import com.android.volley.Request
import org.json.JSONArray
import org.json.JSONException
import java.util.*


class AddLocationSettings  : NavigationBar() {

    private lateinit var addLocation: Button
    private lateinit var newLocation: EditText
    private lateinit var errorMessage: TextView
    private lateinit var loadingPanel: RelativeLayout
    private lateinit var backButton: ImageView
    private lateinit var viewGroup: ViewGroup
    private val url = "https://capstone.api.roopairs.com/v0/service-locations/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location_settings)

        initializeVariables()
        setNavigationBar()
        setActionBar()
        createNavigationBar("settings")
        onAddLocation()
        onBack()
    }

    //Initializes UI variables
    private fun initializeVariables() {
        addLocation = findViewById(R.id.addLocation)
        newLocation = findViewById(R.id.newLocation)
        errorMessage = findViewById(R.id.errorMessage)
        loadingPanel = findViewById(R.id.loadingPanel)
        viewGroup = findViewById(R.id.background)
        //Navigation bar collapse/expand
        backButton = viewGroup.findViewById(R.id.backButton)
    }

    //Sets the navigation bar onto the page
    private fun setNavigationBar() {
        val navBarInflater = layoutInflater
        val navBarView = navBarInflater.inflate(R.layout.activity_navigation_bar, null)
        window.addContentView(navBarView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    //Sets the action bar onto the page
    private fun setActionBar() {
        val actionBarInflater = layoutInflater
        val actionBarView = actionBarInflater.inflate(R.layout.action_bar, null)
        window.addContentView(actionBarView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        supportActionBar!!.elevation = 0.0f
    }

    //Handles when a user adds a location
    private fun onAddLocation() {
        loadingPanel.visibility = View.GONE
        addLocation.setOnClickListener {
            errorMessage.text = ""
            val locationInput = newLocation.text.toString()
            val request = JsonRequest(false, url, HashMap(), checkResponseFunc, checkErrorFunc, true)
            checkLocationInfo(locationInput, request)
        }
    }

    //Checks if a location is not empty and then gets all the locations from the API
    private fun checkLocationInfo(inputAddress: String, request: JsonRequest) {
        if (inputAddress != "") {
            loadingPanel.visibility = View.VISIBLE
            requestJson(Request.Method.GET, JsonType.ARRAY, request)
        } else errorMessage.setText(R.string.invalid_address)
    }

    //Handles an error if the API is unable to retrieve phone numbers for the account
    @JvmField
    val checkErrorFunc = Function<String, Void?> { error: String? ->
        loadingPanel.visibility = View.GONE
        errorMessage.text = error
        null
    }

    //Checks if the location is already in the system and tries to add it
    @JvmField
    val checkResponseFunc = Function<Any, Void?> { response: Any ->
        try {
            val jsonArray = response as JSONArray
            val locationInput = newLocation.text.toString()
            val params = HashMap<Any?, Any?>()
            params["physical_address"] = locationInput
            val request = JsonRequest(false, url, params, locationResponseFunc, locationErrorFunc, true)
            addLocation(checkAlreadyAdded(locationInput, jsonArray), request)
        } catch (e: JSONException) {
            errorMessage.setText(R.string.error_server)
        }
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
            errorMessage.setText(R.string.already_added_location)
        }
    }

    //Response that reloads the location list view with the new location
    @JvmField
    var locationResponseFunc = Function<Any, Void?> {
        loadingPanel.visibility = View.GONE
        try {
            startActivity(Intent(this@AddLocationSettings, ChangeLocationSettings::class.java))
        } catch (e: JSONException) {
            errorMessage.setText(R.string.error_server)
        }
        null
    }

    //Error checking for if there are any issues with requests and API
    @JvmField
    var locationErrorFunc = Function<String, Void?> { error: String? ->
        loadingPanel.visibility = View.GONE
        errorMessage.text = error
        null
    }

    //Animates the main page content when the navigation bar collapses/expands
    override fun animateActivity(boolean: Boolean)
    {
        val amount = if (boolean) -190f else 0f
        val animation = ObjectAnimator.ofFloat(backButton, "translationX", amount)
        if (boolean) animation.duration = 1300 else animation.duration = 300
        animation.start()
    }

    //Sends the user to the Jobs page
    private fun onBack() {
        backButton.setOnClickListener{
            startActivity(Intent(this@AddLocationSettings, ChangeLocationSettings::class.java))
        }
    }
}