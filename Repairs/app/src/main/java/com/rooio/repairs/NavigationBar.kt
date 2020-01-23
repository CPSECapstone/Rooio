package com.rooio.repairs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.activity_navigation_bar.*

abstract class NavigationBar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_bar)
    }

    fun createNavigationBar()
    {
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
        val expand = transitionsContainer.findViewById<ImageView>(R.id.expand)

        expand.setOnClickListener{
            collapse.visibility = View.VISIBLE
            expand.visibility = View.GONE

            TransitionManager.beginDelayedTransition(transitionsContainer)
            visible = !visible
            val v = if (visible) View.GONE else View.VISIBLE
            collapseText.setVisibility(v)
            dashboardText.setVisibility(v)
            jobsText.setVisibility(v)
            equipmentText.setVisibility(v)
            settingsText.setVisibility(v)


            //changing width of the side navigation bar
            val params = navBar.layoutParams
            val p = if (visible) 65 else 250
            params.width = p

            //changing width of horizontal divider
            val params2 = divider.layoutParams
            val p2 = if (visible) 65 else 250
            params2.width = p2

            animateActivity(visible)
        }

        collapse.setOnClickListener {

            TransitionManager.beginDelayedTransition(transitionsContainer)
            visible = !visible
            val v = if (visible) View.GONE else View.VISIBLE
            collapseText.setVisibility(v)
            dashboardText.setVisibility(v)
            jobsText.setVisibility(v)
            equipmentText.setVisibility(v)
            settingsText.setVisibility(v)

            expand.visibility = View.VISIBLE
            collapse.visibility = View.GONE

            val params = navBar.layoutParams
            val p = if (visible) 65 else 250
            params.width = p

            //changing width of horizontal divider
            val params2 = divider.layoutParams
            val p2 = if (visible) 65 else 250
            params2.width = p2

            animateActivity(visible)
        }
    }

    abstract fun animateActivity(boolean: Boolean)

}