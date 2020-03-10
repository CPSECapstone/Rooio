package com.rooio.repairs

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import org.json.JSONObject

//Job details can be viewed when clicking on a job request found under the Jobs tab
class JobDetails: NavigationBar() {

    private lateinit var restaurantName: TextView
    private lateinit var restaurantLocation: TextView
    private lateinit var serviceType: TextView
    private lateinit var availableTechnicians: TextView
    private lateinit var startedOn: TextView
    private lateinit var equipmentName: TextView
    private lateinit var manufacturer: TextView
    private lateinit var modelNumber: TextView
    private lateinit var location: TextView
    private lateinit var serialNumber: TextView
    private lateinit var lastServiceBy: TextView
    private lateinit var lastServiceDate: TextView
    private lateinit var manufacturerText: TextView
    private lateinit var modelText: TextView
    private lateinit var locationText: TextView
    private lateinit var serialText: TextView
    private lateinit var lastServiceByText: TextView
    private lateinit var lastServiceDateText: TextView
    private lateinit var pointOfContact: TextView
    private lateinit var details: TextView
    private lateinit var backButton: ImageView
    private lateinit var dropDown: ImageView
    private lateinit var equipmentDivider: ImageView
    private lateinit var equipmentLayout: ConstraintLayout
    private lateinit var viewEquipment: TextView
    private lateinit var transitionsContainer: ViewGroup
    private lateinit var viewGroup: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details)

        initializeVariables()
        initializeAnimationVariables()
        setNavigationBar()
        setActionBar()
        createNavigationBar("jobs")
        loadJobDetails()
        onBack()
        onDropDown()
    }

    //Initializes variables that are used in loadElements()
    private fun initializeVariables() {
        restaurantName = findViewById(R.id.restaurantName)
        restaurantLocation = findViewById(R.id.restaurantLocation)
        serviceType = findViewById(R.id.serviceInfo)
        availableTechnicians = findViewById(R.id.technicianInfo)
        startedOn = findViewById(R.id.startedInfo)
        pointOfContact = findViewById(R.id.contactInfo)
        details = findViewById(R.id.detailsInfo)
        transitionsContainer = findViewById(R.id.jobDetailLayout)
        viewGroup = findViewById(R.id.jobDetailTitleLayout)
    }

    //Initializes variables that are used in loadElements and animated
    private fun initializeAnimationVariables() {
        //Equipment dropdown
        equipmentLayout = transitionsContainer.findViewById(R.id.equipmentLayout)
        equipmentName = transitionsContainer.findViewById(R.id.equipmentName)
        dropDown = transitionsContainer.findViewById(R.id.dropDown)
        manufacturer = transitionsContainer.findViewById(R.id.manufacturerInfo)
        modelNumber = transitionsContainer.findViewById(R.id.modelInfo)
        location = transitionsContainer.findViewById(R.id.locationInfo)
        serialNumber = transitionsContainer.findViewById(R.id.serialInfo)
        lastServiceBy = transitionsContainer.findViewById(R.id.lastServiceByInfo)
        lastServiceDate = transitionsContainer.findViewById(R.id.lastServiceDateInfo)
        manufacturerText = transitionsContainer.findViewById(R.id.manufacturerText)
        modelText = transitionsContainer.findViewById(R.id.modelText)
        locationText = transitionsContainer.findViewById(R.id.locationText)
        serialText = transitionsContainer.findViewById(R.id.serialText)
        lastServiceByText = transitionsContainer.findViewById(R.id.lastServiceByText)
        lastServiceDateText = transitionsContainer.findViewById(R.id.lastServiceDateText)
        equipmentDivider = transitionsContainer.findViewById(R.id.equipmentDivider)
        viewEquipment = transitionsContainer.findViewById(R.id.viewEquipment)

        //Navigation bar collapse/expand
        backButton = viewGroup.findViewById(R.id.backButton)
    }

    //Animates the main page content when the navigation bar collapses/expands
    override fun animateActivity(boolean: Boolean){
        val amount = if (boolean) -190f else 0f
        val animation = ObjectAnimator.ofFloat(backButton, "translationX", amount)
        if (boolean) animation.duration = 1300 else animation.duration = 300
        animation.start()
    }

    //Sets the text views in the user interface, with "--" if null
    private fun loadElements(response: JSONObject) {
        //NEEDS API WORK
        setElementText(restaurantName, response,"")
        setElementText(restaurantLocation, response,"")
        setElementText(serviceType, response,"")
        setElementText(availableTechnicians, response,"")
        setElementText(startedOn, response,"")
        setElementText(pointOfContact, response,"")
        setElementText(details, response,"")
        setElementText(equipmentName, response,"")
        setElementText(manufacturer, response,"")
        setElementText(serialNumber, response,"")
        setElementText(modelNumber, response,"")
        setElementText(location, response,"")
        setElementText(lastServiceBy, response,"")
        setElementText(lastServiceDate, response,"")
    }

    //Loads job request details from API including equipment information
    private fun loadJobDetails() {
        //NEEDS API WORK
        val response = JSONObject()
        loadElements(response)
    }

    private fun setElementText(element: TextView, response: JSONObject, elementName: String){
        //NEEDS API WORK
        element.text = "--"
    }

    //Sets the navigation bar onto the page
    private fun setNavigationBar() {
        val navBarInflater = layoutInflater
        val navBarView = navBarInflater.inflate(R.layout.activity_navigation_bar, null)
        window.addContentView(navBarView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    //Sets the action bar onto the page
    private fun setActionBar() {
        val actionBarInflater = layoutInflater
        val actionBarView = actionBarInflater.inflate(R.layout.action_bar, null)
        window.addContentView(actionBarView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        supportActionBar!!.elevation = 0.0f
    }

    //Sends the user to the Jobs page
    private fun onBack() {
        backButton.setOnClickListener{
            startActivity(Intent(this@JobDetails, Jobs::class.java))
        }
    }

    //Initially closes the equipment dropdown and allows the user to collapse or expand
    private fun onDropDown() {
        var visible = false
        setVisibility(View.GONE)
        val initial = equipmentLayout.layoutParams
        initial.height = 90
        dropDown.setOnClickListener{
            TransitionManager.beginDelayedTransition(transitionsContainer)
            visible = !visible
            val v = if (visible) View.VISIBLE else View.GONE
            val op = if (visible) View.GONE else View.VISIBLE
            viewEquipment.visibility = op
            setVisibility(v)
            val rotate = if (visible) 180f else 0f
            dropDown.rotation = rotate
            val params = equipmentLayout.layoutParams
            val p = if (visible) 395 else 90
            params.height = p
        }
    }

    //Switches the visibility of Equipment UI elements
    private fun setVisibility(v: Int) {
        manufacturerText.visibility = v
        modelText.visibility = v
        locationText.visibility = v
        serialText.visibility = v
        lastServiceByText.visibility = v
        lastServiceDateText.visibility = v
        manufacturer.visibility = v
        modelNumber.visibility = v
        location.visibility = v
        serialNumber.visibility = v
        lastServiceBy.visibility = v
        lastServiceDate.visibility = v
        equipmentDivider.visibility = v
    }
}