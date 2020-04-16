package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import com.android.volley.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class Jobs : NavigationBar() {

    private lateinit var pendingList: ListView
    private lateinit var scheduledList: ListView
    private lateinit var inProgressList: ListView
    private lateinit var completedButton: Button

    val statuses = arrayListOf<String>()

    private var pendingConstraint: ConstraintLayout? = null
    private var scheduledConstraint: ConstraintLayout? = null
    private var inProgressConstraint: ConstraintLayout? = null

    companion object{
        @JvmStatic private var pendingJobs = ArrayList<JSONObject>()
        @JvmStatic private var scheduledJobs = ArrayList<JSONObject>()
        @JvmStatic private var inProgressJobs = ArrayList<JSONObject>()
        @JvmStatic private var startedJobs = ArrayList<JSONObject>()
        @JvmStatic private var pausedJobs = ArrayList<JSONObject>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs)


        pendingList = findViewById<View>(R.id.pendingList) as ListView
        scheduledList = findViewById<View>(R.id.scheduledList) as ListView
        inProgressList = findViewById<View>(R.id.inProgressList) as ListView
        completedButton = findViewById(R.id.button)
        pendingConstraint = findViewById<View>(R.id.pendingConstraint) as ConstraintLayout
        scheduledConstraint = findViewById<View>(R.id.scheduledConstraint) as ConstraintLayout
        inProgressConstraint = findViewById<View>(R.id.inProgressConstraint) as ConstraintLayout

        //Set navigation bar function call
        setNavigationBar()
        setActionBar()




        createNavigationBar(NavigationType.JOBS)

        onClick()
        clearLists()
        loadJobs()

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
        val Pending = "?status=0"
        val url = "service-locations/$userLocationID/jobs/$Pending"
        requestJson(Request.Method.GET, JsonType.ARRAY, JsonRequest(false, url,
                null, responseFunc, errorFunc, true))
    }

    private fun loadScheduledJobs(){
        val Scheduled = "?status=2"
        val url = "service-locations/$userLocationID/jobs/$Scheduled"
        requestJson(Request.Method.GET, JsonType.ARRAY, JsonRequest(false, url,
                null, responseFunc, errorFunc, true))
    }

    private fun loadInProgressJobs(){
        val InProgress = "?status=5&status=6"
        val url = "service-locations/$userLocationID/jobs/$InProgress"
        requestJson(Request.Method.GET, JsonType.ARRAY, JsonRequest(false, url,
                null, responseFunc, errorFunc, true))
    }


    private fun clearLists(){
        pendingJobs.clear()
        scheduledJobs.clear()
        inProgressJobs.clear()
        startedJobs.clear()
        pausedJobs.clear()

    }

    private fun populateLists(responseObj: JSONArray){
        for (i in 0 until responseObj.length()) {
            val job = responseObj.getJSONObject(i)

            if (job.getInt("status") == 0){
                pendingJobs.add(job)
                sizes("pending")
            }
            else if(job.getInt("status") == 2){
                scheduledJobs.add(job)
                sizes("scheduled")
            }
            else if(job.getInt("status") == 5){
                startedJobs.add(job)
                sizes("started")
            }
            else if(job.getInt("status") == 6){
                pausedJobs.add(job)
                sizes("paused")
            }


        }

        for(i in 0 until startedJobs.size){
            inProgressJobs.add(startedJobs[i])
        }
        for(i in 0 until pausedJobs.size){
            inProgressJobs.add(pausedJobs[i])
        }

        val customAdapter = JobsCustomAdapter(this, pendingJobs)
        if (pendingJobs.size != 0) pendingList.adapter = customAdapter

        val customAdapter1 = JobsCustomAdapter(this, scheduledJobs)
        if (scheduledJobs.size != 0) scheduledList.adapter = customAdapter1

        val customAdapter2 = JobsCustomAdapter(this, inProgressJobs)
        if (inProgressJobs.size != 0) inProgressList.adapter = customAdapter2

    }

    @JvmField
    var responseFunc = Function<Any, Void?> { jsonObj: Any ->
        val responseObj = jsonObj as JSONArray
        populateLists(responseObj)

        null
    }
    @JvmField
    var errorFunc = Function<String, Void?> { string: String? ->

        null
    }

    //Set the sizes for each individual block
    private fun sizes(str: String) {
        var value = 0
        if (str in statuses) {
            value = 200
        } else {
            statuses.add(str)
            value = 260
        }
        set_size(str, value)
    }

    //Set the sizes
    private fun set_size(str: String, value: Int){

        if (str == "pending"){
            val params = pendingConstraint!!.layoutParams
            params.height += value
            pendingConstraint!!.layoutParams = params
            val size = pendingList!!.layoutParams
            size.height += value
            pendingList!!.layoutParams = size
        }
        else if (str == "scheduled" ){
            val params = scheduledConstraint!!.layoutParams
            params.height += value
            scheduledConstraint!!.layoutParams = params
            val size = scheduledList!!.layoutParams
            size.height += value
            scheduledList!!.layoutParams = size
        }
        else{
            val params = inProgressConstraint!!.layoutParams
            params.height += value
            inProgressConstraint!!.layoutParams = params
            val size = inProgressList!!.layoutParams
            size.height += value
            inProgressList!!.layoutParams = size
        }

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