package com.rooio.repairs

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.Request
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class PreferredProvidersSettings  : PreferredProviders() {

    private lateinit var spinner: Spinner
    private val url = "service-providers/"
    private val jsonRequest = JsonRequest(false, url, null, responseFunc, errorFunc, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferred_providers_settings)

        // gets rid of sound when the user clicks on the spinner when editing the equipment type
        onResume()

        initializeVariables()
        setNavigationBar()
        setActionBar()
        createNavigationBar(NavigationType.SETTINGS)
        setSpinner()
        loadPreferredProviders(jsonRequest)
        onAddAnother()
        goToPreferredProviderDetails()
        onPause()

    }

    //Initializes UI variables
    private fun initializeVariables() {
        initializeCommonVariables()
        spinner = findViewById(R.id.settings_spinner)
    }

    //Handles when user wants to add another provider
    private fun onAddAnother() {
        addButton.setOnClickListener { startActivity(Intent(this@PreferredProvidersSettings, AddPreferredProvidersSettings::class.java)) }
    }

    //Set the spinner for the title bar
    private fun setSpinner() {
        // setting on click listeners for the spinner items
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == "Service Location") {
                    val spinner: Spinner = findViewById(R.id.settings_spinner)
                    spinner.onItemSelectedListener = this
                    val mIntent = Intent(this@PreferredProvidersSettings, ChangeLocationSettings::class.java)
                    mIntent.putExtra("UniqueKey", position)
                    startActivity(mIntent)
                }
            }

        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
                this,
                R.array.settings_type,
                R.layout.spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    //Animates anything along with the collapsing and expanding nav bar
    override fun animateActivity(boolean: Boolean)
    {
        //Not implemented due to no animation needed
    }

}
