package com.rooio.repairs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet


class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
      
        var visible = true
        //var navBar = findViewById<ViewGroup>(R.id.navigationView)
        val transitionsContainer = findViewById<ViewGroup>(R.id.navigationView)
        val collapseText = transitionsContainer.findViewById<TextView>(R.id.collapse_text)
        val dashboardText = transitionsContainer.findViewById<TextView>(R.id.dashboard_text)
        val jobsText = transitionsContainer.findViewById<TextView>(R.id.jobs_text)
        val equipmentText = transitionsContainer.findViewById<TextView>(R.id.equipment_text)
        val settingsText = transitionsContainer.findViewById<TextView>(R.id.settings_text)
        val collapse = transitionsContainer.findViewById<ImageView>(R.id.collapse)

        collapse.setOnClickListener{
            TransitionManager.beginDelayedTransition(transitionsContainer)
            visible = !visible
            val v = if (visible) View.GONE else View.VISIBLE
            collapseText.setVisibility(v)
            dashboardText.setVisibility(v)
            jobsText.setVisibility(v)
            equipmentText.setVisibility(v)
            settingsText.setVisibility(v)

            val params = transitionsContainer.getLayoutParams()
            val p = if (visible) 50 else 250
            params.width = p
            transitionsContainer.setLayoutParams(params)

            

        }
    }
}
