package com.rooio.repairs

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.transition.TransitionManager


abstract class NavigationBar : RestApi() {

    private lateinit var navBar: ViewGroup
    private lateinit var collapseText: TextView
    private lateinit var dashboardText: TextView
    private lateinit var jobsText: TextView
    private lateinit var  equipmentText: TextView
    private lateinit var  settingsText: TextView
    private lateinit var collapse: ImageView
    private lateinit var divider: View
    private lateinit var dashboardImage: ImageView
    private lateinit var settingsImage: ImageView
    private lateinit var equipmentImage: ImageView
    private lateinit var jobsImage: ImageView
    private lateinit var coloredDashboard: ImageView
    private lateinit var coloredSettings: ImageView
    private lateinit var coloredJobs: ImageView
    private lateinit var coloredEquipment: ImageView
    private lateinit var transitionsContainer: ViewGroup
    private lateinit var transitionsContainer2: ViewGroup

    companion object {
        var visible = false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_bar)
    }

    private fun initializePage() {
        transitionsContainer = findViewById(R.id.navConstraint)
        transitionsContainer2 = findViewById(R.id.navConstraint)

        navBar = transitionsContainer.findViewById(R.id.navigationView)
        collapseText = transitionsContainer2.findViewById(R.id.collapse_text)
        dashboardText = transitionsContainer2.findViewById(R.id.dashboard_text)
        jobsText = transitionsContainer2.findViewById(R.id.jobs_text)
        equipmentText = transitionsContainer2.findViewById(R.id.equipment_text)
        settingsText = transitionsContainer2.findViewById(R.id.settings_text)
        collapse = transitionsContainer.findViewById(R.id.collapse)
        divider = transitionsContainer.findViewById(R.id.nav_divider)
        dashboardImage = transitionsContainer.findViewById(R.id.dashboard)
        settingsImage = transitionsContainer.findViewById(R.id.settings)
        equipmentImage = transitionsContainer.findViewById(R.id.equipment)
        jobsImage = transitionsContainer.findViewById(R.id.jobs)
        coloredDashboard = transitionsContainer.findViewById(R.id.clicked_dashboard)
        coloredSettings = transitionsContainer.findViewById(R.id.colored_settings)
        coloredJobs = transitionsContainer.findViewById(R.id.colored_jobs)
        coloredEquipment = transitionsContainer.findViewById(R.id.colored_equipment)
    }

    fun createNavigationBar(navType: NavigationType) {

        initializePage()
        if (navType == NavigationType.DASHBOARD) {
            coloredDashboard.visibility = VISIBLE
            dashboardText.setTextColor(Color.parseColor("#00CA8F"))
        }
        if (navType == NavigationType.SETTINGS){
            coloredSettings.visibility = VISIBLE
            settingsText.setTextColor(Color.parseColor("#00CA8F"))

        }
        if (navType == NavigationType.EQUIPMENT){
            coloredEquipment.visibility = VISIBLE
            equipmentText.setTextColor(Color.parseColor("#00CA8F"))
        }
        if (navType == NavigationType.JOBS){
            coloredJobs.visibility = VISIBLE
            jobsText.setTextColor(Color.parseColor("#00CA8F"))
        }

        collapse.setOnClickListener {
            collapseBar(false)
        }

        collapseText.setOnClickListener{
            collapseBar(false)
        }

        dashboardImage.setOnClickListener{
            startActivity(Intent(this, Dashboard::class.java))
        }

        dashboardText.setOnClickListener{
            startActivity(Intent(this, Dashboard::class.java))
        }

        settingsImage.setOnClickListener{
            startActivity(Intent( this, PreferredProvidersSettings::class.java))
        }

        settingsText.setOnClickListener{
            startActivity(Intent( this, PreferredProvidersSettings::class.java))
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

        collapseBar(true)
    }

    private fun collapseBar(isCreating: Boolean){

        TransitionManager.beginDelayedTransition(transitionsContainer)

        if (!isCreating) visible = !visible

        val rotate = if (visible) 180f else 0f
        collapse.rotation = rotate


        val params = navBar.layoutParams
        val p = if (visible) 65 else 250
        params.width = p

        //changing width of horizontal divider
        val params2 = divider.layoutParams
        val p2 = if (visible) 65 else 250
        params2.width = p2

        animateActivity(visible)

        val v = if (visible) View.GONE else VISIBLE

        collapseText.visibility = v
        dashboardText.visibility = v
        jobsText.visibility = v
        equipmentText.visibility = v
        settingsText.visibility = v

    }


    abstract fun animateActivity(boolean: Boolean)

}