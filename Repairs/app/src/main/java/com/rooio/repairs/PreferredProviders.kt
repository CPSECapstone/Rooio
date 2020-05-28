package com.rooio.repairs

import android.content.Intent
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import org.json.JSONArray
import org.json.JSONException
import androidx.arch.core.util.Function
import java.util.ArrayList

abstract class PreferredProviders : NavigationBar() {

    lateinit var addButton: TextView
    private lateinit var serviceProvidersListView: ListView
    private lateinit var errorMessage: TextView
    private lateinit var providerBox: ConstraintLayout
    private lateinit var loadingPanel: ProgressBar
    private val preferredProviders = ArrayList<ServiceProviderData>()

    fun initializeCommonVariables() {
        addButton = findViewById(R.id.addAnother)
        serviceProvidersListView = findViewById(R.id.providerListView)
        errorMessage = findViewById(R.id.errorMessage)
        providerBox = findViewById(R.id.providerBox)
        loadingPanel = findViewById(R.id.loadingPanel)
    }


    // Initially loads the current providers by making a call to the API
    fun loadPreferredProviders(request: JsonRequest) {
        loadingPanel.visibility = View.VISIBLE
        requestJson(Request.Method.GET, JsonType.ARRAY, request)
    }

    //Loads all the elements from the JSON array or displays an error
    @JvmField
    val responseFunc = Function<Any, Void?> { response : Any ->
        loadingPanel.visibility = View.GONE
        val jsonArray = response as JSONArray
        loadElements(jsonArray)
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
            val id = restaurant["id"] as Int
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

    //goes to the PreferredProviderDetails page
    fun goToPreferredProviderDetails() {
        serviceProvidersListView.setOnItemClickListener{ _, _, position, _ ->
            val id = preferredProviders[position].id.toString()
            val listIntent = Intent(this@PreferredProviders, PreferredProviderDetails::class.java)
            listIntent.putExtra("addedProvider", id)
            startActivity(listIntent)
        }
    }
}