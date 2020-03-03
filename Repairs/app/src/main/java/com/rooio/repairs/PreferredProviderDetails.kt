package com.rooio.repairs

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import androidx.arch.core.util.Function


class PreferredProviderDetails: NavigationBar() {

    private lateinit var error: TextView
    private lateinit var backButton: View
    private lateinit var removeButton: Button
    private lateinit var url: String
    private lateinit var email: TextView
    private lateinit var skills: TextView
    private lateinit var licenseNumber: TextView
    private lateinit var overview: TextView
    private lateinit var phone: TextView
    private lateinit var name: TextView
    private lateinit var price: TextView
    private lateinit var logo: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferred_providers_details)

        //initializes variables that are used in loadElements()
        initializeVariables()
        setNavigationBar()
        setActionBar()
        createNavigationBar("settings")
        backButton = findViewById<View>(R.id.back_button_details)
        removeButton = findViewById(R.id.removeProvider)

        onBackClick()
        loadProvider()
        onRemoveClick()

    }

    private fun loadProvider(){
        val bundle: Bundle ?= intent.extras
        if (bundle!=null){
            val theId = bundle.getString("addedProvider")
            url = "https://capstone.api.roopairs.com/v0/service-providers/" + theId.toString() + "/"
            val request = JsonRequest(false, url, HashMap(), providerResponseFunc, providerErrorFunc, true)
            requestGetJsonObj(request)
        }
    }

    @JvmField
    var providerErrorFunc = Function<String, Void?> {
        error.setText(R.string.error_login)
        null
    }

    @JvmField
    val providerResponseFunc = Function<Any, Void?> { response : Any ->
        val jsonObject = response as JSONObject
        try {
            loadElements(jsonObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        null
    }

    private fun onRemoveClick() {
        val params = java.util.HashMap<Any?, Any?>()
        val request = JsonRequest(false, url, params, removeResponseFunc, removeErrorFunc, true)
        removeButton.setOnClickListener {
            requestDeleteJsonObj(request)
        }
    }

    @JvmField
    var removeErrorFunc = Function<String, Void?> {
        error.setText(R.string.error_login)
        null
    }

    @JvmField
    val removeResponseFunc = Function<Any, Void?> {
        try {
            startActivity(Intent (this@PreferredProviderDetails, PreferredProvidersSettings::class.java ))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        null
    }

    @Throws(JSONException::class)
    fun loadElements(response: JSONObject) {
        var image : String
        try {
            image = response.get("logo") as String
        } catch (e: Exception) {
            // if there is no logo for the service provider
            image =""
        }
        if(image.isNotEmpty())
            Picasso.with(applicationContext)
                .load(image)
                .into(logo)

        setElementTexts(overview, response,"overview")
        setElementTexts(email, response, "email")
        setElementTexts(skills, response, "skills")
        setElementTexts(licenseNumber, response, "contractor_license_number")
        setElementTexts(phone, response, "phone")
        setElementTexts(name, response, "name")

        setPriceElement(price, response, "starting_hourly_rate")

    }

    private fun setElementTexts(element: TextView, response: JSONObject, elementName: String){
        try {
            val jsonStr = response.get(elementName) as String
            if(jsonStr.isEmpty() || jsonStr == "null")
                element.text = "--"
            else
                element.text = jsonStr

        }
        catch (e: Exception) {
            element.text = "--"
        }
    }

    private fun setPriceElement(element: TextView, response: JSONObject, elementName: String){
        try {
            val hoursText = getString((R.string.details_price_exception_message),response.get(elementName) as String)
            val standardText = SpannableStringBuilder(" starting cost")
            standardText.setSpan(ForegroundColorSpan(Color.parseColor("#00CA8F")), 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            element.text = standardText.insert(1, "$hoursText ")
        } catch (e: Exception) {
            element.text = "--"
        }
    }
    //Initializes UI variables
    private fun initializeVariables() {
        overview = findViewById(R.id.info_overview)
        email = findViewById(R.id.info_email)
        skills = findViewById(R.id.info_skills)
        licenseNumber = findViewById(R.id.info_license_number)
        phone = findViewById(R.id.info_phone)
        logo = findViewById(R.id.logo)
        name = findViewById(R.id.name)
        price = findViewById(R.id.price)
    }
    // Sets the navigation bar onto the page
    private fun setNavigationBar() {
        //sets the navigation bar onto the page
        val navInflater = layoutInflater
        val tmpView = navInflater.inflate(R.layout.activity_navigation_bar, null)

        window.addContentView(tmpView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    // Sets the action bar onto the page
    private fun setActionBar() {
        //sets the action bar onto the page
        val actionbarInflater = layoutInflater
        val actionbarView = actionbarInflater.inflate(R.layout.action_bar, null)
        window.addContentView(actionbarView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        supportActionBar!!.elevation = 0.0f
    }



    override fun animateActivity(boolean: Boolean)
    {
    }
    private fun onBackClick() {
        backButton.setOnClickListener{
            val intent = Intent(this@PreferredProviderDetails, PreferredProvidersSettings::class.java)
            startActivity(intent)
        }
    }

}