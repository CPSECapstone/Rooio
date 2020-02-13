package com.rooio.repairs


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.arch.core.util.Function
import org.json.JSONArray
import org.json.JSONException
import java.util.*


class ChangeLocationSettings  : NavigationBar() {

    companion object{
        @JvmStatic private var address_list = ArrayList<String>()
        @JvmStatic private var locationIds = HashMap<String?, String?>()
    }

    private var listView: ListView? = null
    private var errorMessage: TextView? = null
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_location_settings)
        val addAnother = findViewById<TextView>(R.id.addAnother)
        val backArrow = findViewById<ImageView>(R.id.backArrow)
        errorMessage = findViewById(R.id.errorMessage)

        listView = findViewById(R.id.service)

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

        addAnother.setOnClickListener{
            startActivity(Intent( this, AddLocationSettings::class.java))
        }

        backArrow.setOnClickListener{
            startActivity(Intent( this, LocationSettings::class.java))
        }

        //New Address Recieved

        val incomingIntent = intent
        var incoming_address = incomingIntent.getStringExtra("address")
        if (incoming_address != null) {

            var incoming_id = incomingIntent.getStringExtra("id")
            locationIds[incoming_address] = incoming_id

            address_list.add(incoming_address as String)
            adapt()
            adapter.notifyDataSetChanged()
        } else {
            address_list.clear()
            locationIds.clear()

            getLocations()
        }

        listView?.onItemClickListener = object : AdapterView.OnItemClickListener{

            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val tv = view as TextView
                Toast.makeText(this@ChangeLocationSettings, "You chose " + tv.text, Toast.LENGTH_SHORT).show()

                userLocationID = locationIds[tv.text]

                val intent1 = Intent(this@ChangeLocationSettings, LocationSettings::class.java)
                startActivity(intent1)

            }
        }
    }

    private fun getLocations(){
        val url = "https://capstone.api.roopairs.com/v0/service-locations/"
        requestGetJsonArray(JsonRequest(false, url, null, responseFunc, errorFunc, true))
    }

    @JvmField
    var responseFunc = Function<Any, Void?> { jsonObj: Any ->
        try {
            val jsonArray = jsonObj as JSONArray
            addElements(jsonArray)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        adapt()
        null
    }

    @JvmField
    var errorFunc = Function<String, Void?> { string: String? ->
        errorMessage!!.text = string
        null
    }

    @Throws(JSONException::class)
    fun addElements(response: JSONArray) {
        for (i in 0 until response.length()) {
            val restaurant = response.getJSONObject(i)
            val physical_address = restaurant.getString("physical_address")
            address_list.add(physical_address)

            locationIds[physical_address] = restaurant.getString("id")
        }
    }

    fun adapt() {
        adapter = ArrayAdapter(this@ChangeLocationSettings,
                android.R.layout.simple_list_item_1, address_list)
        listView?.setAdapter(adapter)
    }

    override fun animateActivity(boolean: Boolean)
    {

    }



}