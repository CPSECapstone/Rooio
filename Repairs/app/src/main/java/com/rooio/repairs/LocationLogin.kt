package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.ActionBar
import androidx.arch.core.util.Function
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class LocationLogin : RestApi(), OnItemClickListener {
    private var addLocation: Button? = null
    private var lv: ListView? = null
    private var errorMessage: TextView? = null
    private var adapter: ArrayAdapter<String>? = null
    private var incomingAddress: String? = null
    private var incomingId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_login)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        supportActionBar!!.elevation = 0f
        addLocation = findViewById<View>(R.id.addLocation) as Button
        lv = findViewById<View>(R.id.Service) as ListView
        errorMessage = findViewById<View>(R.id.Error_Messages) as TextView
        //        test = (TextView) findViewById(R.id.test);
        val url = "https://capstone.api.roopairs.com/v0/service-locations/"
        //New Address Received
        val incomingIntent = intent
        incomingAddress = incomingIntent.getStringExtra("address")
        if (incomingAddress != null) {
            incomingId = incomingIntent.getStringExtra("id")
            locationIds[incomingAddress!!] = incomingId
            address_list.add(incomingAddress!!)
            adapt()
            adapter!!.notifyDataSetChanged()
        } else {
            address_list.clear()
            locationIds.clear()
            val request = JsonRequest(false, url, null, responseFunc, errorFunc, true)
            requestGetJsonArray(request)
        }
        //Launch listener for "Add New Location"
        onBtnClick()
        lv!!.onItemClickListener = this
    }

    var responseFunc = Function<Any, Void?> { jsonResponse: Any ->
        try {
            val jsonArray = jsonResponse as JSONArray
            addElements(jsonArray)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        adapt()
        null
    }
    var errorFunc = Function<String, Void?> { string: String? ->
        errorMessage!!.text = string
        null
    }

    private fun onBtnClick() {
        addLocation!!.setOnClickListener {
            val intent1 = Intent(this@LocationLogin, AddLocationLogin::class.java)
            startActivity(intent1)
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        val tv = view as TextView
        Toast.makeText(this, "You chose " + tv.text, Toast.LENGTH_SHORT).show()
        userLocationID = locationIds[tv.text]
        val intent1 = Intent(this@LocationLogin, PreferredProvidersLogin::class.java)
        startActivity(intent1)
    }

    @Throws(JSONException::class)
    private fun addElements(response: JSONArray) {
        for (i in 0 until response.length()) {
            val restaurant = response.getJSONObject(i)
            val physicalAddress = restaurant.getString("physical_address")
            address_list.add(physicalAddress)
            locationIds[physicalAddress] = restaurant.getString("id")
        }
    }

    private fun adapt() {
        adapter = ArrayAdapter(this@LocationLogin, android.R.layout.simple_list_item_1, address_list)
        lv!!.adapter = adapter
    }

    companion object {
        //TextView test;
        var address_list = ArrayList<String>()
        var locationIds = HashMap<String, String?>()
    }
}