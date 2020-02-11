package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.arch.core.util.Function
import org.json.JSONObject
import java.util.*

class LocationSettings  : NavigationBar() {

    private var curLocation: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_settings)

        //Location text view
        curLocation = findViewById(R.id.CurrLocation)
        getCurrLocation()

        //sets the navigation bar onto the page
        val navInflater = layoutInflater
        val tmpView = navInflater.inflate(R.layout.activity_navigation_bar, null)

        window.addContentView(tmpView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        //sets the action bar onto the page
        val actionbarInflater = layoutInflater
        val actionBarView = actionbarInflater.inflate(R.layout.action_bar, null)
        window.addContentView(actionBarView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        createNavigationBar("settings")


        val spinner: Spinner = findViewById(R.id.settings_spinner)


        // setting on click listeners for the spinner items
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
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
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
            spinner.setSelection(getIntent().getIntExtra("UniqueKey", 0))
        }
    }

    private fun getCurrLocation(){
        val url = "https://capstone.api.roopairs.com/v0/service-locations/"

        val params = HashMap<String, Any>()
        params["service_location_id"] = getUserLocationID()

        requestPostJsonObj(JsonRequest(false, url, params, responseFunc, errorFunc, true))

    }

    @JvmField
    var responseFunc = Function<Any, Void?> { jsonObj: Any ->
            val responseObj = jsonObj as JSONObject
            curLocation!!.text = responseObj.getString("physical_address_formatted")

        null
    }
    @JvmField
    var errorFunc = Function<String, Void?> { string: String? ->
//        errorMessage!!.setTextColor(Color.parseColor("#E4E40B0B"))
//        errorMessage!!.setText(R.string.errorLogin)
        null
    }


    override fun animateActivity(boolean: Boolean)
    {

    }
}