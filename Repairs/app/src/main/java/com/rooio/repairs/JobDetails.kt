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
import org.json.JSONException
import org.json.JSONObject
import androidx.arch.core.util.Function
import com.android.volley.Request
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat.getSystemService

import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
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

    private fun getJobId(){
        val incomingIntent = intent
        jobId = (incomingIntent.getStringExtra("id")).toString()
    }

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

        val locationObj = response.getJSONObject("service_location")
        val internal_client = locationObj.getJSONObject("internal_client")
        val serviceObj = response.getJSONObject("service_company")
        val equipmentObjList = response.getJSONArray("equipment")
        //ALlow multiple different Equipment Cards to be made
        val equipmentObj = equipmentObjList.getJSONObject(0)

        //set serviceType
        val category = equipmentObj.getString("service_category")
        var repairCategory = ""
        when (category) {
            "4" ->
                repairCategory = "General Appliance"
            "1" ->
                repairCategory = "HVAC"
            "2" ->
                repairCategory = "Lighting and Electrical"
            "3" ->
                repairCategory = "Plumbing"
        }
        serviceType.setText(repairCategory)

        restaurantLocation.setText(locationObj.getString("physical_address"))
        restaurantName.setText(internal_client.getString("name"))
        availableTechnicians.setText(serviceObj.getString("name"))
        if(!response.isNull("status_time_value")){
            val date2 = (SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(convertToNewFormat(response.getString("status_time_value")))
            startedOn.setText(date2!!.toString())
        }
        pointOfContact.setText(response.getString("point_of_contact_name"))
        details.setText(response.getString("details"))
        equipmentName.setText(equipmentObj.getString("display_name"))
        manufacturer.setText(equipmentObj.getString("manufacturer"))
        serialNumber.setText(equipmentObj.getString("serial_number"))
        modelNumber.setText(equipmentObj.getString("model_number"))
        location.setText(equipmentObj.getString("location"))
        lastServiceBy.setText(equipmentObj.getString("last_service_by"))
        if (equipmentObj.isNull("last_service_by") || (equipmentObj.getString("last_service_by") == "")){
            lastServiceBy.setText("--")
        }
        else{
            lastServiceBy.setText(equipmentObj.getString("last_service_by"))

        }
        if (equipmentObj.isNull("last_service_date")){
            lastServiceDate.setText("--")
        }
        else{
            val date1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(convertToNewFormat(equipmentObj.getString("last_service_date")))
            lastServiceDate.setText(date1!!.toString())
        }

        val status_enum = response.getInt("status")
        var status_value: String? = null

        //Display Statuses for each swimlane
        when (status_enum) {
            //Declined Swimlane
            1 -> {
                status_value = "Declined"
                DrawableCompat.setTint(
                        DrawableCompat.wrap(activeLayout.getBackground()),
                        ContextCompat.getColor(this, R.color.lightGray)
                )
            }
            //Scheduled swimlane uses time as status
            2 -> {
                //Time Based Statuses
                status_value = "Scheduled"

                DrawableCompat.setTint(
                        DrawableCompat.wrap(activeLayout.getBackground()),
                        ContextCompat.getColor(this, R.color.Blue)
                )
            }
            3 -> {
                status_value = "Archived"

                DrawableCompat.setTint(
                        DrawableCompat.wrap(activeLayout.getBackground()),
                        ContextCompat.getColor(this, R.color.lightGray)
                )
            }
            //Cancelled Swimlane Status
            4 -> {
                status_value = "Cancelled"
                DrawableCompat.setTint(
                        DrawableCompat.wrap(activeLayout.getBackground()),
                        ContextCompat.getColor(this, R.color.lightGray)
                )
            }
            5 -> {
                //In Progress Swimlane Status
                status_value = "Started"

                DrawableCompat.setTint(
                        DrawableCompat.wrap(activeLayout.getBackground()),
                        ContextCompat.getColor(this, R.color.colorPrimary)
                )
            }
            //In Progress Swimlane Status
            6 -> {
                status_value = "Paused"

                DrawableCompat.setTint(
                        DrawableCompat.wrap(activeLayout.getBackground()),
                        ContextCompat.getColor(this, R.color.Yellow)
                )
            }
            //Pending Swimlane Status
            0 -> {
                status_value = "Awaiting Response"

                DrawableCompat.setTint(
                        DrawableCompat.wrap(activeLayout.getBackground()),
                        ContextCompat.getColor(this, R.color.Purple)
                )
            }
        }
        restaurantLocation2.text = status_value
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

    @Throws(ParseException::class)
    fun convertToNewFormat(dateStr: String): String {
        val utc = TimeZone.getTimeZone("UTC")
        val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val destFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
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