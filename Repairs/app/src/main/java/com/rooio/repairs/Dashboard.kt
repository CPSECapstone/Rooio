package com.rooio.repairs


import android.media.Image
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
import androidx.transition.AutoTransition
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


private val have  = ArrayList<ServiceProviderData>()
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

        //sets the navigation bar onto the page
        val nav_inflater = layoutInflater
        val tmpView = nav_inflater.inflate(R.layout.activity_navigation_bar, null)

        window.addContentView(tmpView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        //sets the action bar onto the page

        val actionbar_inflater = layoutInflater
        val actionBarView = actionbar_inflater.inflate(R.layout.action_bar, null)
        window.addContentView(actionBarView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        supportActionBar!!.elevation = 0.0f
        populate_test()

        loadJobs()

        createNavigationBar("dashboard")
        jobRequestsClicked()
    }

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


        val p2 = if (boolean) 1004 else 803
        boxParams1.width = p2
        boxParams2.width = p2
        boxParams3.width = p2
        boxParams4.width = p2
        boxParams5.width = p2
        boxParams6.width = p2

        val p3 = if (boolean) 980 else 803

        boxParams7.width = p3


        //calling the transitions
        notableJobs.layoutParams = boxParams1
        newJobRequest.layoutParams = boxParams2
    }

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

            when(job.getInt("status")) {
                0 -> pendingJobs.add(job)
                2 -> scheduledJobs.add(job)
                5 -> inProgressJobs.add(job)
//                6 -> inProgressJobs.add(job)
                3 -> archivedJobs.add(job)
            }
        }
    }

    private fun listCount(){
//        pendingNum!!.text = "9";
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

}
