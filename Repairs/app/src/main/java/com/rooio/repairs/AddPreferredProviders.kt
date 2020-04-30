package com.rooio.repairs

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.arch.core.util.Function
import com.android.volley.Request
import org.json.JSONArray
import java.util.HashMap

//Page where shared functions are stored for preferred providers activities
abstract class AddPreferredProviders : NavigationBar() {

    enum class AddProviderType {
        LOGIN,
        SETTINGS;
    }

    private lateinit var addProvider: Button
    private lateinit var newProvider: EditText
    private lateinit var errorMessage: TextView
    private lateinit var loadingPanel: ProgressBar
    private val url: String = "service-providers/"
    private lateinit var type: AddProviderType

    //Initializes UI variables
    fun initializeCommonVariables(type: AddProviderType) {
        addProvider = findViewById(R.id.addProvider)
        newProvider = findViewById(R.id.newProvider)
        errorMessage = findViewById(R.id.errorMessage)
        loadingPanel = findViewById(R.id.loadingPanel)
        this.type = type
    }

    //Handles when a user adds a provider
    fun onAddProvider() {
        loadingPanel.visibility = View.GONE
        addProvider.visibility = View.VISIBLE
        addProvider.setOnClickListener {
            errorMessage.text = ""
            val phoneInput = newProvider.text.toString()
            val request = JsonRequest(false, url, null, checkResponseFunc, checkErrorFunc, true)
            checkPhoneNumber(phoneInput, request)
        }
    }

    //Checks if the phone number is already in the system, and then adds it if it is not
    @JvmField
    val checkResponseFunc = Function<Any, Void?> { response: Any ->
        val jsonArray = response as JSONArray
        val phoneInput = newProvider.text.toString()
        val params = HashMap<Any?, Any?>()
        params["phone"] = phoneInput
        val request = JsonRequest(false, url, params, providerResponseFunc, providerErrorFunc, true)
        val added = checkAlreadyAdded(phoneInput, jsonArray)
        addPreferredServiceProvider(added, request)
        null
    }

    //Handles an error if the API is unable to retrieve phone numbers for the account
    @JvmField
    val checkErrorFunc = Function<String, Void?> { error: String? ->
        loadingPanel.visibility = View.GONE
        addProvider.visibility = View.VISIBLE
        errorMessage.text = error
        null
    }

    //Checks if the phone number given is either empty or not a phone number, and then calls the API
    //if it is neither
    private fun checkPhoneNumber(phoneInput: String, request: JsonRequest) {
        if (phoneInput.isNotEmpty() && (phoneInput.length == 10 || phoneInput.length == 9)) {
            loadingPanel.visibility = View.VISIBLE
            addProvider.visibility = View.GONE
            requestJson(Request.Method.GET, JsonType.ARRAY, request)
        }
        else errorMessage.setText(R.string.error_phone)
    }

    //Takes in a boolean if it has been added to the system and makes a call to the API if it has not
    private fun addPreferredServiceProvider(added: Boolean, request: JsonRequest) {
        if (!added) {
            requestJson(Request.Method.POST, JsonType.ARRAY, request)
        } else {
            loadingPanel.visibility = View.GONE
            addProvider.visibility = View.VISIBLE
            errorMessage.setText(R.string.already_added_provider)
        }
    }


    //Returns an error if the provider has already been added
    @JvmField
    val providerErrorFunc = Function<String, Void?> { error -> String
        loadingPanel.visibility = View.GONE
        addProvider.visibility = View.VISIBLE
        if (error == "Does not exist.") {
            errorMessage.setText(R.string.error_provider)
        }
        else errorMessage.text = error
        null
    }

    //Goes through each phone number returned from the API and checks if it is equal to the current
    private fun checkAlreadyAdded(phoneInput: String, jsonArray: JSONArray): Boolean {
        for (i in 0 until jsonArray.length()) {
            val restaurant = jsonArray.getJSONObject(i)
            val phoneNum = restaurant["phone"] as String
            if (phoneNum.contains(phoneInput)) {
                return true
            }
        }
        return false
    }

    //Starts the preferred providers page after a provider has been added
    @JvmField
    val providerResponseFunc = Function<Any, Void?> { response: Any ->
        val jsonArray = response as JSONArray
        loadingPanel.visibility = View.GONE
        addProvider.visibility = View.VISIBLE
        if (jsonArray.length() == 0) errorMessage.setText(R.string.error_provider)
        else
        {
            if (type == AddProviderType.LOGIN) startActivity(Intent(this@AddPreferredProviders, PreferredProvidersLogin::class.java))
            else startActivity(Intent(this@AddPreferredProviders, PreferredProvidersSettings::class.java))
        }
        null
    }
}
