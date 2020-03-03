package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList
import androidx.arch.core.util.Function


class Jobs : NavigationBar() {
    private var pendingList: ListView? = null
    private var scheduledList: ListView? = null
    private var inProgressList: ListView? = null
    private var completedButton: Button? = null


    val statuses = arrayListOf<String>()

    private var pendingConstraint: ConstraintLayout? = null
    private var scheduledConstraint: ConstraintLayout? = null
    private var inProgressConstraint: ConstraintLayout? = null

    companion object{
        @JvmStatic private var pendingJobs = ArrayList<JSONObject>()
        @JvmStatic private var scheduledJobs = ArrayList<JSONObject>()
        @JvmStatic private var inProgressJobs = ArrayList<JSONObject>()
        @JvmStatic private var archivedJobs = ArrayList<JSONObject>()
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

        //sets the action bar onto the page
        val actionbar_inflater = layoutInflater
        val actionBar = actionbar_inflater.inflate(R.layout.action_bar, null)
        window.addContentView(actionBar,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        supportActionBar!!.elevation = 0.0f

        createNavigationBar("jobs")

        onClick()
        loadJobs()

    }

    //Transition to completed items
    private fun onClick() {
        completedButton!!.setOnClickListener { startActivity(Intent(this@Jobs, JobsArchived::class.java)) }
    }

    //Load jobs in
    private fun loadJobs(){
        val url = BaseUrl + "service-locations/$userLocationID/jobs/"
        requestGetJsonArray(JsonRequest(false, url, null, responseFunc, errorFunc, true))
    }

    private fun clearLists(){
        pendingJobs.clear()
        scheduledJobs.clear()
        inProgressJobs.clear()
        archivedJobs.clear()
    }

    private fun populateLists(responseObj: JSONArray){
        clearLists()
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
            else if(job.getInt("status") == 5 || job.getInt("status") == 6 ){
                inProgressJobs.add(job)
                sizes("inProgress")
            }
            else{
                archivedJobs.add(job)

            }
        }

        val customAdapter = JobsCustomerAdapter(this, pendingJobs)
        if (pendingJobs.size != 0) pendingList!!.adapter = customAdapter

        val customAdapter1 = JobsCustomerAdapter(this, scheduledJobs)
        if (scheduledJobs.size != 0) scheduledList!!.adapter = customAdapter1

        val customAdapter2 = JobsCustomerAdapter(this, inProgressJobs)
        if (inProgressJobs.size != 0) inProgressList!!.adapter = customAdapter2

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
            value = 250
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
        else if (str == "scheduled" ||str == "accepted"){
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

    private fun setNavigationBar() {
        //sets the navigation bar onto the page
        val nav_inflater = layoutInflater
        val tmpView = nav_inflater.inflate(R.layout.activity_navigation_bar, null)

        window.addContentView(tmpView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }
}