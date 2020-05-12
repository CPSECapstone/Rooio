package com.rooio.repairs


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.android.volley.Request
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Dashboard : Graph() {
    private lateinit var scheduledNum: TextView
    private lateinit var inProgressNum: TextView
    private lateinit var pendingNum: TextView
    private lateinit var image: ImageView
    private lateinit var address: TextView
    private lateinit var time: TextView
    private lateinit var name: TextView
    private lateinit var repairType: TextView
    private lateinit var clockImage: ImageView
    private lateinit var repairImage: ImageView
    private lateinit var notableJob: Button
    private lateinit var lightingButton: Button
    private lateinit var plumbingButton: Button
    private lateinit var hvacButton: Button
    private lateinit var applianceButton: Button
    private lateinit var jobsLayout: ConstraintLayout
    private lateinit var color: ConstraintLayout
    private lateinit var noJob: TextView
    private lateinit var pendingButton: Button
    private lateinit var scheduledButton: Button
    private lateinit var inProgressButton: Button
    private var imageOn = "off"
    private var jobHistoryMap = HashMap<String, JSONObject>()

    companion object{
        @JvmStatic private var pendingJobs = ArrayList<JSONObject>()
        @JvmStatic private var scheduledJobs = ArrayList<JSONObject>()
        @JvmStatic private var inProgressJobs = ArrayList<JSONObject>()
        @JvmStatic private var archivedJobs = ArrayList<JSONObject>()
        @JvmStatic private var resultSort = ArrayList<JSONObject>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initializePage()
        setNavigationBar()
        setActionBar()
        setGraphSpinners()
        setGraphAdapters(GraphType.DASHBOARD)
        loadJobs()
        createNavigationBar(NavigationType.DASHBOARD)
        jobRequestsClicked()
        jobNumberClicked()
    }

    //initialize variables
    private fun initializePage(){
        notableJob = findViewById(R.id.notableJob)
        repairType = findViewById(R.id.repairType)
        name = findViewById(R.id.name)
        time = findViewById(R.id.timeImage)
        address = findViewById(R.id.address)
        image = findViewById(R.id.image)
        pendingNum = findViewById(R.id.pendingNum)
        scheduledNum = findViewById(R.id.scheduledNum)
        inProgressNum = findViewById(R.id.inProgressNum)
        lightingButton = findViewById(R.id.lightingButton)
        plumbingButton = findViewById(R.id.plumbingButton)
        hvacButton = findViewById(R.id.hvacButton)
        applianceButton = findViewById(R.id.applianceButton)
        clockImage = findViewById(R.id.clockImage)
        repairImage = findViewById(R.id.repairImage)
        jobsLayout = findViewById(R.id.JobsLayout)
        color = findViewById(R.id.color)
        noJob = findViewById(R.id.noJob)
        inProgressButton = findViewById(R.id.inProgressButton)
        scheduledButton = findViewById(R.id.scheduledButton)
        pendingButton = findViewById(R.id.pendingButton)
        name_greeting!!.text = ("Hi, $userName!")
    }

    //LoadJobs sends a Api Get Request to get all Jobs
    private fun loadJobs(){
        jobsLayout.visibility = (View.INVISIBLE)
        val url = url + "jobs/"
        requestJson(Request.Method.GET, JsonType.ARRAY, JsonRequest(false, url,
                null, responseFunc, errorFunc, true))
    }

    //Function for Successfull API Response
    @JvmField
    var responseFunc = Function<Any, Void?> { jsonObj: Any ->
        val responseObj = jsonObj as JSONArray
        populateLists(responseObj)
        listCount()

        null
    }

    //Function for error API Response
    @JvmField
    var errorFunc = Function<String, Void?> { string: String? ->
        name_greeting.text =string

        null
    }

    // loads all the job histories for all pieces of equipment at the service location
    override fun setUpGraph() {
        val params = HashMap<Any?, Any?>()
        params["service_location_id"] = userLocationID

        val url = url + "equipment/"

        val request = JsonRequest(false, url, params, responseLoadAllEquipmentFunc, errorLoadAllEquipmentFunc, true)
        requestJson(Request.Method.GET, JsonType.ARRAY, request)
    }

    @JvmField
    var responseLoadAllEquipmentFunc = Function<Any, Void?> { jsonResponse: Any? ->
        try {
            val jsonArray = jsonResponse as JSONArray
            loadAllJobHistory(jsonArray)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        null
    }

    @JvmField
    var errorLoadAllEquipmentFunc = Function<String, Void?> {
        name_greeting.text = it
        null
    }

    // loading all job histories for each piece of equipment
    private fun loadAllJobHistory(response: JSONArray){
        val params = HashMap<Any?, Any?>()

        for (i in 0 until response.length()){
            val obj = response.getJSONObject(i)
            val equipmentId = obj.getString("id")

            params["service_location_id"] = userLocationID
            params["equipment_id"] = equipmentId

            val url = url + "equipment/$equipmentId/job-history"

            val request = JsonRequest(false, url, params, responseLoadJobHistoryFunc, errorLoadJobHistoryFunc, true)
            requestJson(Request.Method.GET, JsonType.ARRAY, request)
        }
    }

    @JvmField
    var responseLoadJobHistoryFunc = Function<Any, Void?> { jsonResponse: Any? ->
        try {
            val jsonArray = jsonResponse as JSONArray

            // adding each job history to a hash map to ensure there are no duplicates
            for (i in 0 until jsonArray.length()){
                val obj = jsonArray.getJSONObject(i)
                jobHistoryMap[obj.getString("id")] = obj
            }

            combineJobHistory()

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        null
    }

    @JvmField
    var errorLoadJobHistoryFunc = Function<String, Void?> {
        Log.i("graph", it)
        null
    }

    // turning hash map of job histories into a JSONArray for graphing
    private fun combineJobHistory() {
        val jsonArray = JSONArray()
        for(i in jobHistoryMap.keys){
            val obj = jobHistoryMap[i]
            jsonArray.put(obj)
        }

        createGraph(jsonArray, graphJob, graphOption, graphTime)
    }

    //OnClick for New Job Requests to Choose Equipment Page
    private fun jobRequestsClicked(){
        hvacButton.setOnClickListener{
            val intent = Intent(this@Dashboard, ChooseEquipment::class.java)
            intent.putExtra("equipmentType", EquipmentType.HVAC.getIntRepr())
            startActivity(intent)
        }
        lightingButton.setOnClickListener{
            val intent = Intent(this@Dashboard, ChooseEquipment::class.java)
            intent.putExtra("equipmentType", EquipmentType.LIGHTING_AND_ELECTRICAL.getIntRepr())
            startActivity(intent)
        }
        plumbingButton.setOnClickListener{
            val intent = Intent(this@Dashboard, ChooseEquipment::class.java)
            intent.putExtra("equipmentType", EquipmentType.PLUMBING.getIntRepr())
            startActivity(intent)
        }
        applianceButton.setOnClickListener{
            val intent = Intent(this@Dashboard, ChooseEquipment::class.java)
            intent.putExtra("equipmentType", EquipmentType.APPLIANCE.getIntRepr())
            startActivity(intent)
        }
    }

    //Onclick for Job Numbers to Jobs Page
    private fun jobNumberClicked(){
        inProgressButton.setOnClickListener{
            val intent = Intent(this@Dashboard, Jobs::class.java)
            startActivity(intent)
        }
        scheduledButton.setOnClickListener{
            val intent = Intent(this@Dashboard, Jobs::class.java)
            startActivity(intent)
        }
        pendingButton.setOnClickListener{
            val intent = Intent(this@Dashboard, Jobs::class.java)
            startActivity(intent)
        }
    }

    //Clear all lists before populating
    private fun clearLists(){
        pendingJobs.clear()
        scheduledJobs.clear()
        inProgressJobs.clear()
    }

    //populate lists from API JSONArray with jobs
    private fun populateLists(responseObj: JSONArray){
        clearLists()
        for (i in 0 until responseObj.length()) {
            val job = responseObj.getJSONObject(i)
            when(job.getInt("status")) {
                JobType.PENDING.getIntRepr() -> pendingJobs.add(job)
                JobType.SCHEDULED.getIntRepr()-> scheduledJobs.add(job)
                JobType.STARTED.getIntRepr() -> inProgressJobs.add(job)
                JobType.PAUSED.getIntRepr() -> inProgressJobs.add(job)
                JobType.COMPLETED.getIntRepr() -> archivedJobs.add(job)

            }
        }
        notableJobsFill()
    }

    //find number of each job swimlane to display on dashboard
    private fun listCount(){
        pendingNum.text = pendingJobs.size.toString()
        scheduledNum.text = scheduledJobs.size.toString()
        inProgressNum.text = inProgressJobs.size.toString()
    }

    //Filter notable jobs by #1 Pending Jobs, #2 inProgress Jobs, #3 scheduled jobs
    private fun notableJobsFill() {
        if (pendingJobs.size != 0) {
            // Sort PendingJobs and fill in
            jobsLayout.visibility = (View.VISIBLE)
            resultSort = sortJobsList(pendingJobs)
            loadNotable(0, 3)
        }
        else if (inProgressJobs.size != 0){
            //Sort inProgressJobs and fill in
            jobsLayout.visibility = (View.VISIBLE)
            resultSort = sortJobsList(inProgressJobs)
            loadNotable(0, 2)
        }
        else if (scheduledJobs.size != 0){
            //Check if there is a scheduled job today
            var i = 0
            jobsLayout.visibility = (View.VISIBLE)
            resultSort = sortJobsList(scheduledJobs)
            for (index in resultSort.indices){
                if (0 == (timeConvert(resultSort[index].getString("status_time_value")))) {
                    if(index == (resultSort.size - 1) && (i == 0)){

                        jobsLayout.visibility = (View.INVISIBLE)
                        noJob.visibility = (View.VISIBLE)
                        noJob.text = resources.getString(R.string.no_jobs)
                        repairImage.visibility = (View.GONE)
                        color.visibility = (View.INVISIBLE)

                    }
                }
                else{
                    i = 1
                    loadNotable(index, 1)
                }
            }
        }
        else{
            noJob.visibility = (View.VISIBLE)
            noJob.text = resources.getString(R.string.no_jobs)
            repairImage.visibility = (View.GONE)
            color.visibility = (View.INVISIBLE)

        }
    }

    //convert to proper format "yyyy-MM-dd HH:mm:ss"
    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun timeConvert(dateStr: String): Int {
        val eta = convertToNewFormat(dateStr)
        //GET NOW DATE + TIME
        val now = now()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        //If past due then return 0
        return if (sdf.parse(eta)!!.before(sdf.parse(now))) 0 else 1
    }

    @SuppressLint("SimpleDateFormat")
    private fun now(): String {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val c = Calendar.getInstance()
        return df.format(c.time)
    }

    //Load the Notable Jobs with the JsonObject info
    @SuppressLint("SimpleDateFormat")
    private fun loadNotable(index: Int, colorStatus: Int ){
        val locationObj = resultSort[index].getJSONObject("service_location")
        val internalClient = locationObj.getJSONObject("internal_client")

        val equipmentObjList = resultSort[index].getJSONArray("equipment")

        val equipmentObj = equipmentObjList.optJSONObject(0)

        lateinit var category: String
        category = if (equipmentObj != null) {
            equipmentObj.getString("service_category")
        } else { "4" }

        //repair type
        when (category) {
            "4" ->
                //categories.add("General Appliance")
                repairType.text = resources.getString(R.string.generalAppliance)
            "1" ->
                repairType.text = resources.getString(R.string.hvac)
            "2" ->
                repairType.text = resources.getString(R.string.lightingAndElectrical)
            "3" ->
                repairType.text = resources.getString(R.string.plumbing)
        }
        //set the date/time
        if (!resultSort[index].isNull("status_time_value")) {
            val date1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(convertToNewFormat(resultSort[index].getString("status_time_value")))
            timeImage.text = date1!!.toString()

        }
        //address
        address.text = locationObj.getString("physical_address")

        //name
        name.text = internalClient.getString("name")

        //load logo
        val imageVal = internalClient.getString("logo")
        if (imageVal.isNullOrBlank() || imageVal == "null"){
            val viewGroup = findViewById<ViewGroup>(R.id.JobsLayout)
            val sideMover = viewGroup.findViewById<ViewGroup>(R.id.jobMover)
            val boxParams10 = sideMover.layoutParams
            boxParams10.width = 160
            sideMover.layoutParams = boxParams10
            image.visibility = (View.GONE)
            imageOn = "off"
        }
        else{
            imageOn = "on"
            Picasso.with(this)
                    .load(imageVal)
                    .into(image)
        }

        //Navigate to JobDetails after a click of the job
        notableJob.setOnClickListener {
            try {
                val jobId = resultSort[index].getString("id")

                val intent = Intent(this, JobDetails::class.java)
                intent.putExtra("id", jobId.toString())

                this.startActivity(intent)

            } catch (e: JSONException) {
                Log.d("exception", e.toString())
            }
        }

        //Change the color of the job
        when (colorStatus) {
            //Scheduled
            1 -> {
                //Time Based Statuses
                DrawableCompat.setTint(
                        DrawableCompat.wrap(color.background),
                        ContextCompat.getColor(this, R.color.Blue))

            }
            //In Progress Swimlane Status
            2 -> {
                DrawableCompat.setTint(
                        DrawableCompat.wrap(color.background),
                        ContextCompat.getColor(this, R.color.colorPrimary)
                )
            }
            //Pending Swimlane Status
            3 -> {
                DrawableCompat.setTint(
                        DrawableCompat.wrap(color.background),
                        ContextCompat.getColor(this, R.color.Purple)
                )
            }
        }

    }

    //Animate dashboard for navigation bar expand
    override fun animateActivity(boolean: Boolean){
        val viewGroup = findViewById<ViewGroup>(R.id.dashboardConstraint)

        //changing the width of the notableJobs and newJobRequest
        val notableJobs = viewGroup.findViewById<ViewGroup>(R.id.notableJobs)
        val newJobRequest = viewGroup.findViewById<ViewGroup>(R.id.newJobRequest)
        val allJobs = viewGroup.findViewById<ViewGroup>(R.id.allJobs)

        val notableJobsDivider = viewGroup.findViewById<View>(R.id.notableJobsDivider)
        val newJobDivider = viewGroup.findViewById<View>(R.id.newJobDivider)
        val allJobsDivider = viewGroup.findViewById<View>(R.id.allJobsDivider)
        val jobsLayout = viewGroup.findViewById<ConstraintLayout>(R.id.JobsLayout)


        if (boolean) {
            val autoTransition = AutoTransition()
            autoTransition.duration = 900
            TransitionManager.beginDelayedTransition(viewGroup, autoTransition)
        } else {
            TransitionManager.beginDelayedTransition(viewGroup)
        }
        val boxParams1 = notableJobs.layoutParams
        val boxParams2 = newJobRequest.layoutParams
        val boxParams3 = allJobs.layoutParams
        val boxParams4 = notableJobsDivider.layoutParams
        val boxParams5 = newJobDivider.layoutParams
        val boxParams6 = allJobsDivider.layoutParams
        val boxParams7 = jobsLayout.layoutParams


        val widgetWidth = if (boolean) 1004 else 885
        boxParams1.width = widgetWidth
        boxParams2.width = widgetWidth
        boxParams3.width = widgetWidth
        boxParams4.width = widgetWidth
        boxParams5.width = widgetWidth
        boxParams6.width = widgetWidth

        val notableJobsWidth = if (boolean) 948 else 750

        boxParams7.width = notableJobsWidth

        if(imageOn == "off"){
            val textWidth = if (boolean) 330 else 160

            val sideMover = viewGroup.findViewById<ViewGroup>(R.id.jobMover)
            val boxParams10 = sideMover.layoutParams
            boxParams10.width = textWidth
            sideMover.layoutParams = boxParams10
        }

        //calling the transitions
        notableJobs.layoutParams = boxParams1
        newJobRequest.layoutParams = boxParams2
    }
    //Convert to new format
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

     private fun sortJobsList(list: ArrayList<JSONObject>):ArrayList<JSONObject> {

        Collections.sort(list, JSONComparator())

        return list
    }
}
