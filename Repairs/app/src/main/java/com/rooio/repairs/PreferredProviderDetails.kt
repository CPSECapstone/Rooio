package com.rooio.repairs

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.arch.core.util.Function
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject


class PreferredProviderDetails: NavigationBar() {

    private lateinit var message: TextView
    private lateinit var backButton: View
    private lateinit var removeButton: Button
    private lateinit var email: TextView
    private lateinit var skills: TextView
    private lateinit var licenseNumber: TextView
    private lateinit var overview: TextView
    private lateinit var phone: TextView
    private lateinit var name: TextView
    private lateinit var price: TextView
    private lateinit var logo: ImageView

    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferred_providers_details)

        //initializes variables that are used in loadElements()
        initializeVariables()
        setNavigationBar()
        setActionBar()
        createNavigationBar(NavigationType.SETTINGS)
        onBackClick()
        loadProvider()
        onRemoveClick()

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
        backButton = findViewById(R.id.back_button_details)
        removeButton = findViewById(R.id.removeProvider)

    }

    private fun onBackClick() {
        backButton.setOnClickListener{
            val intent = Intent(this@PreferredProviderDetails, PreferredProvidersSettings::class.java)
            startActivity(intent)
        }
    }

    private fun loadProvider(){
        val bundle: Bundle ?= intent.extras
        if (bundle!=null){
            val theId = bundle.getString("addedProvider")
            
            //set url to an empty string, as a private val
            url = "service-providers/" + theId.toString() + "/"
            requestJson(Request.Method.GET, JsonType.OBJECT, JsonRequest(false, url, null,
                    providerResponseFunc, providerErrorFunc, true))
        }
    }

    @JvmField
    var providerErrorFunc = Function<String, Void?> {error -> String
        message.text = error
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
        removeButton.setOnClickListener {
            requestJson(Request.Method.DELETE, JsonType.OBJECT, JsonRequest(false, url, null,
                    removeResponseFunc, removeErrorFunc, true))
        }
    }

    @JvmField
    var removeErrorFunc = Function<String, Void?> {error -> String
        message.text = error
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
        val image = try {
            response.get("logo") as String
        } catch (e: Exception) {
            // if there is no logo for the service provider
            ""
        }
        if(image.isNotEmpty())
            Picasso.with(applicationContext)
                .load(image)
                .into(logo)
        else{
            logo.setBackgroundResource(R.drawable.blank_border)
        }

        setElementTexts(overview, response, getString(R.string.overview_text))
        setElementTexts(email, response, getString(R.string.email))
        setElementTexts(skills, response, getString(R.string.skills))
        setElementTexts(licenseNumber, response, getString(R.string.contractor_license_number))
        setElementTexts(phone, response, getString(R.string.phone))
        setElementTexts(name, response, getString(R.string.name))

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
            val standardText = SpannableStringBuilder(getString(R.string.starting_cost_text))
            standardText.setSpan(ForegroundColorSpan(ContextCompat.getColor(applicationContext, R.color.colorAccent)), 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            element.text = standardText.insert(1, "$hoursText ")
        } catch (e: Exception) {
            element.text = getString(R.string.empty_field)
        }
    }


    override fun animateActivity(boolean: Boolean)
    {
        val amount = if (boolean) -190f else 0f
        val animation = ObjectAnimator.ofFloat(backButton, "translationX", amount)
        if (boolean) animation.duration = 1300 else animation.duration = 300
        animation.start()
    }

}