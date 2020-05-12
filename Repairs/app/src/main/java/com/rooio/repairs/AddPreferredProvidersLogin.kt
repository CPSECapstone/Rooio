package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.widget.*

//Page that only shows up for first time login that allows user to add preferred providers
class AddPreferredProvidersLogin : AddPreferredProviders() {

    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_preferred_providers_login)

        // gets rid of sound when the user clicks on the spinner when editing the equipment type
        onResume()
        centerTitleBar()
        initializeVariables()
        onAddProvider()
        onCancel()
        onPause()
    }

    //Initializes UI variables
    private fun initializeVariables() {
        cancelButton = findViewById(R.id.cancel)
        initializeCommonVariables(AddProviderType.LOGIN)
    }

    //Sends user back to provider list view when the cancel button is clicked
    private fun onCancel() {
        cancelButton.setOnClickListener { startActivity(Intent(this@AddPreferredProvidersLogin, PreferredProvidersLogin::class.java)) }
    }

    override fun animateActivity(boolean: Boolean) {
        //Not implemented bc no navigation bar
    }
}