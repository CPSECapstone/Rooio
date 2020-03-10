package com.rooio.repairs


import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.AdapterView.OnItemClickListener
import com.android.volley.Request
import org.json.JSONArray
import org.json.JSONException
import java.util.*


class ChangeLocationSettings  : NavigationBar(), OnItemClickListener  {

    private lateinit var addAnother: TextView
    private lateinit var locationListView: ListView
    private lateinit var errorMessage: TextView
    private lateinit var locationBox: ConstraintLayout
    private lateinit var loadingPanel: RelativeLayout
    private lateinit var backButton: ImageView
    private lateinit var viewGroup: ViewGroup
    private val url = "https://capstone.api.roopairs.com/v0/service-locations/"

    //Static lists that hold the address list and location ids, move to REST API?
    companion object {
        @JvmStatic var addressList = ArrayList<String>()
        @JvmStatic var locationIds = ArrayList<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_location_settings)

        initializeVariables()
        setNavigationBar()
        setActionBar()
        createNavigationBar("settings")
        loadLocations(JsonRequest(false, url, HashMap(), responseFunc, errorFunc, true))
        onAddAnother()
        onBack()
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


    //Initializes UI variables
    private fun initializeVariables() {
        addAnother = findViewById(R.id.addAnother)
        locationListView = findViewById(R.id.locationListView)
        errorMessage = findViewById(R.id.errorMessage)
        locationBox = findViewById(R.id.locationBox)
        loadingPanel = findViewById(R.id.loadingPanel)
        locationListView.onItemClickListener = this
        viewGroup = findViewById(R.id.background)
        //Navigation bar collapse/expand
        backButton = viewGroup.findViewById(R.id.backButton)
    }

    //Calls the API initially to load the locations into the page
    private fun loadLocations(request: JsonRequest) {
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
    private fun onAddAnother() {
        addAnother.setOnClickListener {
            startActivity(Intent(this@ChangeLocationSettings, AddLocationSettings::class.java))
        }
    }

    //Starts the next page when a location is clicked on
    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        Toast.makeText(this, "You chose " + addressList[position], Toast.LENGTH_SHORT).show()
        userLocationID = locationIds[position]
        startActivity(Intent(this@ChangeLocationSettings, LocationSettings::class.java))
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
            startActivity(Intent(this@ChangeLocationSettings, LocationSettings::class.java))
        }
    }

}