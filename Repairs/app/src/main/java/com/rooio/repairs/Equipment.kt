package com.rooio.repairs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import org.json.JSONArray
import org.json.JSONException

class Equipment : NavigationBar() {

    var equipmentListView: ListView? = null
    private val equipmentList = ArrayList<EquipmentData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment_2)

        //sets the navigation bar onto the page
        val navInflater = layoutInflater
        val tmpView = navInflater.inflate(R.layout.activity_navigation_bar, null)

        window.addContentView(tmpView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        //sets the action bar onto the page
        val actionbarInflater = layoutInflater
        val actionbarView = actionbarInflater.inflate(R.layout.action_bar, null)
        window.addContentView(actionbarView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        supportActionBar!!.elevation = 0.0f

        //making navigation bar w/ Equipment text highlighted
        createNavigationBar("equipment")

        equipmentListView = findViewById<View>(R.id.equipmentList) as ListView

        //get equipment
        loadEquipment();
    }

    private fun loadEquipment() {
        val url = "https://capstone.api.roopairs.com/v0/service-locations/" + getUserLocationID().toString() + "/equipment/"

        var responseFunc = Function<Any, Void?> { jsonResponse: Any? ->
            try {
                val jsonArray = jsonResponse as JSONArray
                loadElements(jsonArray)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            null
        }

        var errorFunc = Function<String, Void?> { string: String? ->
            null
        }

        val request = JsonRequest(false, url, null, responseFunc, errorFunc, true)
        requestGetJsonArray(request)
    }

    private fun loadElements(response: JSONArray) {
        equipmentList.clear()
        for (i in 0 until response.length()) {
            val equipment = EquipmentData(response.getJSONObject(i))
            equipmentList.add(equipment);
        }

        val customAdapter = EquipmentCustomAdapter(this, equipmentList)
        if (equipmentList?.size != 0) equipmentListView!!.adapter = customAdapter
    }

    override fun animateActivity(boolean: Boolean){
        /*val pageConstraint = findViewById<ConstraintLayout>(R.id.equipmentPageConstraint)

        val constraintSet1 = ConstraintSet()
        constraintSet1.load(this, R.layout.activity_equipment_1)

        val constraintSet2 = ConstraintSet()
        constraintSet2.load(this, R.layout.activity_equipment_2)

        TransitionManager.beginDelayedTransition(pageConstraint)
        val constraint = if (boolean) constraintSet1 else constraintSet2

        constraint.applyTo(pageConstraint)
        */
        val pageConstraint = findViewById<ConstraintLayout>(R.id.equipmentPageConstraint)

        val equipment = pageConstraint.findViewById<ConstraintLayout>(R.id.equipmentBarConstraint)

        TransitionManager.beginDelayedTransition(pageConstraint)
        val params = equipment.layoutParams
        val change = if (boolean) 471 else 654
        params.width = change

        equipment.layoutParams = params
    }
}