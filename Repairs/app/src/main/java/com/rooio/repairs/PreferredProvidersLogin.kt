package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.arch.core.util.Function
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class PreferredProvidersLogin : RestApi() {
    var addButton: TextView? = null
    var doneButton: Button? = null
    var serviceProvidersListView: ListView? = null
    var error: TextView? = null
    private val preferredProviders = ArrayList<ServiceProviderData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferred_providers_login)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        supportActionBar!!.elevation = 0f
        addButton = findViewById<View>(R.id.addAnother) as TextView
        doneButton = findViewById<View>(R.id.Done) as Button
        serviceProvidersListView = findViewById<View>(R.id.list) as ListView
        error = findViewById<View>(R.id.error) as TextView
        loadPreferredProviders()
        onAddClick()
        onDoneClick()
    }

    fun loadPreferredProviders() {
        val url = "https://capstone.api.roopairs.com/v0/service-providers/"
        val responseFunc = Function<JSONArray, Void?> { jsonArray: JSONArray ->
            try {
                loadElements(jsonArray)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            null
        }
        val errorFunc = Function<String, Void?> { string: String? ->
            error!!.text = string
            null
        }
        requestGetJsonArray(url, responseFunc, errorFunc, true)
    }

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
                image = "http://rsroemani.com/rv2/wp-content/themes/rsroemani/images/no-user.jpg"
            } finally {
                val serviceProviderData = ServiceProviderData(name, image, id)
                preferredProviders.add(serviceProviderData)
            }
        }

        val customAdapter = PreferredProvidersCustomAdapter(this, preferredProviders)
        if (preferredProviders.size != 0) serviceProvidersListView!!.adapter = customAdapter
    }

    fun onAddClick() {
        addButton!!.setOnClickListener { startActivity(Intent(this@PreferredProvidersLogin, AddPreferredProvidersLogin::class.java)) }
    }

    fun onDoneClick() {
        doneButton!!.setOnClickListener { startActivity(Intent(this@PreferredProvidersLogin, Dashboard::class.java)) }
    }
}