package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.arch.core.util.Function
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class AddPreferredProvidersLogin : RestApi() {
    private var addButton: Button? = null
    private var backButton: Button? = null
    private var newProvider: EditText? = null
    private var error: TextView? = null
    var phoneInput: String? = null
    var addedProviderRet: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_preferred_providers_login)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        supportActionBar!!.elevation = 0f

        addButton = findViewById<View>(R.id.add_provider) as Button
        backButton = findViewById<View>(R.id.cancel) as Button
        newProvider = findViewById<View>(R.id.new_phone) as EditText
        error = findViewById<View>(R.id.error) as TextView

        onAddClick()
        onBackClick()
    }

    private fun onBackClick() {
        backButton!!.setOnClickListener { startActivity(Intent(this@AddPreferredProvidersLogin, PreferredProvidersLogin::class.java)) }
    }

    private fun onAddClick() {
        addButton!!.setOnClickListener {
            error!!.text = ""
            phoneInput = newProvider!!.text.toString()
            if (!phoneInput!!.isEmpty() && phoneInput!!.length >= 10) checkIfAlreadyAdded(phoneInput!!) else error!!.text = "Please enter a valid phone number."
        }
    }

    fun addPreferredServiceProvider(phoneInput: String) {
        val url = "https://capstone.api.roopairs.com/v0/service-providers/"
        val params = HashMap<String, Any>()
        params["phone"] = phoneInput
        val responseFunc = Function<JSONArray, Void?> { jsonArray: JSONArray? ->
            startActivity(Intent(this@AddPreferredProvidersLogin, PreferredProvidersLogin::class.java))
            null
        }
        val errorFunc = Function<String, Void?> { string: String ->
            error!!.text = "This service provider $string Please use another phone number."
            null
        }
        requestPostJsonArray(url, params, responseFunc, errorFunc, true)
    }

    private fun checkIfAlreadyAdded(phoneInput: String) {
        val url = "https://capstone.api.roopairs.com/v0/service-providers/"
        val responseFunc = Function<JSONArray, Void?> { jsonArray: JSONArray ->
            try {
                var added = false
                for (i in 0 until jsonArray.length()) {
                    val restaurant = jsonArray.getJSONObject(i)
                    val addedProvider = restaurant["name"] as String
                    val phoneNum = restaurant["phone"] as String
                    if (phoneNum.contains(phoneInput)) {
                        addedProviderRet = addedProvider
                        added = true
                    }
                }
                if (!added) {
                    addPreferredServiceProvider(phoneInput)
                } else {
                    error!!.text = "You've already added $addedProviderRet."
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            null
        }
        val errorFunc = Function<String, Void?> { string: String? -> null }
        requestGetJsonArray(url, responseFunc, errorFunc, true)
    }
}