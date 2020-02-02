package com.rooio.repairs

import android.os.Bundle
import android.view.ViewGroup
import androidx.transition.TransitionManager

class Jobs : NavigationBar() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs)

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


        createNavigationBar("jobs")
    }

    override fun animateActivity(boolean: Boolean){
        //val viewGroup = findViewById<ViewGroup>(R.id.equipmentScroll)

        //TransitionManager.beginDelayedTransition(viewGroup)
        //val viewGroup = findViewById<ViewGroup>(R.id.jobsConstraint)

        //changing the width of the notableJobs and newJobRequest
        //val my_pending = viewGroup.findViewById<ViewGroup>(R.id.pending)
        //val my_scheduled = viewGroup.findViewById<ViewGroup>(R.id.scheduled)
        //val my_in_progress = viewGroup.findViewById<ViewGroup>(R.id.in_progress)

        //TransitionManager.beginDelayedTransition(viewGroup)
        //val boxParams1 = my_pending.layoutParams
        //val boxParams2 = my_scheduled.layoutParams
        //val boxParams3 = my_in_progress.layoutParams
        //val p2 = if (boolean) 300 else 430
        //boxParams1.width = p2
        //boxParams2.width = p2
        //boxParams3.width = p2

        //calling the transitions
        //my_pending.layoutParams = boxParams1
        //my_scheduled.layoutParams = boxParams2
        //my_in_progress.layoutParams = boxParams3


    }
}