package com.rooio.repairs


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.arch.core.util.Function
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class AddLocationSettings  : NavigationBar() {

    private var errorMessage: TextView? = null
    private lateinit var new_address: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location_settings)
        val backArrow = findViewById<ImageView>(R.id.backArrow)

        val addAddress = findViewById<Button>(R.id.addLocation)

        errorMessage = findViewById(R.id.error_Messages)
        new_address = findViewById(R.id.newLocation2)

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

        supportActionBar!!.elevation = 0.0f

        createNavigationBar("settings")

        backArrow.setOnClickListener{
            startActivity(Intent( this, ChangeLocationSettings::class.java))
        }

        addAddress.setOnClickListener{
            val url = "https://capstone.api.roopairs.com/v0/service-locations/"

            errorMessage?.setTextColor(Color.parseColor("#A6A9AC"))
            errorMessage?.text = "Loading"

            val inputted_address = new_address?.text!!.toString()
            if (inputted_address != "") {
                //     -- Example params initiations
                val params = HashMap<String, Any>()
                params["physical_address"] = inputted_address

                val request = JsonRequest(false, url, params, responseFunc, errorFunc, true)
                requestPostJsonObj(request)

            } else {
                errorMessage?.setTextColor(Color.parseColor("#E4E40B0B"))
                errorMessage?.text = "Invalid Address"
            }
        }
    }

    @JvmField
    var responseFunc = Function<Any, Void?> { jsonObj: Any ->
        val responseObj = jsonObj as JSONObject
        var address: String? = null
        var id: String? = null

        try {
            address = responseObj.getString("physical_address")
            id = responseObj.getString("id")

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        val intent = Intent(this@AddLocationSettings, ChangeLocationSettings::class.java)
        intent.putExtra("address", address)
        intent.putExtra("id", id)
        startActivity(intent)
        null
    }
    @JvmField
    var errorFunc = Function<String, Void?> { string: String? ->

        errorMessage!!.text = string
        null
    }

    override fun animateActivity(boolean: Boolean)
    {

    }



}