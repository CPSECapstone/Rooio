package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList
import java.util.HashMap

class AddPreferredProvidersSetting  : NavigationBar() {

    lateinit var addButton: TextView
    lateinit var backButton: ImageView
    private var newProvider: EditText? = null
    lateinit var error: TextView

    internal lateinit var phoneInput: String
    internal lateinit var addedProviderRet: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_preferred_providers_setting)

        //sets the navigation bar onto the page
        val navInflater = layoutInflater
        val tmpView = navInflater.inflate(R.layout.activity_navigation_bar, null)

        window.addContentView(tmpView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        //sets the action bar onto the page
        val actionbarInflater = layoutInflater
        val actionBarView = actionbarInflater.inflate(R.layout.action_bar, null)
        window.addContentView(actionBarView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        createNavigationBar("settings")

        addButton = findViewById<View>(R.id.add_provider) as TextView
        backButton = findViewById<View>(R.id.back_button) as ImageView
        newProvider = findViewById<View>(R.id.new_phone) as EditText
        error = findViewById<View>(R.id.error) as TextView

        onAddClick();
        onBackClick();
    }

    override fun animateActivity(boolean: Boolean)
    {

    }

    private fun onBackClick() {
        backButton.setOnClickListener{
            val intent = Intent(this@AddPreferredProvidersSetting, PreferredProvidersSetting::class.java)
            startActivity(intent);
        }    }

    private fun onAddClick() {
        addButton.setOnClickListener{
            error.text = ""
            phoneInput = newProvider?.getText().toString()
            if (!phoneInput.isEmpty() && phoneInput.length >= 10) {
                checkIfAlreadyAdded(phoneInput)
            }
            else
                error.text = "Please enter a valid phone number."
        }

        }

    fun addPreferredServiceProvider(phoneInput: String) {
        val url = "https://capstone.api.roopairs.com/v0/service-providers/"

        val params = HashMap<String, Any>()
        params["phone"] = phoneInput

        val responseFunc = { jsonArray : JSONArray ->
            startActivity(Intent(this@AddPreferredProvidersSetting, PreferredProvidersLogin::class.java))
            null
        }

        val errorFunc = { string : String ->
            error.text = "This service provider $string. Please use another phone number."
            null
        }

        requestPostJsonArray(url, params, responseFunc, errorFunc, true)
    }

    private fun checkIfAlreadyAdded(phoneInput: String) {

        val url = "https://capstone.api.roopairs.com/v0/service-providers/"

        val responseFunc = { jsonArray : JSONArray ->
            try {
                var added: Boolean? = false

                for (i in 0 until jsonArray.length()) {
                    val restaurant = jsonArray.getJSONObject(i)
                    val addedProvider = restaurant.get("name") as String
                    val phoneNum = restaurant.get("phone") as String

                    if (phoneNum.contains(phoneInput)) {
                        addedProviderRet = addedProvider
                        added = true
                    }
                }

                if ((added?.not())!!) {
                    addPreferredServiceProvider(phoneInput)
                } else {
                    error.text = "You've already added $addedProviderRet."
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }

            null
        }

        val errorFunc = { string : String -> null }

        requestGetJsonArray(url, responseFunc, errorFunc, true)
    }

}