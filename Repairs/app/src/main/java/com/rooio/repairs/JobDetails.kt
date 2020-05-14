package com.rooio.repairs

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import org.json.JSONException
import org.json.JSONObject
import androidx.arch.core.util.Function
import com.android.volley.Request
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import kotlinx.android.synthetic.main.activity_job_details.*


//Job details can be viewed when clicking on a job request found under the Jobs tab
class JobDetails: NavigationBar() {

    private lateinit var jobId: String
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
    private lateinit var error: TextView
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

        createNavigationBar(NavigationType.JOBS)
        onBack()
        onDropDown()
        getJobId()
        loadJobs()
    }

    //Get the JobId from the previous page
    private fun getJobId(){
        val incomingIntent = intent
        jobId = (incomingIntent?.getStringExtra("id")).toString()
    }

    //Request JobDetails from API
    @JvmField
    val responseFunc = Function<Any, Void?> { response : Any ->
        val jsonObject1 = response as JSONObject

        try {
            loadElements(jsonObject1)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        null
    }

    @JvmField
    var errorFunc = Function<String, Void?> {  string: String? ->
        error.text = string
        null

    }
    private fun loadJobs(){
        val url =  "service-locations/$userLocationID/jobs/$jobId/"
        requestJson(Request.Method.GET, JsonType.OBJECT, JsonRequest(false, url, null, responseFunc, errorFunc, true))
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
        error = findViewById(R.id.errorMessage)
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
    @SuppressLint("SimpleDateFormat")
    private fun loadElements(response: JSONObject) {

        val locationObj = response.getJSONObject("service_location")
        val internal_client = locationObj.getJSONObject("internal_client")
        val serviceObj = response.getJSONObject("service_company")
        val equipmentObjList = response.getJSONArray("equipment")
        //ALlow multiple different Equipment Cards to be made
        val equipmentObj = equipmentObjList.optJSONObject(0)

        lateinit var category: String
        if (equipmentObj != null) {
            category = equipmentObj.getString("service_category")
            equipmentName.text = (equipmentObj.getString("display_name"))
            manufacturer.text = (equipmentObj.getString("manufacturer"))
            serialNumber.text = (equipmentObj.getString("serial_number"))
            modelNumber.text = (equipmentObj.getString("model_number"))
            location.text = (equipmentObj.getString("location"))
            lastServiceBy.text = (equipmentObj.getString("last_service_by"))
            if (equipmentObj.isNull("last_service_by") || (equipmentObj.getString("last_service_by") == "")){
                lastServiceBy.text = ("--")
            }
            else{
                lastServiceBy.text = (equipmentObj.getString("last_service_by"))

            }
            //lastServiceDate.text = ("--")

            if (equipmentObj.isNull("last_service_date")){
                lastServiceDate.text = ("--")
            }
            else{
                val date1 = SimpleDateFormat("yyyy-MM-dd").parse(convertToNewFormat2(equipmentObj.getString("last_service_date")))
                lastServiceDate.text = (date1!!.toString())
            }


        } else {
            category = "4"
            equipmentName.text = "--"
            equipmentName.text = "--"
            manufacturer.text = "--"
            serialNumber.text = "--"
            modelNumber.text = "--"
            location.text = "--"
            lastServiceBy.text = "--"

            lastServiceDate.text = ("--")
            }

        //set serviceType
        var repairCategory = ""
        when (category) {
            "4" -> {
                repairCategory = "General Appliance"
                equipmentLayout.visibility = View.GONE
                viewEquipment.visibility = View.GONE
            }
            "1" ->
                repairCategory = "HVAC"
            "2" ->
                repairCategory = "Lighting and Electrical"
            "3" ->
                repairCategory = "Plumbing"
        }
        serviceType.text = repairCategory
        restaurantLocation.text = locationObj.getString("physical_address")
        restaurantName.text = internal_client.getString("name")
        availableTechnicians.text = serviceObj.getString("name")
        if(!response.isNull("status_time_value")){
            val date2 = (SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(convertToNewFormat(response.getString("status_time_value")))
            startedOn.text = (date2!!.toString())
        }
        pointOfContact.text = (response.getString("point_of_contact_name"))
        details.text = (response.getString("details"))

        val statusEnum = response.getInt("status")
        var statusValue: String? = null

        //Display Statuses for each swimlane
        when (statusEnum) {
            //Declined Swimlane
            1 -> {
                statusValue = "Declined"
                DrawableCompat.setTint(
                        DrawableCompat.wrap(activeLayout.getBackground()),
                        ContextCompat.getColor(this, R.color.lightGray)
                )
            }
            //Scheduled swimlane uses time as status
            2 -> {
                //Time Based Statuses
                statusValue = "Scheduled"
            }
            3 -> {
                statusValue = "Archived"
            }
            //Cancelled Swimlane Status
            4 -> {
                statusValue = "Cancelled"
            }
            5 -> {
                //In Progress Swimlane Status
                statusValue = "Started"
            }
            //In Progress Swimlane Status
            6 -> {
                statusValue = "Paused"
            }
            //Pending Swimlane Status
            0 -> {
                statusValue = "Awaiting Response"

            }
        }
        changeHeaderColor(statusEnum)
        restaurantLocation2.text = statusValue
    }

    //Change the HeaderColor
    private fun changeHeaderColor(statusEnum: Int){
        when (statusEnum) {
            //Declined Swimlane
            1 -> {
                DrawableCompat.setTint(
                        DrawableCompat.wrap(activeLayout.getBackground()),
                        ContextCompat.getColor(this, R.color.lightGray)
                )
            }
            //Scheduled swimlane uses time as status
            2 -> {
                DrawableCompat.setTint(
                        DrawableCompat.wrap(activeLayout.getBackground()),
                        ContextCompat.getColor(this, R.color.Blue)
                )
            }
            3 -> {
                DrawableCompat.setTint(
                        DrawableCompat.wrap(activeLayout.getBackground()),
                        ContextCompat.getColor(this, R.color.lightGray)
                )
            }
            //Cancelled Swimlane Status
            4 -> {
                DrawableCompat.setTint(
                        DrawableCompat.wrap(activeLayout.getBackground()),
                        ContextCompat.getColor(this, R.color.lightGray)
                )
            }
            5 -> {
                //In Progress Swimlane Status
                DrawableCompat.setTint(
                        DrawableCompat.wrap(activeLayout.getBackground()),
                        ContextCompat.getColor(this, R.color.colorPrimary)
                )
            }
            //In Progress Swimlane Status
            6 -> {
                DrawableCompat.setTint(
                        DrawableCompat.wrap(activeLayout.getBackground()),
                        ContextCompat.getColor(this, R.color.Yellow)
                )
            }
            //Pending Swimlane Status
            0 -> {
                DrawableCompat.setTint(
                        DrawableCompat.wrap(activeLayout.getBackground()),
                        ContextCompat.getColor(this, R.color.Purple)
                )
            }
        }
    }

    //Sends the user to the Jobs page
    private fun onBack() {
        backButton.setOnClickListener{
            super.onBackPressed()
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

    //Sets the time format
    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun convertToNewFormat(dateStr: String): String {
        val utc = TimeZone.getTimeZone("UTC")
        val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val destFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sourceFormat.timeZone = utc
        val convertedDate = sourceFormat.parse(dateStr)
        return destFormat.format(convertedDate!!)
    }

    //Sets the time format
    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun convertToNewFormat2(dateStr: String): String {
        val utc = TimeZone.getTimeZone("UTC")
        val sourceFormat = SimpleDateFormat("yyyy-MM-dd")
        val destFormat = SimpleDateFormat("yyyy-MM-dd")
        sourceFormat.timeZone = utc
        val convertedDate = sourceFormat.parse(dateStr)
        return destFormat.format(convertedDate!!)
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