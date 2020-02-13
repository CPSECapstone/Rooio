package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.ActionBar
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class LocationLogin : RestApi(), OnItemClickListener {
    private var addAnother: TextView ? = null
    private var lv: ListView? = null
    private var errorMessage: TextView? = null
    private var locationBox: ConstraintLayout? = null
    private val url = "https://capstone.api.roopairs.com/v0/service-locations/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_login)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        supportActionBar!!.elevation = 0f
        addAnother = findViewById<View>(R.id.addAnother) as TextView
        lv = findViewById<View>(R.id.service) as ListView
        errorMessage = findViewById<View>(R.id.errorMessage) as TextView
        locationBox = findViewById<View>(R.id.locationBox) as ConstraintLayout
        loadLocations()
        onBtnClick()
        lv!!.onItemClickListener = this
    }
    private fun loadLocations() {
        val request = JsonRequest(false, url, null, responseFunc, errorFunc, true)
        requestGetJsonArray(request)
    }

    var responseFunc = Function<Any, Void?> { jsonResponse: Any ->
        try {
            val jsonArray = jsonResponse as JSONArray
            addElements(jsonArray)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        null
    }
    var errorFunc = Function<String, Void?> { string: String? ->
        errorMessage!!.text = string
        null
    }

    private fun onBtnClick() {
        addAnother!!.setOnClickListener {
            startActivity(Intent(this@LocationLogin, AddLocationLogin::class.java))
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        Toast.makeText(this, "You chose " + addressList[position], Toast.LENGTH_SHORT).show()
        userLocationID = locationIds[position]
        startActivity(Intent(this@LocationLogin, PreferredProvidersLogin::class.java))
    }

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
                val params = locationBox!!.layoutParams
                params.height += 90
                locationBox!!.layoutParams = params
                val size = lv!!.layoutParams
                size.height += 90
                lv!!.layoutParams = size
            }
        }

        val customAdapter = LocationCustomAdapter(this, addressList)
        if (addressList.size != 0) lv!!.adapter = customAdapter
    }

    companion object {
        //TextView test;
        var addressList = ArrayList<String>()
        var locationIds = ArrayList<String>()
    }
}
