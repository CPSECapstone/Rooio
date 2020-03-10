package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject
import androidx.arch.core.util.Function
import com.android.volley.Request
import kotlinx.android.synthetic.main.activity_create_job_details.*

//Job details can be viewed when clicking on a job request found under the Jobs tab
class CreateJobDetails: NavigationBar() {

    private lateinit var restuarantLocation: TextView
    private lateinit var serviceType: Spinner
    private lateinit var whatHappened: TextInputEditText
    private lateinit var month: Spinner
    private lateinit var date: Spinner
    private lateinit var time: Spinner
    private lateinit var contact: TextInputEditText
    private lateinit var phoneNumber: TextInputEditText
    private lateinit var sendRequestButton: Button
    private lateinit var errorMsg: TextView

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
    private lateinit var expandBackButton: ImageView
    private lateinit var collapseBackButton: ImageView
    private lateinit var dropDown: ImageView
    private lateinit var equipmentDivider: ImageView
    private lateinit var equipmentLayout: ConstraintLayout
    private lateinit var viewEquipment: TextView
    private lateinit var transitionsContainer: ViewGroup
    private lateinit var viewGroup: ViewGroup
    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_job_details)

        initializeVariables()
        initializeAnimationVariables()
        setNavigationBar()
        setActionBar()
        createNavigationBar("jobs")

        initializeUI()
        onSendRequest()
        onDropDown()
    }

    //Initializes variables that are used in loadElements()
    private fun initializeVariables() {
        restuarantLocation = findViewById(R.id.restaurantLocation)
        serviceType = findViewById(R.id.serviceTypeSpinner)
        whatHappened = findViewById(R.id.whatHappenedInput)
        month = findViewById(R.id.monthSpinner)
        date = findViewById(R.id.dateSpinner)
        time = findViewById(R.id.timeSpinner)
        contact = findViewById(R.id.contactInput)
        phoneNumber = findViewById(R.id.phoneNumberInput)
        errorMsg = findViewById(R.id.errorMessage)
        sendRequestButton = findViewById(R.id.sendRequestButton)
        transitionsContainer = findViewById(R.id.jobDetailLayout)
        viewGroup = findViewById(R.id.jobDetailTitleLayout)
        scrollView = findViewById(R.id.jobDetailScrollView)

        serviceType.adapter = ArrayAdapter<ServiceType>(this, android.R.layout.simple_list_item_1, ServiceType.values())
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
        expandBackButton = viewGroup.findViewById(R.id.expandBackButton)
        collapseBackButton = viewGroup.findViewById(R.id.collapseBackButton)
    }

    //Animates the main page content when the navigation bar collapses/expands
    override fun animateActivity(boolean: Boolean){
        TransitionManager.beginDelayedTransition(viewGroup)
        val v = if (boolean) View.VISIBLE else View.GONE
        val op = if (boolean) View.GONE else View.VISIBLE
        expandBackButton.visibility = v
        collapseBackButton.visibility = op
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

    // initializing restaurant location text and equipment widget
    private fun initializeUI() {
        requestLocation()
        //requestEquipmentInfo()
    }

    private fun requestEquipmentInfo() {
        // get equipment information from whichever piece of equipment that the user chose earlier
        TODO("not implemented")
    }

    // sending JSONRequest for the restaurant location
    private fun requestLocation() {
        val url = BaseUrl + "service-locations/$userLocationID/"
        val request = JsonRequest(false, url, HashMap(), responseFuncLoad, errorFuncLoad, true)
        requestJson(Request.Method.GET, JsonType.OBJECT, request)
    }

    @JvmField
    // set restaurant location text at top of screen
    var responseFuncLoad = Function<Any, Void?>{ jsonResponse: Any? ->
        try{
            val jsonObject = jsonResponse as JSONObject
            restuarantLocation.text = jsonObject.getString("physical_address_formatted")
        } catch (e: JSONException){
            errorMsg.text = e.toString()
        }
        null
    }

    @JvmField
    var errorFuncLoad = Function<String, Void?> {
        restuarantLocation.text = it
        null
    }

    private fun onSendRequest() {
        val params = HashMap<Any?, Any?>()
        val url = BaseUrl + "service-locations/$userLocationID/jobs/"
        sendRequestButton.setOnClickListener {
            errorMsg.text = ""
            params["equipment"] = intent.getStringExtra("equipment")
            params["service_company"] = intent.getIntExtra("service_company", 0)
            params["service_category"] = intent.getIntExtra("service_category", 0)
            params["service_type"] = (serviceTypeSpinner.selectedItem as ServiceType).getIntRepr()
            params["details"] = whatHappened.text.toString()
            params["point_of_contact_name"] = contact.text.toString()
            params["point_of_contact_phone"] = phoneNumber.text.toString()
            params["requested_arrival_time"] = month.selectedItem.toString() + date.selectedItem.toString() + time.selectedItem.toString()

            val request = JsonRequest(false, url, params, responseFuncSendRequest, errorFuncSendRequest, false)
            sendJobRequest(request)
        }
    }

    @JvmField
    val responseFuncSendRequest = Function<Any, Void?> {jsonResponse: Any ->
        startActivity(Intent(this@CreateJobDetails, Dashboard::class.java))
        null
    }

    @JvmField
    val errorFuncSendRequest = Function<String, Void?> {
        errorMsg.text = it
        null
    }

    private fun sendJobRequest(request: JsonRequest) {
        if(validJobRequest(request))
            requestJson(Request.Method.POST, JsonType.OBJECT, request)
        else {
            errorMsg.text = resources.getString(R.string.fill_out_fields)
            scrollView.fullScroll(ScrollView.FOCUS_UP)
        }
    }

    private fun validJobRequest(request: JsonRequest) : Boolean{
        val serviceCompany = request.params?.get("service_company")
        val serviceCategory = request.params?.get("service_category")
        val serviceType = request.params?.get("service_type")
        val details = request.params?.get("details")
        val contactName = request.params?.get("point_of_contact_name")
        val requestedTime = request.params?.get("requested_arrival_time")

        return (serviceCompany != "") && (serviceCategory != "") && (serviceType != "") && (details != "") && (contactName != "") && (requestedTime != "")
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