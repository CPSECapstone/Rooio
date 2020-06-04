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
import android.widget.ProgressBar
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
    private lateinit var loadingPanel: ProgressBar

    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferred_providers_details)

        onResume()
        //initializes variables that are used in loadElements()
        initializeVariables()
        setNavigationBar()
        setActionBar()
        createNavigationBar(NavigationType.SETTINGS)
        onBackClick()
        loadProvider()
        onRemoveClick()
        onPause()

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
        loadingPanel = findViewById(R.id.loadingPanel)
        message = findViewById(R.id.errorMessage)
    }

    private fun onBackClick() {
        backButton.setOnClickListener{
            val intent = Intent(this@PreferredProviderDetails, PreferredProvidersSettings::class.java)
            startActivity(intent)
        }
    }

    private fun loadProvider(){
        loadingPanel.visibility = View.VISIBLE
        val theId = intent.getStringExtra("addedProvider")
        val string = if (theId == null) "" else theId.toString()
        //set url to an empty string, as a private val
        url = "service-providers/" + string + "/"
        requestJson(Request.Method.GET, JsonType.OBJECT, JsonRequest(false, url, null,
                providerResponseFunc, providerErrorFunc, true))

    }

    @JvmField
    var providerErrorFunc = Function<String, Void?> {error -> String
        loadingPanel.visibility = View.GONE
        message.text = error
        null
    }

    @JvmField
    val providerResponseFunc = Function<Any, Void?> { response : Any ->
        loadingPanel.visibility = View.GONE
        val jsonObject = response as JSONObject
        loadElements(jsonObject)
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
        val provider = ProviderData(response)
        val image = provider.logo
        if(image.isNotEmpty())
            Picasso.with(applicationContext)
                .load(image)
                .into(logo)
        else{
           logo.visibility = View.GONE
        }

        setElementTexts(overview, provider.bio)
        setElementTexts(email, provider.email)
        setElementTexts(skills, setSkills(provider.skills))
        setElementTexts(licenseNumber, provider.contractor_license_number)
        setElementTexts(phone, provider.phone)
        setElementTexts(name, provider.name)

        setPriceElement(price, response, "starting_hourly_rate")
    }

    private fun setElementTexts(element: TextView, elementValue: String){
        if(elementValue.isEmpty() || elementValue == "null")
            element.text = "--"
        else
            element.text = elementValue
    }

    //Sets the skills text for the item by parsing an array of skills
    private fun setSkills(skills: ArrayList<String>): String {
        var text = ""
        val comma = ", "
        for (skill in skills) {
            text += "$skill$comma"
        }
        return text.subSequence(0, text.length - 2) as String
    }

    private fun setPriceElement(element: TextView, response: JSONObject, elementName: String){
        val hoursText = getString((R.string.details_price_exception_message),response.get(elementName) as String)
        val standardText = SpannableStringBuilder(getString(R.string.starting_cost_text))
        standardText.setSpan(ForegroundColorSpan(ContextCompat.getColor(applicationContext, R.color.colorAccent)), 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        element.text = standardText.insert(1, "$hoursText ")
    }


    override fun animateActivity(boolean: Boolean)
    {
        val amount = if (boolean) -190f else 0f
        val animation = ObjectAnimator.ofFloat(backButton, "translationX", amount)
        if (boolean) animation.duration = 1300 else animation.duration = 300
        animation.start()
    }

}