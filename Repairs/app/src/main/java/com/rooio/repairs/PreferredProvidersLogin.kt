package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class PreferredProvidersLogin : RestApi() {
    private var addButton: TextView? = null
    private var doneButton: Button? = null
    private var serviceProvidersListView: ListView? = null
    private var error: TextView? = null
    private var providerBox: ConstraintLayout? = null
    private val preferredProviders = ArrayList<ServiceProviderData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferred_providers_login)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        supportActionBar!!.elevation = 0f
        addButton = findViewById<View>(R.id.addAnother) as TextView
        doneButton = findViewById<View>(R.id.done) as Button
        serviceProvidersListView = findViewById<View>(R.id.list) as ListView
        error = findViewById<View>(R.id.error) as TextView
        providerBox = findViewById<View>(R.id.providerBox) as ConstraintLayout
        loadPreferredProviders()
        onAddClick()
        onDoneClick()
    }

    private fun loadPreferredProviders() {
        val url = "https://capstone.api.roopairs.com/v0/service-providers/"
        val responseFunc = Function<Any, Void?> { response : Any ->
            val jsonArray = response as JSONArray
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
        val request = JsonRequest(false, url, HashMap(), responseFunc, errorFunc, true)
        requestGetJsonArray(request)
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
                image = ""
            } finally {
                val serviceProviderData = ServiceProviderData(name, image, id)
                preferredProviders.add(serviceProviderData)
                if (i > 0) {
                    val params = providerBox!!.layoutParams
                    params.height += 105
                    providerBox!!.layoutParams = params
                    val size = serviceProvidersListView!!.layoutParams
                    size.height += 105
                    serviceProvidersListView!!.layoutParams = size
                }
            }
        }

        val customAdapter = PreferredProvidersCustomAdapter(this, preferredProviders)
        if (preferredProviders.size != 0) serviceProvidersListView!!.adapter = customAdapter
    }

    private fun onAddClick() {
        addButton!!.setOnClickListener { startActivity(Intent(this@PreferredProvidersLogin, AddPreferredProvidersLogin::class.java)) }
    }

    private fun onDoneClick() {
        doneButton!!.setOnClickListener { startActivity(Intent(this@PreferredProvidersLogin, Dashboard::class.java)) }
    }
}