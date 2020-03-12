package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import com.android.volley.Request
import org.json.JSONArray
import org.json.JSONObject


class Jobs : NavigationBar() {
    private lateinit var pendingList: ListView
    private lateinit var scheduledList: ListView
    private lateinit var inProgressList: ListView
    private lateinit var completedButton: Button
    private lateinit var errorMsg: TextView

    private val statuses = ArrayList<String>()

    private lateinit var pendingConstraint: ConstraintLayout
    private lateinit var scheduledConstraint: ConstraintLayout
    private lateinit var inProgressConstraint: ConstraintLayout


    private var pendingJobs = ArrayList<JSONObject>()
    private var scheduledJobs = ArrayList<JSONObject>()
    private var inProgressJobs = ArrayList<JSONObject>()
    private var startedJobs = ArrayList<JSONObject>()
    private var pausedJobs = ArrayList<JSONObject>()

    private val url = "service-locations/$userLocationID/jobs/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initVariables()
        setNavigationBar()
        setActionBar()
        createNavigationBar(NavigationType.JOBS)
        onClick()
        loadJobs()
    }

    private fun initVariables(){
        setContentView(R.layout.activity_jobs)

        //sets the navigation bar onto the page
        pendingList = findViewById(R.id.pendingList)
        scheduledList = findViewById(R.id.scheduledList)
        inProgressList = findViewById(R.id.inProgressList)
        completedButton = findViewById(R.id.button)
        pendingConstraint = findViewById(R.id.pendingConstraint)
        scheduledConstraint = findViewById(R.id.scheduledConstraint)
        inProgressConstraint = findViewById(R.id.inProgressConstraint)
        errorMsg = findViewById(R.id.errorMessage)
    }

    //Transition to completed items
    private fun onClick() {
        completedButton.setOnClickListener { startActivity(Intent(this@Jobs, JobsArchived::class.java)) }
    }

    private fun loadJobs(){
        loadPendingJobs()
        loadScheduledJobs()
        loadInProgressJobs()
    }

    private fun loadPendingJobs(){
        val pending = "?status=0"
        requestJson(Request.Method.GET, JsonType.ARRAY, JsonRequest(false, url + pending,
                null, responseFunc, errorFunc, true))
    }

    private fun loadScheduledJobs(){
        val scheduled = "?status=2"
        requestJson(Request.Method.GET, JsonType.ARRAY, JsonRequest(false, url + scheduled,
                null, responseFunc, errorFunc, true))
    }

    private fun loadInProgressJobs(){
        val inProgress = "?status=5&status=6"
        requestJson(Request.Method.GET, JsonType.ARRAY, JsonRequest(false, url + inProgress,
                null, responseFunc, errorFunc, true))
    }

    private fun populateLists(responseObj: JSONArray){
        for (i in 0 until responseObj.length()) {
            val job = responseObj.getJSONObject(i)

            if (job.getInt("status") == StatusType.Pending.getInt()){
                pendingJobs.add(job)
                statusesSizing("pending")
            }
            else if(job.getInt("status") == StatusType.Accepted.getInt()){
                scheduledJobs.add(job)
                statusesSizing("scheduled")
            }
            else if(job.getInt("status") == StatusType.Started.getInt()){
                startedJobs.add(job)
                statusesSizing("started")
            }
            else if(job.getInt("status") == StatusType.Paused.getInt()){
                pausedJobs.add(job)
                statusesSizing("paused")
            }
        }

        for(i in 0 until startedJobs.size){
            inProgressJobs.add(startedJobs[i])
        }

        for(i in 0 until pausedJobs.size){
            inProgressJobs.add(pausedJobs[i])
        }

        val pendingJobsCustomerAdapter = JobsCustomerAdapter(this, pendingJobs)
        if (pendingJobs.size != 0) pendingList.adapter = pendingJobsCustomerAdapter

        val scheduledJobsCustomerAdapter = JobsCustomerAdapter(this, scheduledJobs)
        if (scheduledJobs.size != 0) scheduledList.adapter = scheduledJobsCustomerAdapter

        val inProvidersCustomAdapter = JobsCustomerAdapter(this, inProgressJobs)
        if (inProgressJobs.size != 0) inProgressList.adapter = inProvidersCustomAdapter
    }


    @JvmField
    var responseFunc = Function<Any, Void?> { jsonObj: Any ->
        val responseObj = jsonObj as JSONArray
        populateLists(responseObj)

        null
    }
    @JvmField
    var errorFunc = Function<String, Void?> { string: String? ->
        errorMsg.text = string
        null
    }


    //Set the statusesSizing for each individual block
    private fun statusesSizing(str: String) {
        var value: Int
        if (str in statuses) {
            value = 200
        } else {
            statuses.add(str)
            value = 260
        }
        setStatusesSizing(str, value)
    }

    //Set the statusesSizing
    private fun setStatusesSizing(str: String, value: Int){

        if (str == "pending"){
            setLayoutParams(value, pendingConstraint, pendingList)
        }
        else if (str == "scheduled" ){
            setLayoutParams(value, scheduledConstraint, scheduledList)
//            val params = scheduledConstraint.layoutParams
//            params.height += value
//            scheduledConstraint.layoutParams = params
//            val size = scheduledList.layoutParams
//            size.height += value
//            scheduledList.layoutParams = size
        }
        else{
            setLayoutParams(value, inProgressConstraint, inProgressList)
        }

    }

    private fun setLayoutParams(value: Int, constraintLayout: ConstraintLayout, listView: ListView){
        val params = constraintLayout.layoutParams
        params.height += value
        constraintLayout.layoutParams = params
        val size = listView.layoutParams
        size.height += value
        listView.layoutParams = size
    }


    //Shifting the layout in response to navBar position
    override fun animateActivity(boolean: Boolean){
        val viewGroup = findViewById<ViewGroup>(R.id.jobsConstraint)

        //changing the width of the notableJobs and newJobRequest
        TransitionManager.beginDelayedTransition(viewGroup)

        val pending = viewGroup.findViewById<ViewGroup>(R.id.pendingConstraint)
        val scheduled = viewGroup.findViewById<ViewGroup>(R.id.scheduledConstraint)
        val inProgress = viewGroup.findViewById<ViewGroup>(R.id.inProgressConstraint)

        val sideMover = viewGroup.findViewById<ViewGroup>(R.id.sideMover)

        val pendingTitle = viewGroup.findViewById<ViewGroup>(R.id.pendingTitleConstraint)
        val scheduledTitle = viewGroup.findViewById<ViewGroup>(R.id.scheduledTitleConstraint)
        val inProgressTitle = viewGroup.findViewById<ViewGroup>(R.id.inProgressTitleConstraint)

        val boxParams1 = scheduledTitle.layoutParams
        val boxParams2 = pendingTitle.layoutParams
        val boxParams3 = inProgressTitle.layoutParams

        val boxParams4 = pending.layoutParams
        val boxParams5 = scheduled.layoutParams
        val boxParams6 = inProgress.layoutParams

        val boxParams10 = sideMover.layoutParams

        val p2 = if (boolean) 478 else 454
        boxParams1.width = p2
        boxParams2.width = p2
        boxParams3.width = p2
        boxParams4.width = p2
        boxParams5.width = p2
        boxParams6.width = p2

        val p3 = if (boolean) 85 else 20

        boxParams10.width = p3

        //calling the transitions
        scheduledTitle.layoutParams = boxParams1
        pendingTitle.layoutParams = boxParams2
        inProgressTitle.layoutParams = boxParams3
        pending.layoutParams = boxParams4
        scheduled.layoutParams = boxParams5
        inProgress.layoutParams = boxParams6
        sideMover.layoutParams = boxParams10
    }

}