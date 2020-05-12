package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.widget.*

//Page when user first logs in, they will see providers linked to their account and be able to add more
class PreferredProvidersLogin : PreferredProviders() {

    private lateinit var continueButton: Button
    private val url = "service-providers/"
    private val jsonRequest = JsonRequest(false, url, null, responseFunc, errorFunc, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferred_providers_login)
        centerTitleBar()
        initializeVariables()
        loadPreferredProviders(jsonRequest)
        onAddAnother()
        onContinue()
    }

    //Initializes UI variables
    private fun initializeVariables() {
        continueButton = findViewById(R.id.done)
        initializeCommonVariables()
    }

    //Handles when user wants to add another provider
    private fun onAddAnother() {
        addButton.setOnClickListener { startActivity(Intent(this@PreferredProvidersLogin, AddPreferredProvidersLogin::class.java)) }
    }

    //Handles when user is done and presses continue
    private fun onContinue() {
        continueButton.setOnClickListener { startActivity(Intent(this@PreferredProvidersLogin, Dashboard::class.java)) }
    }

    //Animates anything along with the collapsing and expanding nav bar
    override fun animateActivity(boolean: Boolean) {
        //Not implemented due to no navigation bar
    }
}