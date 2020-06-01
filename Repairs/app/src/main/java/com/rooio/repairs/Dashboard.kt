package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.arch.core.util.Function
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.android.volley.Request
import com.github.mikephil.charting.charts.LineChart
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Dashboard : Graph() {
    private lateinit var scheduledNum: TextView
    private lateinit var inProgressNum: TextView
    private lateinit var pendingNum: TextView
    private lateinit var lightingButton: Button
    private lateinit var plumbingButton: Button
    private lateinit var hvacButton: Button
    private lateinit var applianceButton: Button
    private lateinit var noJob: TextView
    private lateinit var pendingButton: Button
    private lateinit var scheduledButton: Button
    private lateinit var inProgressButton: Button
    private lateinit var nameGreeting: TextView
    private lateinit var loadingPanel: ProgressBar
    private var jobHistoryMap = HashMap<String, JSONObject>()

    private lateinit var notableJobView: RecyclerView
    private lateinit var notableJobView2: RecyclerView

    companion object{
        @JvmStatic private var pendingJobs = ArrayList<JobData>()
        @JvmStatic private var scheduledJobs = ArrayList<JobData>()
        @JvmStatic private var inProgressJobs = ArrayList<JobData>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        onResume()
        initializePage()
        setNavigationBar()
        setActionBar()
        setGraphSpinners()
        setGraphAdapters(GraphType.DASHBOARD)
        loadJobs()
        createNavigationBar(NavigationType.DASHBOARD)
        jobRequestsClicked()
        jobNumberClicked()
        onPause()
    }

    //initialize variables
    private fun initializePage(){
        notableJobView = findViewById(R.id.notableJobsView)
        notableJobView2 = findViewById(R.id.notableJobsView2)
        pendingNum = findViewById(R.id.pendingNum)
        scheduledNum = findViewById(R.id.scheduledNum)
        inProgressNum = findViewById(R.id.inProgressNum)
        lightingButton = findViewById(R.id.lightingButton)
        plumbingButton = findViewById(R.id.plumbingButton)
        hvacButton = findViewById(R.id.hvacButton)
        applianceButton = findViewById(R.id.applianceButton)
        noJob = findViewById(R.id.noJob)
        inProgressButton = findViewById(R.id.inProgressButton)
        scheduledButton = findViewById(R.id.scheduledButton)
        pendingButton = findViewById(R.id.pendingButton)
        nameGreeting = findViewById(R.id.nameGreeting)
        loadingPanel = findViewById(R.id.loadingPanel)
        nameGreeting.text = ("Hi, $userName!")
    }

    //LoadJobs sends a Api Get Request to get all Jobs
    private fun loadJobs(){
        val url = url + "jobs/"
        requestJson(Request.Method.GET, JsonType.ARRAY, JsonRequest(false, url,
                null, responseFunc, errorFunc, true))
    }

    //Function for successful API Response
    @JvmField
    var responseFunc = Function<Any, Void?> { jsonObj: Any ->
        loadingPanel.visibility = View.INVISIBLE
        val responseObj = jsonObj as JSONArray
        populateLists(responseObj)
        listCount()

        null
    }

    //Function for error API Response
    @JvmField
    var errorFunc = Function<String, Void?> { string: String? ->
        nameGreeting.text =string
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
    var errorLoadAllEquipmentFunc = Function<String, Void?> {error -> String
        nameGreeting.text = error
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
            val job = JobData(responseObj.getJSONObject(i))
            when(job.status) {
                JobType.PENDING -> pendingJobs.add(job)
                JobType.SCHEDULED -> scheduledJobs.add(job)
                JobType.STARTED -> inProgressJobs.add(job)
                JobType.PAUSED-> inProgressJobs.add(job)
                // else do nothing
                else -> Unit
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
         val resultSort = ArrayList<JobData>()
        pendingJobs = sortJobsList(pendingJobs)
        inProgressJobs = sortJobsList(inProgressJobs)
        scheduledJobs = sortJobsList(scheduledJobs)

        if(pendingJobs.size != 0)
            resultSort.add(pendingJobs[0])
        if(inProgressJobs.size != 0)
            resultSort.add(inProgressJobs[0])
        if(scheduledJobs.size != 0)
            resultSort.add(scheduledJobs[0])

        noJob.visibility = View.GONE
        when (resultSort.size) {
            1 ->  {
                val customAdapter = DashboardCustomAdapter(this, resultSort)
                notableJobView.adapter = customAdapter
                notableJobView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                notableJobView.addItemDecoration(ItemOffsetDecoration(this, R.dimen.item_offset))
                notableJobView2.visibility = View.GONE
            }
            2 -> {
                val customAdapter = DashboardCustomAdapter(this, resultSort)
                notableJobView.adapter = customAdapter
                notableJobView.layoutManager = GridLayoutManager(this, 2)
                notableJobView.addItemDecoration(ItemOffsetDecoration(this, R.dimen.item_offset))
                notableJobView2.visibility = View.GONE
            }
            3 -> {
                val list = ArrayList<JobData>()
                list.add(resultSort[0])
                list.add(resultSort[1])
                val customAdapter = DashboardCustomAdapter(this, list)
                notableJobView.adapter = customAdapter
                notableJobView.layoutManager = GridLayoutManager(this, 2)
                notableJobView.addItemDecoration(ItemOffsetDecoration(this, R.dimen.item_offset))

                val list2 = ArrayList<JobData>()
                list2.add(resultSort[2])
                val customAdapter2 = DashboardCustomAdapter(this, list2)
                notableJobView2.adapter = customAdapter2
                notableJobView2.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            }
            else -> {
                notableJobView.visibility = View.GONE
                notableJobView2.visibility = View.GONE
                noJob.visibility = View.VISIBLE
                noJob.text = resources.getString(R.string.no_jobs)
            }
        }
    }

    //Animate dashboard for navigation bar expand
    override fun animateActivity(boolean: Boolean){
        val viewGroup = findViewById<ViewGroup>(R.id.dashboardConstraint)

        val notableJobs = viewGroup.findViewById<ViewGroup>(R.id.notableJobs)
        val notableJobsDivider = viewGroup.findViewById<View>(R.id.notableJobsDivider)

        val newJobRequest = viewGroup.findViewById<ViewGroup>(R.id.newJobRequest)
        val newJobDivider = viewGroup.findViewById<View>(R.id.newJobDivider)

        val allJobs = viewGroup.findViewById<ViewGroup>(R.id.allJobs)
        val allJobsDivider = viewGroup.findViewById<View>(R.id.allJobsDivider)

        val jobAnalytics = viewGroup.findViewById<ViewGroup>(R.id.jobAnalytics)
        val lineChart = viewGroup.findViewById<LineChart>(R.id.lineChart)


        if (boolean) {
            val autoTransition = AutoTransition()
            autoTransition.duration = 900
            TransitionManager.beginDelayedTransition(viewGroup, autoTransition)
        } else {
            TransitionManager.beginDelayedTransition(viewGroup)
        }

        val notableJobsParams = notableJobs.layoutParams
        val notableJobsDividerParams = notableJobsDivider.layoutParams

        val newJobRequestParams = newJobRequest.layoutParams
        val newJobRequestDividerParams = newJobDivider.layoutParams

        val allJobsParams = allJobs.layoutParams
        val allJobsDividerParams = allJobsDivider.layoutParams

        val jobAnalyticsParams = jobAnalytics.layoutParams
        val graphParams = lineChart.layoutParams


        val widgetWidth = if (boolean) 1000 else 900
        notableJobsParams.width = widgetWidth
        notableJobsDividerParams.width = widgetWidth

        newJobRequestParams.width = widgetWidth
        allJobsParams.width = widgetWidth
        newJobRequestDividerParams.width = widgetWidth
        allJobsDividerParams.width = widgetWidth
        jobAnalyticsParams.width = widgetWidth

        val analyticsWidth = if (boolean) 905 else 805
        graphParams.width = analyticsWidth

        //calling the transitions
        notableJobs.layoutParams = notableJobsParams
        newJobRequest.layoutParams = newJobRequestParams
    }

     private fun sortJobsList(list: ArrayList<JobData>):ArrayList<JobData> {
         list.sortWith(compareBy { it.statusTimeValue })
         return list
    }
}