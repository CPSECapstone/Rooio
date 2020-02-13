package com.rooio.repairs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.transition.TransitionManager




class Dashboard : NavigationBar() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        //sets the navigation bar onto the page
        val nav_inflater = layoutInflater
        val tmpView = nav_inflater.inflate(R.layout.activity_navigation_bar, null)

        window.addContentView(tmpView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        //sets the action bar onto the page

        val actionbar_inflater = layoutInflater
        val poopView = actionbar_inflater.inflate(R.layout.action_bar, null)
        window.addContentView(poopView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        supportActionBar!!.elevation = 0.0f


        createNavigationBar("dashboard")
    }


    override fun animateActivity(boolean: Boolean){
        val viewGroup = findViewById<ViewGroup>(R.id.dashboardConstraint)

        //changing the width of the notableJobs and newJobRequest
        val notableJobs = viewGroup.findViewById<ViewGroup>(R.id.notableJobs)
        val newJobRequest = viewGroup.findViewById<ViewGroup>(R.id.newJobRequest)

        TransitionManager.beginDelayedTransition(viewGroup)
        val boxParams1 = notableJobs.layoutParams
        val boxParams2 = newJobRequest.layoutParams
        val p2 = if (boolean) 1004 else 803
        boxParams1.width = p2
        boxParams2.width = p2

        //calling the transitions
        notableJobs.layoutParams = boxParams1
        newJobRequest.layoutParams = boxParams2
    }
}
