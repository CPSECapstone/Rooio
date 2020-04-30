package com.rooio.repairs

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.arch.core.util.Function
import com.android.volley.Request
import org.json.JSONArray
import org.json.JSONException
import java.util.*


//Page where user can add a preferred provider from settings
class AddPreferredProvidersSettings  : AddPreferredProviders(){

    private lateinit var backButton: ImageView
    private lateinit var viewGroup: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_preferred_providers_setting)

        // gets rid of sound when the user clicks on the spinner when editing the equipment type
        onResume()
        initializeVariables()
        setNavigationBar()
        setActionBar()
        createNavigationBar(NavigationType.SETTINGS)
        onAddProvider()
        onBack()
        onPause()
    }

    //Initializes UI variables
    private fun initializeVariables() {
        viewGroup = findViewById(R.id.background)
        //Navigation bar collapse/expand
        backButton = viewGroup.findViewById(R.id.backButton)
        initializeCommonVariables(AddProviderType.SETTINGS)
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
            startActivity(Intent(this@AddPreferredProvidersSettings, PreferredProvidersSettings::class.java))
        }
    }

}