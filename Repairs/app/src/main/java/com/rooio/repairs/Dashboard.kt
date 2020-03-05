package com.rooio.repairs

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.widget.Button
import androidx.transition.TransitionManager
import java.util.ArrayList
import android.widget.ImageView
import android.widget.TextView
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.json.JSONArray
import org.json.JSONObject

class Dashboard : NavigationBar() {

    private lateinit var scheduledNum: TextView
    private lateinit var inProgressNum: TextView
    private lateinit var pendingNum: TextView
    private lateinit var image: ImageView
    private lateinit var address: TextView
    private lateinit var time: TextView
    private lateinit var name: TextView
    private lateinit var repairType: TextView

    private lateinit var lightingButton: Button
    private lateinit var plumbingButton: Button
    private lateinit var hvacButton: Button
    private lateinit var applianceButton: Button

    companion object{
        @JvmStatic private var pendingJobs = ArrayList<JSONObject>()
        @JvmStatic private var scheduledJobs = ArrayList<JSONObject>()
        @JvmStatic private var inProgressJobs = ArrayList<JSONObject>()
        @JvmStatic private var archivedJobs = ArrayList<JSONObject>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        //initialize variables
        repairType = findViewById<View>(R.id.repairType) as TextView
        name = findViewById<View>(R.id.name) as TextView
        time = findViewById<View>(R.id.timeImage) as TextView
        address = findViewById<View>(R.id.address) as TextView
        image = findViewById<View>(R.id.image) as ImageView
        pendingNum = findViewById(R.id.pendingNum)
        scheduledNum = findViewById(R.id.scheduledNum)
        inProgressNum = findViewById(R.id.inProgressNum)
        lightingButton = findViewById(R.id.lightingButton)
        plumbingButton = findViewById(R.id.plumbingButton)
        hvacButton = findViewById(R.id.hvacButton)
        applianceButton = findViewById(R.id.applianceButton)

        setNavigationBar()
        setActionBar()
        populate_test()
        loadJobs()
        createNavigationBar("dashboard")
        jobRequestsClicked()
    }

    private fun setNavigationBar() {
        //sets the navigation bar onto the page
        val nav_inflater = layoutInflater
        val tmpView = nav_inflater.inflate(R.layout.activity_navigation_bar, null)

        window.addContentView(tmpView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    private fun setActionBar() {
        supportActionBar!!.elevation = 0.0f
    }

    private fun loadJobs(){
        val url = BaseUrl + "service-locations/$userLocationID/jobs/"
        requestGetJsonArray(JsonRequest(false, url, null, responseFunc, errorFunc, true))
    }

    //OnClick for Appliances
    private fun jobRequestsClicked(){
        hvacButton.setOnClickListener{
            val intent = Intent(this@Dashboard, ChooseEquipment::class.java)
            intent.putExtra("equipmentType", 1)
            startActivity(intent);
        }
        plumbingButton.setOnClickListener{
            val intent = Intent(this@Dashboard, ChooseEquipment::class.java)
            intent.putExtra("equipmentType", 2)
            startActivity(intent);
        }
        lightingButton.setOnClickListener{
            val intent = Intent(this@Dashboard, ChooseEquipment::class.java)
            intent.putExtra("equipmentType", 3)
            startActivity(intent);
        }
        applianceButton.setOnClickListener{
            val intent = Intent(this@Dashboard, ChooseEquipment::class.java)
            intent.putExtra("equipmentType", 4)
            startActivity(intent);
        }
    }

    //Placeholder for Notable Jobs
    fun populate_test(){

        val nameVal = "Luna Red"
        val imageVal = "http://www.lunaredslo.com/images/luna_red_logo.png"
        val repairTypeVal = "Hot Equipment Repair"
        val addressVal = "1023 Chorro St. San Luis Obispo"
        val timeVal = "Oct 4, '19 6:30pm PDT"

        repairType!!.text = repairTypeVal
        name!!.text = nameVal
        address!!.text = addressVal
        time!!.text = timeVal
        name_greeting!!.text = "Hi, " + userName + "!"

        Picasso.with(this)
                .load(imageVal)
                .into(image)
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
            when(job.getInt("status")) {
                0 -> pendingJobs.add(job)
                2 -> scheduledJobs.add(job)
                5 -> inProgressJobs.add(job)
                //6 -> inProgressJobs.add(job)
                3 -> archivedJobs.add(job)
            }
        }
    }

    private fun listCount(){
        pendingNum!!.text = pendingJobs.size.toString()
        scheduledNum!!.text = scheduledJobs.size.toString()
        inProgressNum!!.text = inProgressJobs.size.toString()
    }

    @JvmField
    var responseFunc = Function<Any, Void?> { jsonObj: Any ->
        val responseObj = jsonObj as JSONArray
        populateLists(responseObj)
        listCount()

        null
    }
    @JvmField
    var errorFunc = Function<String, Void?> { string: String? ->
        null
    }

    //Transitions for navigation bar open/close
    override fun animateActivity(boolean: Boolean){
        val viewGroup = findViewById<ViewGroup>(R.id.dashboardConstraint)
        val notableJobs = viewGroup.findViewById<ViewGroup>(R.id.notableJobs)
        val newJobRequest = viewGroup.findViewById<ViewGroup>(R.id.newJobRequest)
        val allJobs = viewGroup.findViewById<ViewGroup>(R.id.allJobs)
        val notableJobsDivider = viewGroup.findViewById<View>(R.id.notableJobsDivider)
        val newJobDivider = viewGroup.findViewById<View>(R.id.newJobDivider)
        val allJobsDivider = viewGroup.findViewById<View>(R.id.allJobsDivider)
        val jobsLayout = viewGroup.findViewById<ConstraintLayout>(R.id.JobsLayout)

        TransitionManager.beginDelayedTransition(viewGroup)
        val notableJobsParams = notableJobs.layoutParams
        val newJobsParams = newJobRequest.layoutParams
        val allJobsParams = allJobs.layoutParams
        val notableJobsDividerParams = notableJobsDivider.layoutParams
        val newJobsDividerParams = newJobDivider.layoutParams
        val allJobsDividerParams = allJobsDivider.layoutParams
        val jobParams = jobsLayout.layoutParams

        //Changing width of widgets and dividers for Navigation Bar
        val p2 = if (boolean) 1004 else 803
        notableJobsParams.width = p2
        newJobsParams.width = p2
        allJobsParams.width = p2
        notableJobsDividerParams.width = p2
        newJobsDividerParams.width = p2
        allJobsDividerParams.width = p2

        //Changing width of job in notable jobs for Navigation Bar
        val p3 = if (boolean) 980 else 803
        jobParams.width = p3

        //calling the transitions
        notableJobs.layoutParams = notableJobsParams
        newJobRequest.layoutParams = newJobsParams
        allJobs.layoutParams = allJobsParams
    }

}
