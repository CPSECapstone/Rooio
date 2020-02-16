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

class PreferredProvidersSettings  : NavigationBar() {

    internal lateinit var serviceProvidersListView: ListView
    lateinit var addButton: TextView
    lateinit var error: TextView
    internal var preferredProviders = ArrayList<ServiceProviderData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferred_providers_setting)

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

        serviceProvidersListView = findViewById<View>(R.id.list) as ListView
        addButton = findViewById<View>(R.id.addAnother) as TextView
        error = findViewById<View>(R.id.error) as TextView


        val spinner: Spinner = findViewById(R.id.settings_spinner)

        // setting on click listeners for the spinner items
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if(selectedItem == "Service Location") {
                      val spinner: Spinner = findViewById(R.id.settings_spinner)
                      spinner.onItemSelectedListener = this
                      val mIntent = Intent(this@PreferredProvidersSettings, LocationSettings::class.java)
                      mIntent.putExtra("UniqueKey", position)
                      startActivity(mIntent)
                }
            }

        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                this,
                R.array.settings_type,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        loadPreferredProviders()

        onAddClick();

        serviceProvidersListView.setOnItemClickListener{ parent, view, position, id ->
             addDetails2(position)
    }

    }

    private fun addDetails2(position: Int) {
        val id = preferredProviders[position].id.toString()
        val listIntent = Intent(this@PreferredProvidersSettings, PreferredProviderDetails::class.java)
        listIntent.putExtra("addedProvider", id)
        startActivity(listIntent)

    }

    fun loadPreferredProviders() {
        val url = "https://capstone.api.roopairs.com/v0/service-providers/"

        val responseFunc = { jsonArray : JSONArray ->
            try {
                loadElements(jsonArray)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            null
        }

        val errorFunc = { string : String ->
            error.setText(string)
            null
        }

        requestGetJsonArray(url, responseFunc, errorFunc, true)
    }

    @Throws(JSONException::class)
    fun loadElements(response: JSONArray) {
        preferredProviders.clear()
        for (i in 0 until response.length()) {
            val restaurant = response.getJSONObject(i)
            val name = restaurant.get("name") as String
            val id = restaurant.get("id") as Int
            var image = ""
            try {
                image = restaurant.get("logo") as String
            } catch (e: Exception) {
                // if there is no logo for the service provider
                image = ""
            } finally {
                val serviceProviderData = ServiceProviderData(name, image, id)
                preferredProviders.add(serviceProviderData)
            }
        }

        val customAdapter = PreferredProvidersCustomAdapter(this, preferredProviders)
        if(preferredProviders.isEmpty().not()){
            serviceProvidersListView.setAdapter(customAdapter)
        }
    }

    private fun onAddClick() {
        addButton.setOnClickListener{
            val intent = Intent(this@PreferredProvidersSettings, AddPreferredProvidersSettings::class.java)
            startActivity(intent);
        }
    }


    override fun animateActivity(boolean: Boolean)
    {

    }



}
