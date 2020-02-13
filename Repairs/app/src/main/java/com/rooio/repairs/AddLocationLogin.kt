package com.rooio.repairs

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.arch.core.util.Function
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class AddLocationLogin : RestApi() {
    private var addAddress: Button? = null
    private var backButton: ImageView? = null
    private var newAddress: EditText? = null
    private var errorMessage: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location_login)
        addAddress = findViewById<View>(R.id.addLocation) as Button
        backButton = findViewById<View>(R.id.backArrow) as ImageView
        newAddress = findViewById<View>(R.id.newLocationLogin) as EditText
        errorMessage = findViewById<View>(R.id.errorMessage) as TextView
        errorMessage!!.text = ""
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        supportActionBar!!.elevation = 0f
        onAddClick()
        onBackClick()
    }

    private fun onAddClick() {
        val url = "https://capstone.api.roopairs.com/v0/service-locations/"
        addAddress!!.setOnClickListener {
            errorMessage!!.setTextColor(Color.parseColor("#A6A9AC"))
            errorMessage!!.text = "Loading"
            val inputtedAddress = newAddress!!.text.toString()
            if (inputtedAddress != "") { //     -- Example params initiations
                val params = HashMap<String, Any>()
                params["physical_address"] = inputtedAddress
                val request = JsonRequest(false, url, params, responseFunc, errorFunc, true)
                requestPostJsonObj(request)
            } else {
                errorMessage!!.setTextColor(Color.parseColor("#E4E40B0B"))
                errorMessage!!.text = "Invalid Address"
            }
        }
        onCancel()
    }

    private fun onCancel() {
        backButton!!.setOnClickListener { view: View? -> startActivity(Intent(this@AddLocationLogin, LocationLogin::class.java)) }
    }

    private fun onBackClick() {
        backButton!!.setOnClickListener { startActivity(Intent(this@AddLocationLogin, LocationLogin::class.java)) }
    }

    private var responseFunc = Function<Any, Void?> { jsonObj: Any ->
        val responseObj = jsonObj as JSONObject
        var address: String? = null
        var id: String? = null
        try {
            address = responseObj.getString("physical_address")
            id = responseObj.getString("id")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val intent = Intent(this@AddLocationLogin, LocationLogin::class.java)
        intent.putExtra("address", address)
        intent.putExtra("id", id)
        startActivity(intent)
        null
    }
    var errorFunc = Function<String, Void?> { string: String? ->
        errorMessage!!.setTextColor(Color.parseColor("#E4E40B0B"))
        errorMessage!!.text = "Invalid Address"
        null
    }
}