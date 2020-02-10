package com.rooio.repairs


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.transition.TransitionManager

import android.graphics.Color
import android.view.View.VISIBLE


abstract class NavigationBar : RestApi() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_bar)
    }



    fun createNavigationBar(string: String) {
        var visible = true

        //transitionsContainer initializes the container and stores all transition objects
        val transitionsContainer = findViewById<ViewGroup>(R.id.navConstraint)

        //adding objects into the transitionsContainer
        val navBar = transitionsContainer.findViewById<ViewGroup>(R.id.navigationView)
        val collapseText = transitionsContainer.findViewById<TextView>(R.id.collapse_text)
        val dashboardText = transitionsContainer.findViewById<TextView>(R.id.dashboard_text)
        val jobsText = transitionsContainer.findViewById<TextView>(R.id.jobs_text)
        val equipmentText = transitionsContainer.findViewById<TextView>(R.id.equipment_text)
        val settingsText = transitionsContainer.findViewById<TextView>(R.id.settings_text)
        val collapse = transitionsContainer.findViewById<ImageView>(R.id.collapse)
        val divider = transitionsContainer.findViewById<View>(R.id.nav_divider)
        val dashboardImage = transitionsContainer.findViewById<ImageView>(R.id.dashboard)
        val settingsImage = transitionsContainer.findViewById<ImageView>(R.id.settings)
        val equipmentImage = transitionsContainer.findViewById<ImageView>(R.id.equipment)
        val jobsImage = transitionsContainer.findViewById<ImageView>(R.id.jobs)
        val coloredDashboard = transitionsContainer.findViewById<ImageView>(R.id.clicked_dashboard)
        val coloredSettings = transitionsContainer.findViewById<ImageView>(R.id.colored_settings)
        val coloredJobs = transitionsContainer.findViewById<ImageView>(R.id.colored_jobs)
        val coloredEquipment = transitionsContainer.findViewById<ImageView>(R.id.colored_equipment)


        if (string == "dashboard") {
            coloredDashboard.visibility = VISIBLE
            dashboardText.setTextColor(Color.parseColor("#00CA8F"))
        }
        if (string == "settings"){
            coloredSettings.visibility = VISIBLE
            settingsText.setTextColor(Color.parseColor("#00CA8F"))

        }
        if (string == "equipment"){
            coloredEquipment.visibility = VISIBLE
            equipmentText.setTextColor(Color.parseColor("#00CA8F"))
        }
        if (string == "jobs"){
            coloredJobs.visibility = VISIBLE
            jobsText.setTextColor(Color.parseColor("#00CA8F"))
        }
        TransitionManager.beginDelayedTransition(transitionsContainer)

        collapse.setOnClickListener {

            TransitionManager.beginDelayedTransition(transitionsContainer)
            visible = !visible
            val v = if (visible) View.GONE else View.VISIBLE
            collapseText.visibility = v
            dashboardText.visibility = v
            jobsText.visibility = v
            equipmentText.visibility = v
            settingsText.visibility = v
            val rotate = if (visible) 180f else 0f
            collapse.setRotation(rotate)



            val params = navBar.layoutParams
            val p = if (visible) 65 else 250
            params.width = p

            //changing width of horizontal divider
            val params2 = divider.layoutParams
            val p2 = if (visible) 65 else 250
            params2.width = p2

            animateActivity(visible)


        }

        dashboardImage.setOnClickListener{
            startActivity(Intent(this, Dashboard::class.java))
        }

        dashboardText.setOnClickListener{
            startActivity(Intent(this, Dashboard::class.java))
        }

        settingsImage.setOnClickListener{
            startActivity(Intent( this, Settings::class.java))
        }

        settingsText.setOnClickListener{
            startActivity(Intent( this, Settings::class.java))
        }

        equipmentImage.setOnClickListener{
            startActivity(Intent ( this, Equipment::class.java))
        }

        equipmentText.setOnClickListener{
            startActivity(Intent( this, Equipment::class.java))
        }

        jobsImage.setOnClickListener{
            startActivity(Intent ( this, Jobs::class.java))
        }

        jobsText.setOnClickListener{
            startActivity(Intent( this, Jobs::class.java))
        }



    }

    abstract fun animateActivity(boolean: Boolean)

}