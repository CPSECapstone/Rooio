package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import org.json.JSONArray
import org.json.JSONException
import java.util.*

//Page when user first logs in, they will see providers linked to their account and be able to add more
class PreferredProvidersLogin : RestApi() {

    private lateinit var addButton: TextView
    private lateinit var continueButton: Button
    private lateinit var serviceProvidersListView: ListView
    private lateinit var errorMessage: TextView
    private lateinit var providerBox: ConstraintLayout
    private lateinit var loadingPanel: RelativeLayout
    private val preferredProviders = ArrayList<ServiceProviderData>()
    private val url = "service-providers/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferred_providers_login)

        centerTitleBar()
        initializeVariables()
        loadPreferredProviders(JsonRequest(false, url, HashMap(), responseFunc, errorFunc, true))
        onAddAnother()
        onContinue()
    }

    //Centers "Repairs" title
    private fun centerTitleBar() {
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        supportActionBar!!.elevation = 0f
    }

    //Initializes UI variables
    private fun initializeVariables() {
        addButton = findViewById(R.id.addAnother)
        continueButton = findViewById(R.id.done)
        serviceProvidersListView = findViewById(R.id.providerListView)
        errorMessage = findViewById(R.id.errorMessage)
        providerBox = findViewById(R.id.providerBox)
        loadingPanel = findViewById(R.id.loadingPanel)
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
        addButton.setOnClickListener { startActivity(Intent(this@PreferredProvidersLogin, AddPreferredProvidersLogin::class.java)) }
    }

    //Handles when user is done and presses continue
    private fun onContinue() {
        continueButton.setOnClickListener { startActivity(Intent(this@PreferredProvidersLogin, Dashboard::class.java)) }
    }
}