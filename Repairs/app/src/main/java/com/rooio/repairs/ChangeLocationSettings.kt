package com.rooio.repairs


import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_change_location_settings.*

class ChangeLocationSettings  : NavigationBar() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_location_settings)
        val addAnother = findViewById<TextView>(R.id.addAnother)
        val backArrow = findViewById<ImageView>(R.id.backArrow)

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

        supportActionBar!!.elevation = 0.0f

        createNavigationBar("settings")

        addAnother.setOnClickListener{
            startActivity(Intent( this, AddLocationSettings::class.java))
        }

        backArrow.setOnClickListener{
            startActivity(Intent( this, Settings::class.java))
        }

    }
    override fun animateActivity(boolean: Boolean)
    {

    }



}