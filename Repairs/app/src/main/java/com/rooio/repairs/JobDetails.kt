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
import android.view.ViewGroup.MarginLayoutParams

//Job details can be viewed when clicking on a job request found under the Jobs tab
class JobDetails : NavigationBar() {

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
    private lateinit var jobDetailsInfoLayout: ConstraintLayout

    private lateinit var viewEquipment: TextView
    private lateinit var jobStatus: TextView
    private lateinit var transitionsContainer: ViewGroup
    private lateinit var viewGroup: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details)

        onResume()
        initializeVariables()
        initializeAnimationVariables()
        setNavigationBar()
        setActionBar()

        createNavigationBar(NavigationType.JOBS)
        onBack()
        onDropDown()
        getJobId()
        loadJobs()
        onPause()
    }

    //Get the JobId from the previous page
    private fun getJobId() {
        val incomingIntent = intent
        jobId = (incomingIntent?.getStringExtra("id")).toString()
    }

    //Request JobDetails from API
    @JvmField
    val responseFunc = Function<Any, Void?> { response: Any ->
        val jsonObject1 = response as JSONObject

        try {
            loadElements(jsonObject1)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        null
    }

    @JvmField
    var errorFunc = Function<String, Void?> { string: String? ->
        error.text = string
        null

    }

    private fun loadJobs() {
        val url = "service-locations/$userLocationID/jobs/$jobId/"
        requestJson(Request.Method.GET, JsonType.OBJECT, JsonRequest(false, url, null, responseFunc, errorFunc, true))
    }

    //Initializes variables that are used in loadElements()
    private fun initializeVariables() {
        jobStatus = findViewById(R.id.jobStatus)
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
        jobDetailsInfoLayout = transitionsContainer.findViewById(R.id.jobDetailInfoLayout)
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
    override fun animateActivity(boolean: Boolean) {
        val amount = if (boolean) -190f else 0f
        val animation = ObjectAnimator.ofFloat(backButton, "translationX", amount)
        if (boolean) animation.duration = 1300 else animation.duration = 300
        animation.start()
    }

    //Sets the text views in the user interface, with "--" if null
    @SuppressLint("SimpleDateFormat")
    private fun loadElements(r: JSONObject) {
        val jobData = JobData(r)

        val status = jobData.status
        jobStatus.text = status.toString()
        DrawableCompat.setTint(
                DrawableCompat.wrap(activeLayout.background),
                ContextCompat.getColor(this, status.getColor())
        )

        pointOfContact.text = jobData.pointOfContact
        details.text = jobData.details
        serviceType.text = jobData.serviceType.toString()
        restaurantLocation.text = jobData.serviceLocation.getString("physical_address")
        restaurantName.text = jobData.serviceLocation.getJSONObject("internal_client").getString("name")
        availableTechnicians.text = jobData.serviceCompany.getString("name")

        val equipmentList = jobData.equipmentList
        if (equipmentList.size != 0) {
            val equipmentData = equipmentList[0]
            equipmentName.text = equipmentData.name
            manufacturer.text = equipmentData.manufacturer
            serialNumber.text = equipmentData.serialNumber
            modelNumber.text = equipmentData.modelNumber
            location.text = equipmentData.location

            if (equipmentData.lastServiceBy == "") {
                lastServiceBy.text = ("--")
            } else {
                lastServiceBy.text = equipmentData.lastServiceBy
            }

            if (equipmentData.lastServiceDate == "") {
                lastServiceDate.text = ("--")
            } else {
                val date1 = SimpleDateFormat("yyyy-MM-dd").parse(convertToNewFormat2(equipmentData.lastServiceDate))
                lastServiceDate.text = (date1!!.toString())
            }
        } else {
            equipmentLayout.visibility = View.GONE
            viewEquipment.visibility = View.GONE
            val params = jobDetailsInfoLayout.layoutParams as MarginLayoutParams
            params.bottomMargin = 50
        }

        @SuppressLint("SimpleDateFormat") val dateFormatter = SimpleDateFormat("MMMM d, y hh:mm a zzz")
        if (status == JobType.STARTED || status == JobType.PAUSED) {
            if (jobData.estimatedArrivalTime != "") {
                val date2 = (SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(convertToNewFormat(jobData.estimatedArrivalTime))
                startedOn.text = (dateFormatter.format(date2).toString())
            }
        } else {
            if (jobData.statusTimeValue != "") {
                val date2 = (SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(convertToNewFormat(jobData.statusTimeValue))
                startedOn.text = (dateFormatter.format(date2).toString())
            }
        }
    }

    //Sends the user to the Jobs page
    private fun onBack() {
        backButton.setOnClickListener {
            super.onBackPressed()
        }
    }

    //Initially closes the equipment dropdown and allows the user to collapse or expand
    private fun onDropDown() {
        var visible = false
        setVisibility(View.GONE)
        val initial = equipmentLayout.layoutParams
        initial.height = 90
        dropDown.setOnClickListener {
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