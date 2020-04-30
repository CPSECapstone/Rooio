package com.rooio.repairs


import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*

//Page for adding locations page that a user can find under settings
class AddLocationSettings  : AddLocation() {

    private lateinit var backButton: ImageView
    private lateinit var viewGroup: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location_settings)

        // gets rid of sound when the user clicks on the spinner when editing the equipment type
        onResume()

        initializeVariables()
        setNavigationBar()
        setActionBar()
        createNavigationBar(NavigationType.SETTINGS)
        onAddLocation()
        onBack()
        onPause()
    }

    //Initializes UI variables
    private fun initializeVariables() {
        viewGroup = findViewById(R.id.background)
        backButton = viewGroup.findViewById(R.id.backButton)
        initializeCommonVariables(AddLocationType.SETTINGS)
    }

    //Animates the main page content when the navigation bar collapses/expands
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
            startActivity(Intent(this@AddLocationSettings, ChangeLocationSettings::class.java))
        }
    }
}