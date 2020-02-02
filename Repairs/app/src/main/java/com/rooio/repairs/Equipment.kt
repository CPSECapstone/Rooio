package com.rooio.repairs

import android.os.Bundle
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager

class Equipment : NavigationBar() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment_1)

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


        createNavigationBar("equipment")
    }

    override fun animateActivity(boolean: Boolean){
        val equipmentScroll = findViewById<ViewGroup>(R.id.equipmentScroll)
        TransitionManager.beginDelayedTransition(equipmentScroll)

        val pageConstraint = findViewById<ConstraintLayout>(R.id.equipmentPageConstraint)

        val constraintSet1 = ConstraintSet()
        constraintSet1.load(this, R.layout.activity_equipment_1)

        val constraintSet2 = ConstraintSet()
        constraintSet2.load(this, R.layout.activity_equipment_2)

        TransitionManager.beginDelayedTransition(pageConstraint)
        val constraint = if (boolean) constraintSet1 else constraintSet2

        constraint.applyTo(pageConstraint)
    }
}