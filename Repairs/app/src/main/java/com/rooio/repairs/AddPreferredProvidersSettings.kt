package com.rooio.repairs

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.arch.core.util.Function
import com.android.volley.Request
import org.json.JSONArray
import org.json.JSONException
import java.util.*


//Page where user can add a preferred provider from settings
class AddPreferredProvidersSettings  : NavigationBar() {

    private lateinit var addProvider: Button
    private lateinit var newProvider: EditText
    private lateinit var errorMessage: TextView
    private lateinit var loadingPanel: ProgressBar
    private lateinit var backButton: ImageView
    private lateinit var viewGroup: ViewGroup
    private val url: String = "service-providers/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_preferred_providers_setting)

        initializeVariables()
        setNavigationBar()
        setActionBar()
        createNavigationBar(NavigationType.SETTINGS)
        onAddProvider()
        onBack()
    }

    //Initializes UI variables
    private fun initializeVariables() {
        addProvider = findViewById(R.id.addProvider)
        newProvider = findViewById(R.id.newProvider)
        errorMessage = findViewById(R.id.errorMessage)
        loadingPanel = findViewById(R.id.loadingPanel)
        viewGroup = findViewById(R.id.background)
        //Navigation bar collapse/expand
        backButton = viewGroup.findViewById(R.id.backButton)
    }

    //Handles when a user adds a provider
    private fun onAddProvider() {
        loadingPanel.visibility = View.GONE
        addProvider.visibility = View.VISIBLE
        addProvider.setOnClickListener {
            errorMessage.text = ""
            val phoneInput = newProvider.text.toString()
            val request = JsonRequest(false, url, null, checkResponseFunc, checkErrorFunc, true)
            checkPhoneNumber(phoneInput, request)
        }
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

    //Starts the preferred providers page after a provider has been added
    @JvmField
    val providerResponseFunc = Function<Any, Void?> {
        loadingPanel.visibility = View.GONE
        addProvider.visibility = View.VISIBLE
        startActivity(Intent(this@AddPreferredProvidersSettings, PreferredProvidersSettings::class.java))
        null
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

    //Handles an error if the API is unable to retrieve phone numbers for the account
    @JvmField
    val checkErrorFunc = Function<String, Void?> { error: String? ->
        loadingPanel.visibility = View.GONE
        addProvider.visibility = View.VISIBLE
        errorMessage.text = error
        null
    }

    //Checks if the phone number is already in the system, and then adds it if it is not
    @JvmField
    val checkResponseFunc = Function<Any, Void?> { response: Any ->
        try {
            val jsonArray = response as JSONArray
            val phoneInput = newProvider.text.toString()
            val params = HashMap<Any?, Any?>()
            params["phone"] = phoneInput
            val request = JsonRequest(false, url, params, providerResponseFunc, providerErrorFunc, true)
            addPreferredServiceProvider(checkAlreadyAdded(phoneInput, jsonArray), request)
        } catch (e: JSONException) {
            errorMessage.setText(R.string.error_server)
        }
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

    //Animates the main page content when the navigation bar collapses/expands
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
            startActivity(Intent(this@AddPreferredProvidersSettings, PreferredProvidersSettings::class.java))
        }
    }

}