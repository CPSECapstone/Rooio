package com.rooio.repairs

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.GONE
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
    private lateinit var archivedText: TextView
    private lateinit var equipmentText: TextView
    private lateinit var settingsText: TextView
    private lateinit var logoutText: TextView
    private lateinit var collapse: ImageView
    private lateinit var divider: View
    private lateinit var dashboardImage: ImageView
    private lateinit var settingsImage: ImageView
    private lateinit var equipmentImage: ImageView
    private lateinit var jobsImage: ImageView
    private lateinit var logoutImage: ImageView
    private lateinit var archivedImage: ImageView
    private lateinit var coloredDashboard: ImageView
    private lateinit var coloredSettings: ImageView
    private lateinit var coloredJobs: ImageView
    private lateinit var coloredEquipment: ImageView
    private lateinit var coloredArchived: ImageView
    private lateinit var coloredLogout: ImageView
    private lateinit var transitionsContainer: ViewGroup
    private lateinit var transitionsContainer2: ViewGroup

    companion object {
        var collapsed = false
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
        archivedText = transitionsContainer2.findViewById(R.id.archived_text)
        equipmentText = transitionsContainer2.findViewById(R.id.equipment_text)
        settingsText = transitionsContainer2.findViewById(R.id.settings_text)
        logoutText = transitionsContainer2.findViewById(R.id.logout_text)
        collapse = transitionsContainer.findViewById(R.id.collapse)
        divider = transitionsContainer.findViewById(R.id.nav_divider)
        dashboardImage = transitionsContainer.findViewById(R.id.dashboard)
        settingsImage = transitionsContainer.findViewById(R.id.settings)
        equipmentImage = transitionsContainer.findViewById(R.id.equipment)
        jobsImage = transitionsContainer.findViewById(R.id.jobs)
        archivedImage = transitionsContainer.findViewById(R.id.archived_arrow)
        logoutImage = transitionsContainer.findViewById(R.id.logout)
        coloredDashboard = transitionsContainer.findViewById(R.id.clicked_dashboard)
        coloredSettings = transitionsContainer.findViewById(R.id.colored_settings)
        coloredJobs = transitionsContainer.findViewById(R.id.colored_jobs)
        coloredEquipment = transitionsContainer.findViewById(R.id.colored_equipment)
        coloredArchived = transitionsContainer.findViewById(R.id.colored_archived_arrow)
        coloredLogout = transitionsContainer.findViewById(R.id.colored_logout)
    }

    fun createNavigationBar(navType: NavigationType) {

        initializePage()
        if (navType == NavigationType.DASHBOARD) {
            coloredDashboard.visibility = VISIBLE
            dashboardImage.visibility = GONE
            dashboardText.setTextColor(Color.parseColor("#00CA8F"))
        }
        if (navType == NavigationType.SETTINGS){
            coloredSettings.visibility = VISIBLE
            settingsImage.visibility = GONE
            settingsText.setTextColor(Color.parseColor("#00CA8F"))

        }
        if (navType == NavigationType.EQUIPMENT){
            coloredEquipment.visibility = VISIBLE
            equipmentImage.visibility = GONE
            equipmentText.setTextColor(Color.parseColor("#00CA8F"))
        }
        if (navType == NavigationType.JOBS){
            coloredJobs.visibility = VISIBLE
            jobsImage.visibility = GONE
            jobsText.setTextColor(Color.parseColor("#00CA8F"))
        }

        if (navType == NavigationType.ARCHIVED){
            coloredArchived.visibility = VISIBLE
            archivedImage.visibility = GONE
            //archivedImage.visibility = INVISIBLE
            archivedText.setTextColor(Color.parseColor("#00CA8F"))
        }

        collapse.setOnClickListener {
            collapseBar(false, navType)
        }

        collapseText.setOnClickListener{
            collapseBar(false, navType)
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

        archivedImage.setOnClickListener{
            startActivity(Intent(this, JobsArchived::class.java))
        }

        archivedText.setOnClickListener{
            startActivity(Intent(this, JobsArchived::class.java))
        }

        logoutImage.setOnClickListener{
            startActivity(Intent(this, Landing::class.java))
            //Logan add logic for logging out here//
            logout()
        }

        logoutText.setOnClickListener{
            startActivity(Intent(this, Landing::class.java))
            //Logan add logic for logging out here//
            logout()
        }

        collapseBar(true, navType)
    }

    private fun logout(){
        val prefs = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(employeeId + "__token")
        editor.remove(employeeId + "__name")
        editor.remove(employeeId + "__userLocationId")
        editor.apply()
    }

    private fun collapseBar(isCreating: Boolean, navType: NavigationType){

        TransitionManager.beginDelayedTransition(transitionsContainer)

        if (!isCreating) collapsed = !collapsed

        val rotate = if (collapsed) 180f else 0f
        collapse.rotation = rotate


        val params = navBar.layoutParams
        val p = if (collapsed) 65 else 250
        params.width = p

        //changing width of horizontal divider
        val params2 = divider.layoutParams
        val p2 = if (collapsed) 65 else 250
        params2.width = p2

        animateActivity(collapsed)

        val v = if (collapsed) View.GONE else VISIBLE

        collapseText.visibility = v
        dashboardText.visibility = v
        jobsText.visibility = v
        equipmentText.visibility = v
        settingsText.visibility = v
        archivedText.visibility = v
        logoutText.visibility = v

    }


    abstract fun animateActivity(boolean: Boolean)

}