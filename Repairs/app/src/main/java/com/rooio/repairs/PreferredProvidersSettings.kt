package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class PreferredProvidersSettings  : NavigationBar() {

    private lateinit var addButton: TextView
    private lateinit var serviceProvidersListView: ListView
    private lateinit var errorMessage: TextView
    private lateinit var providerBox: ConstraintLayout
    private lateinit var loadingPanel: RelativeLayout
    private lateinit var spinner: Spinner
    private val preferredProviders = ArrayList<ServiceProviderData>()
    private val url = "service-providers/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferred_providers_settings)

        initializeVariables()
        setNavigationBar()
        setActionBar()
        createNavigationBar("settings")
        setSpinner()
        loadPreferredProviders(JsonRequest(false, url, null, responseFunc, errorFunc, true))
        onAddAnother()
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
        addButton = findViewById(R.id.addAnother)
        serviceProvidersListView = findViewById(R.id.providerListView)
        errorMessage = findViewById(R.id.errorMessage)
        providerBox = findViewById(R.id.providerBox)
        loadingPanel = findViewById(R.id.loadingPanel)
        spinner = findViewById(R.id.settings_spinner)
    }

    // Initially loads the current providers by making a call to the API
    private fun loadPreferredProviders(request: JsonRequest) {
        requestJson(Request.Method.GET, JsonType.ARRAY, request)
    }

    //Loads all the elements from the JSON array or displays an error
    @JvmField
    val responseFunc = Function<Any, Void?> { response : Any ->
        loadingPanel.visibility = View.GONE
        val jsonArray = response as JSONArray
        try {
            loadElements(jsonArray)
        } catch (e: JSONException) {
            errorMessage.setText(R.string.error_server)
        }
        null
    }

    //Handles error from the API if the user is not authorized
    @JvmField
    val errorFunc = Function<String, Void?> { error: String? ->
        loadingPanel.visibility = View.GONE
        errorMessage.text = error
        null
    }

    //Individually loads each provider into the custom list view and extends the layout based on number
    @Throws(JSONException::class)
    fun loadElements(response: JSONArray) {
        preferredProviders.clear()
        for (i in 0 until response.length()) {
            val restaurant = response.getJSONObject(i)
            val name = restaurant["name"] as String
            val id = restaurant.get("id") as Int
            var image = ""
            try {
                image = restaurant["logo"] as String
            } catch (e: Exception) { // if there is no logo for the service provider
                image = ""
            } finally {
                val serviceProviderData = ServiceProviderData(name, image, id)
                preferredProviders.add(serviceProviderData)
                if (i > 0) {
                    val params = providerBox.layoutParams
                    params.height += 110
                    providerBox.layoutParams = params
                    val size = serviceProvidersListView.layoutParams
                    size.height += 110
                    serviceProvidersListView.layoutParams = size
                }
            }
        }
        val customAdapter = PreferredProvidersCustomAdapter(this, preferredProviders)
        if (preferredProviders.size != 0) serviceProvidersListView.adapter = customAdapter
    }

    //Handles when user wants to add another provider
    private fun onAddAnother() {
        addButton.setOnClickListener { startActivity(Intent(this@PreferredProvidersSettings, AddPreferredProvidersSettings::class.java)) }
    }

    //Set the spinner for the title bar
    private fun setSpinner() {
        // setting on click listeners for the spinner items
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == "Service Location") {
                    val spinner: Spinner = findViewById(R.id.settings_spinner)
                    spinner.onItemSelectedListener = this
                    val mIntent = Intent(this@PreferredProvidersSettings, LocationSettings::class.java)
                    mIntent.putExtra("UniqueKey", position)
                    startActivity(mIntent)
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
        }
    }

    //Animates anything along with the collapsing and expanding nav bar
    override fun animateActivity(boolean: Boolean)
    {
    }
}
