package com.rooio.repairs

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.arch.core.util.Function
import com.android.volley.Request
import org.json.JSONObject

//Displays the current location of the user in Settings
class LocationSettings  : NavigationBar() {

    private lateinit var currentLocation: TextView
    private lateinit var errorMessage: TextView
    private lateinit var changeLocation: Button
    private lateinit var spinner: Spinner
    private lateinit var loadingPanel: RelativeLayout
    private val url = "service-locations/$userLocationID/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_settings)

        onResume()
        initializeVariables()
        setNavigationBar()
        setActionBar()
        createNavigationBar(NavigationType.SETTINGS)
        onChangeLocation()
        setSettingsSpinner()
        onPause()
        
        getCurrentLocation(JsonRequest(false, url, null, responseFunc, errorFunc, true))
        

    }

    //Handles when the user clicks the change location button
    private fun onChangeLocation() {
        changeLocation.setOnClickListener{
            startActivity(Intent(this, ChangeLocationSettings::class.java))
        }
    }

    //Handles clicking on the spinner items
    private fun setSettingsSpinner() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if(selectedItem == "Preferred Providers") {
                    val spinner: Spinner = findViewById(R.id.settings_spinner)
                    spinner.onItemSelectedListener = this
                    startActivity(Intent(this@LocationSettings, PreferredProvidersSettings::class.java))
                }
            }
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                this,
                R.array.settings_type,
                R.layout.spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
            spinner.setSelection(Intent().getIntExtra("UniqueKey", 1))
        }
    }


    //Initializes UI variables
    private fun initializeVariables() {
        currentLocation = findViewById(R.id.currentLocation)
        errorMessage = findViewById(R.id.errorMessage)
        changeLocation = findViewById(R.id.changeLocation)
        spinner = findViewById(R.id.settings_spinner)
        loadingPanel = findViewById(R.id.loadingPanel)
    }

    //Requests the current location from the API
    private fun getCurrentLocation(request: JsonRequest){
        requestJson(Request.Method.GET, JsonType.OBJECT, request)
    }

    //If API returns a location, sets the physical address
    @JvmField
    var responseFunc = Function<Any, Void?> { response: Any ->
        loadingPanel.visibility = View.GONE
        val responseObj = response as JSONObject
        currentLocation.text = responseObj.getString("physical_address")
        null
    }

    //Handles error if user does not have a location set
    @JvmField
    var errorFunc = Function<String, Void?> { error: String? ->
        loadingPanel.visibility = View.GONE
        errorMessage.text = error
        null
    }

    override fun animateActivity(boolean: Boolean)
    {
    }

    // gets rid of sound when the user clicks on the spinner
    public override fun onResume() {
    super.onResume()
    val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
    } else {
        am.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }

}

    public override fun onPause() {
        super.onPause()
        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
        }
        else {
            am.setStreamMute(AudioManager.STREAM_SYSTEM, false)
        }
    }
}