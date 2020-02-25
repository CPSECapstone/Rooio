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
import java.util.ArrayList

class Jobs : NavigationBar() {
    private var id1: ListView? = null
    private var id2: ListView? = null
    private var id3: ListView? = null
    private var completedButton: Button? = null


    val statuses = arrayListOf<String>()

    private var pendingConstraint: ConstraintLayout? = null
    private var scheduledConstraint: ConstraintLayout? = null
    private var inProgressConstraint: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs)

        //sets the navigation bar onto the page
        id1 = findViewById<View>(R.id.id1) as ListView
        id2 = findViewById<View>(R.id.id2) as ListView
        id3 = findViewById<View>(R.id.id3) as ListView
        completedButton = findViewById(R.id.button)
        pendingConstraint = findViewById<View>(R.id.pendingConstraint) as ConstraintLayout
        scheduledConstraint = findViewById<View>(R.id.scheduledConstraint) as ConstraintLayout
        inProgressConstraint = findViewById<View>(R.id.inProgressConstraint) as ConstraintLayout

        val nav_inflater = layoutInflater
        val tmpView = nav_inflater.inflate(R.layout.activity_navigation_bar, null)

        window.addContentView(tmpView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        //sets the action bar onto the page

        val actionbar_inflater = layoutInflater
        val poopView = actionbar_inflater.inflate(R.layout.action_bar, null)
        window.addContentView(poopView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        supportActionBar!!.elevation = 0.0f


        createNavigationBar("jobs")


        addElements()
        onClick()

    }

    private fun onClick() {
        completedButton!!.setOnClickListener { startActivity(Intent(this@Jobs, JobsArchived::class.java)) }
    }

    private fun addElements() {

        pendingJobs.clear()
        scheduledJobs.clear()
        inProgressJobs.clear()


//        for (i in 0 until response.length()) {
//
//            //val job = response.getJSONObject(i)
//            //Have if statement to determine which kind of job
//
//            val physicalAddress = job.getString("physical_address")
//            LocationLogin.addressList.add(physicalAddress)
//            LocationLogin.locationIds.add(job.getString("id"))
//
//            //Creating the height for each row
//            if (i > 0) {
//                val params = pendingConstraint!!.layoutParams
//                params.height += 170
//                pendingConstraint!!.layoutParams = params
//                val size = id1!!.layoutParams
//                size.height += 170
//            }
//            //HAVE A NEW ^^ FOR EACH KIND OF ARRAY
//        }
        //A FAKE DATA SET
        val pending = JobsData("pending","San Luis Taqueria", "http://www.lunaredslo.com/images/luna_red_logo.png", "Plumbing", "46017 Paseo Padre", "8:30 PM")
        val pending2 = JobsData("in","San Luis Taqueria2", "http://www.lunaredslo.com/images/luna_red_logo.png", "Plumbing", "46017 Paseo Padre", "8:30 PM")
        val pending3 = JobsData("pending","San Luis Taqueria3", "http://www.lunaredslo.com/images/luna_red_logo.png", "Plumbing", "46017 Paseo Padre", "8:30 PM")
        val pending4 = JobsData("accepted","San Luis Taqueria4", "http://www.lunaredslo.com/images/luna_red_logo.png", "Plumbing", "46017 Paseo Padre", "8:30 PM")

        val arrayList1 = ArrayList<JobsData>(2)
        arrayList1.add(pending)
        arrayList1.add(pending2)
        arrayList1.add(pending3)
        arrayList1.add(pending4)


        //Divide each job by swimlane and set sizes
        for (item in arrayList1) {

            if (item.status == "pending"){
                pendingJobs.add(item)
                sizes(item.status)
            }
            else if(item.status == "scheduled" || item.status == "accepted"){
                scheduledJobs.add(item)
                sizes(item.status)
            }
            else{
                inProgressJobs.add(item)
                sizes(item.status)
            }
        }

        //Sets the Adapter of Jobs_List_Row
        val customAdapter = JobsCustomerAdapter(this, pendingJobs)
        if (pendingJobs.size != 0) id1!!.adapter = customAdapter

        val customAdapter1 = JobsCustomerAdapter(this, scheduledJobs)
        if (scheduledJobs.size != 0) id2!!.adapter = customAdapter1

        val customAdapter2 = JobsCustomerAdapter(this, inProgressJobs)
        if (inProgressJobs.size != 0) id3!!.adapter = customAdapter2

    }

    companion object {
        //TextView test;
        var pendingJobs = ArrayList<JobsData>()
        var scheduledJobs = ArrayList<JobsData>()
        var inProgressJobs = ArrayList<JobsData>()

    }

    //Set the sizes for each individual block
    private fun sizes(str: String) {
        var value = 0
        if (str in statuses) {
            value = 200
        } else {
            statuses.add(str)
            value = 240
        }
        set_size(str, value)
    }

    //Set the sizes
    private fun set_size(str: String, value: Int){

        if (str == "pending"){
            val params = pendingConstraint!!.layoutParams
            params.height += value
            pendingConstraint!!.layoutParams = params
            val size = id1!!.layoutParams
            size.height += value
            id1!!.layoutParams = size
        }
        else if (str == "scheduled" ||str == "accepted"){
            val params = scheduledConstraint!!.layoutParams
            params.height += value
            scheduledConstraint!!.layoutParams = params
            val size = id2!!.layoutParams
            size.height += value
            id2!!.layoutParams = size
        }
        else {
            val params = inProgressConstraint!!.layoutParams
            params.height += value
            inProgressConstraint!!.layoutParams = params
            val size = id3!!.layoutParams
            size.height += value
            id3!!.layoutParams = size
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


        val p2 = if (boolean) 480 else 454
        boxParams1.width = p2
        boxParams2.width = p2
        boxParams3.width = p2
        boxParams4.width = p2
        boxParams5.width = p2
        boxParams6.width = p2
//


        //calling the transitions
        scheduled.layoutParams = boxParams1
        pending.layoutParams = boxParams2
        inProgress.layoutParams = boxParams3
        pending.layoutParams = boxParams4
        scheduled.layoutParams = boxParams5
        inProgress.layoutParams = boxParams6

//

        val p3 = if (boolean) 85 else 20

        boxParams10.width = p3

        sideMover.layoutParams = boxParams10
    }
}