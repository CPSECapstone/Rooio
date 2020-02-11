package com.rooio.repairs

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_preferred_providers_details.*
import org.json.JSONArray
import org.json.JSONException
import java.net.URL
import java.util.ArrayList
import org.json.JSONObject as JSONObject1

class PreferredProviderDetails: NavigationBar() {

    lateinit var error: TextView
    lateinit var backButton: ImageView
    lateinit var removeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferred_providers_details)

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


        val bundle: Bundle ?= intent.extras
            if (bundle!=null){
                val theId = bundle.getString("addedProvider")
                val url = "https://capstone.api.roopairs.com/v0/service-providers/" + theId.toString() + "/"

                val responseFunc = { jsonObject : JSONObject1 ->
                    try {
                        loadElements(jsonObject)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    null
                }

                val errorFunc = { string : String ->
                    error.setText(string)
                    null
                }

                requestGetJsonObj(url, responseFunc, errorFunc, true)
                removeButton = findViewById<Button>(R.id.removeProvider) as Button
                removeButton.setOnClickListener {
                    val intent = Intent(this@PreferredProviderDetails, PreferredProvidersSettings::class.java)
                    //deletePostJsonObj(JsonRequest(false, url, ))
                    startActivity(intent)
                }
                //removeProvider(url, responseFunc, errorFunc, true)
            }
        backButton = findViewById<View>(R.id.back_button_details) as ImageView
        onBackClick()

    }
//    fun loadElements(response: JSONObject1) {
//        removeButton = findViewById<Button>(R.id.removeProvider) as Button
//        removeButton.setOnClickListener{
//            val intent = Intent(this@PreferredProviderDetails, PreferredProvidersSettings::class.java)
//            response.remove(id)
//            startActivity(intent)
//        }
    @Throws(JSONException::class)
    fun loadElements(response: JSONObject1) {
        val overview = findViewById<TextView>(R.id.info_overview)
        val email = findViewById<TextView>(R.id.info_email)
        val skills = findViewById<TextView>(R.id.info_skills)
        val licenseNumber = findViewById<TextView>(R.id.info_license_number)
        val phone = findViewById<TextView>(R.id.info_phone)
        val logo = findViewById<ImageView>(R.id.logo)
        val name = findViewById<TextView>(R.id.name)
        val price = findViewById<TextView>(R.id.price)
        try {
            //need to figure out how to get the url of the image
           val inputStream = URL(response.get("logo") as String).openStream()
            logo.setImageBitmap(BitmapFactory.decodeStream(inputStream))

        } catch (e: Exception) {
            // if there is no logo for the service provider
            val inputStream2 = URL("http://rsroemani.com/rv2/wp-content/themes/rsroemani/images/no-user.jpg").openStream()
            logo.setImageBitmap(BitmapFactory.decodeStream(inputStream2))

        }
        setElementTexts(overview, response,"overview", "overview")
        setElementTexts(email, response, "email", "email")
        setElementTexts(skills, response, "skills", "skills")
        setElementTexts(licenseNumber, response, "contractor_license_number", "license number")
        setElementTexts(phone, response, "phone", "phone number")
        setElementTexts(name, response, "name", "name")
        setPriceElement(price, response, "starting_hourly_rate", "")


    }

    private fun setElementTexts(element: TextView, response: JSONObject1, elementName: String, name: String){
        try {
            element.text = response.get(elementName) as String
        } catch (e: Exception) {
            // if there is no logo for the service provider
            element.text = "No $name provided."
        }
    }

    private fun setPriceElement(element: TextView, response: JSONObject1, elementName: String, name: String){
        try {
            element.text = "$" + response.get(elementName) as String + "/hour"
        } catch (e: Exception) {
            // if there is no logo for the service provider
            element.text = "No $name provided."
        }
    }


    override fun animateActivity(boolean: Boolean)
    {
    }
    private fun onBackClick() {
        backButton.setOnClickListener{
            val intent = Intent(this@PreferredProviderDetails, PreferredProvidersSettings::class.java)
            startActivity(intent);
        }    }

}