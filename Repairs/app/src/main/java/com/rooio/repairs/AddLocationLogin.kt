package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.widget.*

//Page where user is able to add a location when initially logging in
class AddLocationLogin : AddLocation(){

    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location_login)

        // gets rid of sound when the user clicks on the spinner when editing the equipment type
        onResume()
        centerTitleBar()
        initializeVariables()
        onAddLocation()
        onCancel()
        onPause()
    }

    private fun initializeVariables() {
        cancelButton = findViewById(R.id.cancel)
        initializeCommonVariables(AddLocationType.LOGIN)
    }

    //Sends user back to locations list view
    private fun onCancel() {
        cancelButton.setOnClickListener { startActivity(Intent(this@AddLocationLogin, LocationLogin::class.java)) }
    }

    override fun animateActivity(boolean: Boolean) {
        //does not have navigation bar
    }
}