package com.rooio.repairs


import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*

//Page where user can select a new location in the settings
class LocationSettings  : Location()  {

    private lateinit var backButton: ImageView
    private lateinit var viewGroup: ViewGroup
    private val url = "service-locations/"
    private val jsonRequest = JsonRequest(false, url, null, responseFunc, errorFunc, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_location_settings)

        initializeVariables()
        setNavigationBar()
        setActionBar()
        createNavigationBar(NavigationType.SETTINGS)
        loadLocations(jsonRequest)
        onAddAnother()
        onBack()
    }

    //Initializes UI variables
    private fun initializeVariables() {
        viewGroup = findViewById(R.id.background)
        //Navigation bar collapse/expand
        backButton = viewGroup.findViewById(R.id.backButton)
        initializeCommonVariables(LocationType.SETTINGS)
    }

    override fun animateActivity(boolean: Boolean)
    {
        val amount = if (boolean) -190f else 0f
        val animation = ObjectAnimator.ofFloat(backButton, "translationX", amount)
        if (boolean) animation.duration = 1300 else animation.duration = 300
        animation.start()
    }

    //Sends the user to the Jobs page
    private fun onBack() {
        backButton.setOnClickListener{
            startActivity(Intent(this@LocationSettings, LocationSettings::class.java))
        }
    }

}