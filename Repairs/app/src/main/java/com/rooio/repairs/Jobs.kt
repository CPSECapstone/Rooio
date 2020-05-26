package com.rooio.repairs

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.AutoTransition
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
    private lateinit var errorMessage: TextView

    private lateinit var pendingConstraint: ConstraintLayout
    private lateinit var scheduledConstraint: ConstraintLayout
    private lateinit var inProgressConstraint: ConstraintLayout
    private lateinit var pendingText: TextView
    private lateinit var scheduledText: TextView
    private lateinit var inProgressText: TextView
    private lateinit var loadingPanel: ProgressBar

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
        pendingText = findViewById(R.id.pendingText)
        pendingList = findViewById(R.id.pendingList)
        scheduledList = findViewById(R.id.scheduledList)
        scheduledText = findViewById(R.id.scheduledText)
        inProgressList = findViewById(R.id.inProgressList)
        inProgressText = findViewById(R.id.inProgressText)
        pendingConstraint = findViewById(R.id.pendingConstraint)
        scheduledConstraint = findViewById(R.id.scheduledConstraint)
        inProgressConstraint = findViewById(R.id.inProgressConstraint)
        loadingPanel = findViewById(R.id.loadingPanel)

        //Set navigation bar function call
        setNavigationBar()
        setActionBar()
        createNavigationBar(NavigationType.JOBS)

        clearLists()
        loadJobs()

    }

    //Load Jobs in from API
    private fun loadJobs(){
        loadPendingJobs()
        loadScheduledJobs()
        loadInProgressJobs()
    }

    private fun loadPendingJobs(){
        val pending = "?status=0"
        val url = "service-locations/$userLocationID/jobs/$pending"
        requestJson(Request.Method.GET, JsonType.ARRAY, JsonRequest(false, url,
                null, responseFunc, errorFunc, true))
    }

    private fun loadScheduledJobs(){
        val scheduled = "?status=2"
        val url = "service-locations/$userLocationID/jobs/$scheduled"
        requestJson(Request.Method.GET, JsonType.ARRAY, JsonRequest(false, url,
                null, responseFunc, errorFunc, true))
    }

    private fun loadInProgressJobs(){
        val inProgress = "?status=5&status=6"
        val url = "service-locations/$userLocationID/jobs/$inProgress"
        requestJson(Request.Method.GET, JsonType.ARRAY, JsonRequest(false, url,
                null, responseFunc, errorFunc, true))
    }


    //Clear the swim lanes
    private fun clearLists(){
        pendingJobs.clear()
        scheduledJobs.clear()
        inProgressJobs.clear()
        startedJobs.clear()
        pausedJobs.clear()

    }

    //Push jobs into designated swim lanes
    private fun populateLists(responseObj: JSONArray){
        for (i in 0 until responseObj.length()) {
            val job = responseObj.getJSONObject(i)
            when (job.getInt("status")){
                0 ->{
                    pendingJobs.add(job)
                    if (i > 0) setSize(pendingConstraint)
                    setSize(pendingList)
                }
                2 ->
                {
                    scheduledJobs.add(job)
                    if (i > 0) setSize(scheduledConstraint)
                    setSize(scheduledList)
                }
                5 ->
                {
                    startedJobs.add(job)
                    if (i > 0) setSize(inProgressConstraint)
                    setSize(inProgressList)
                }
                6 -> {
                    pausedJobs.add(job)
                    if (i > 0) setSize(inProgressConstraint)
                    setSize(inProgressList)
                }
            }

        }

        for(i in 0 until startedJobs.size){
            inProgressJobs.add(startedJobs[i])
        }
        for(i in 0 until pausedJobs.size){
            inProgressJobs.add(pausedJobs[i])
        }

        val customAdapter = JobsCustomAdapter(this, pendingJobs)
        if (pendingJobs.size != 0) {
            pendingList.adapter = customAdapter
            pendingText.visibility = View.INVISIBLE
        }
        else setEmptyText(JobType.PENDING)

        val customAdapter1 = JobsCustomAdapter(this, scheduledJobs)
        if (scheduledJobs.size != 0){
            scheduledList.adapter = customAdapter1
            scheduledText.visibility = View.INVISIBLE
        }
        else setEmptyText(JobType.SCHEDULED)

        val customAdapter2 = JobsCustomAdapter(this, inProgressJobs)
        if (inProgressJobs.size != 0) {
            inProgressList.adapter = customAdapter2
            inProgressText.visibility = View.INVISIBLE
        }
        else setEmptyText(JobType.IN_PROGRESS)
    }

    //API response functions
    @JvmField
    var responseFunc = Function<Any, Void?> { jsonObj: Any ->
        loadingPanel.visibility = View.INVISIBLE
        val responseObj = jsonObj as JSONArray
        populateLists(responseObj)
        null
    }
    @JvmField
    var errorFunc = Function<String, Void?> { error: String? ->
        errorMessage.text = error
        null
    }

    //Sets the empty list text
    private fun setEmptyText(type: JobType) {
        when (type) {
            JobType.PENDING -> pendingText.visibility = View.VISIBLE
            JobType.SCHEDULED ->  scheduledText.visibility = View.VISIBLE
            JobType.IN_PROGRESS ->  inProgressText.visibility = View.VISIBLE
        }
    }


    //Set the sizes
    private fun setSize(constraint: ViewGroup){
        val value = 172
        val params = constraint.layoutParams
        params.height += value
        constraint.layoutParams = params
    }

    //Shifting the layout in response to navBar position
    override fun animateActivity(boolean: Boolean){
        val viewGroup = findViewById<ViewGroup>(R.id.jobsConstraint)

        //changing the width of the notableJobs and newJobRequest
        if (boolean) {
            val autoTransition = AutoTransition()
            autoTransition.duration = 1000
            TransitionManager.beginDelayedTransition(viewGroup, autoTransition)
        } else {
            TransitionManager.beginDelayedTransition(viewGroup)
        }

        val pending = viewGroup.findViewById<ViewGroup>(R.id.pendingConstraint)
        val scheduled = viewGroup.findViewById<ViewGroup>(R.id.scheduledConstraint)
        val inProgress = viewGroup.findViewById<ViewGroup>(R.id.inProgressConstraint)

        val amount = if (boolean) -125f else 0f
        var animation = ObjectAnimator.ofFloat(pending, "translationX", amount)
        if (boolean) animation.duration = 900 else animation.duration = 300
        animation.start()
        animation = ObjectAnimator.ofFloat(scheduled, "translationX", amount)
        if (boolean) animation.duration = 900 else animation.duration = 300
        animation.start()
        animation = ObjectAnimator.ofFloat(inProgress, "translationX", amount)
        if (boolean) animation.duration = 900 else animation.duration = 300
        animation.start()
    }

}
