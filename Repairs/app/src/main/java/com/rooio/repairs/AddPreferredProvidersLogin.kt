package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.arch.core.util.Function
import org.json.JSONArray
import org.json.JSONException
import java.util.*

//Page that only shows up for first time login that allows user to add preferred providers
class AddPreferredProvidersLogin : RestApi() {

    private lateinit var addProvider: Button
    private lateinit var cancelButton: Button
    private lateinit var newProvider: EditText
    private lateinit var errorMessage: TextView
    private lateinit var phoneInput: String
    private lateinit var addedProviderRet: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_preferred_providers_login)

        centerTitleBar()
        initializeVariables()
        onAddProvider()
        onCancel()
    }

    //Initializes UI variables
    private fun initializeVariables() {
        addProvider = findViewById(R.id.addProvider)
        cancelButton = findViewById(R.id.cancel)
        newProvider = findViewById(R.id.newProvider)
        errorMessage = findViewById(R.id.errorMessage)
    }

    //Centers "Repairs" title
    private fun centerTitleBar() {
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        supportActionBar!!.elevation = 0f
    }

    //Sends user back to provider list view when the cancel button is clicked
    private fun onCancel() {
        cancelButton.setOnClickListener { startActivity(Intent(this@AddPreferredProvidersLogin, PreferredProvidersLogin::class.java)) }
    }

    //Handles when a user adds a provider
    private fun onAddProvider() {
        addProvider.setOnClickListener {
            errorMessage.text = ""
            phoneInput = newProvider.text.toString()
            if (phoneInput.isNotEmpty() && (phoneInput.length == 10 || phoneInput.length == 9))
                checkIfAlreadyAdded(phoneInput)
            else errorMessage.text = "Please enter a valid phone number."
        }
    }

    private fun addPreferredServiceProvider(phoneInput: String) {
        val url = "https://capstone.api.roopairs.com/v0/service-providers/"
        val params = HashMap<Any?, Any?>()
        params["phone"] = phoneInput
        val responseFunc = Function<Any, Void?> {
            startActivity(Intent(this@AddPreferredProvidersLogin, PreferredProvidersLogin::class.java))
            null
        }
        val errorFunc = Function<String, Void?> { string: String ->
            errorMessage.text = "This service provider $string Please use another phone number."
            null
        }
        val request = JsonRequest(false, url, params, responseFunc, errorFunc, true)
        requestPostJsonArray(request)
    }

    private fun checkIfAlreadyAdded(phoneInput: String) {
        val url = "https://capstone.api.roopairs.com/v0/service-providers/"
        val responseFunc = Function<Any, Void?> { response: Any ->
            try {
                val jsonArray = response as JSONArray
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
                    errorMessage.text = "You've already added $addedProviderRet."
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            null
        }
        val errorFunc = Function<String, Void?> { string: String? -> null }
        val request = JsonRequest(false, url, HashMap(), responseFunc, errorFunc, true)
        requestGetJsonArray(request)
    }
}