package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.arch.core.util.Function
import com.android.volley.Request
import org.json.JSONException
import java.util.*

//Page where user is able to add a location when initially logging in
class AddLocationLogin : RestApi() {

    private lateinit var addLocation: Button
    private lateinit var cancelButton: Button
    private lateinit var newLocation: EditText
    private lateinit var errorMessage: TextView
    private lateinit var loadingPanel: RelativeLayout
    private val url = "https://capstone.api.roopairs.com/v0/service-locations/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location_login)

        centerTitleBar()
        initializeVariables()
        onAddLocation()
        onCancel()
    }

    //Initializes UI variables
    private fun initializeVariables() {
        addLocation = findViewById(R.id.addLocation)
        cancelButton = findViewById(R.id.cancel)
        newLocation = findViewById(R.id.newLocation)
        errorMessage = findViewById(R.id.errorMessage)
        loadingPanel = findViewById(R.id.loadingPanel)
    }

    //Centers "Repairs" title
    private fun centerTitleBar() {
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        supportActionBar!!.elevation = 0f
    }

    //Handles when a user adds a location
    private fun onAddLocation() {
        loadingPanel.visibility = View.GONE
        addLocation.setOnClickListener {
            loadingPanel.visibility = View.VISIBLE
            val params = HashMap<Any?, Any?>()
            params["physical_address"] = newLocation.text.toString()
            val request = JsonRequest(false, url, params, responseFunc, errorFunc, true)
            sendLocationInfo(request)
        }
    }

    //Sends a location to the API through a request
    private fun sendLocationInfo(request: JsonRequest) {
        val inputAddress = request.params["physical_address"]
        if (inputAddress != "") {
            requestJson(Request.Method.POST, JsonType.OBJECT, request)
        } else errorMessage.setText(R.string.invalid_address)
    }

    //Sends user back to locations list view
    private fun onCancel() {
        cancelButton.setOnClickListener { startActivity(Intent(this@AddLocationLogin, LocationLogin::class.java)) }
    }

    //Response that reloads the location list view with the new location
    @JvmField
    var responseFunc = Function<Any, Void?> {
        loadingPanel.visibility = View.GONE
        try {
            startActivity(Intent(this@AddLocationLogin, LocationLogin::class.java))
        } catch (e: JSONException) {
            errorMessage.setText(R.string.error_server)
        }
        null
    }

    //Error checking for if there are any issues with requests and API
    @JvmField
    var errorFunc = Function<String, Void?> { error: String? ->
        loadingPanel.visibility = View.GONE
        errorMessage.text = error
        null
    }
}