package com.rooio.repairs


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.transition.TransitionManager
import android.widget.ImageView
import android.widget.TextView
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.AutoTransition
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.json.JSONArray
import org.json.JSONObject
import android.R.array
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.R.string.no
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.android.volley.Request
import kotlinx.android.synthetic.main.activity_navigation_bar.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Dashboard : NavigationBar() {

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

    private lateinit var lightingButton: Button
    private lateinit var plumbingButton: Button
    private lateinit var hvacButton: Button
    private lateinit var applianceButton: Button


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

        initialize()
        setNavigationBar()
        setActionBar()
        loadJobs()
        createNavigationBar(NavigationType.DASHBOARD)
        jobRequestsClicked()
    }

    private fun initialize(){
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
        clockImage = findViewById(R.id.clockImage)
        repairImage = findViewById(R.id.repairImage)
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
        val url = "service-locations/$userLocationID/jobs/"
        requestJson(Request.Method.GET, JsonType.ARRAY, JsonRequest(false, url,
                null, responseFunc, errorFunc, true))
    }

    private fun clearLists(){
        pendingJobs.clear()
        scheduledJobs.clear()
        inProgressJobs.clear()
    }

    private fun populateLists(responseObj: JSONArray){
        clearLists()
        for (i in 0 until responseObj.length()) {
            val job = responseObj.getJSONObject(i)
            when(job.getInt("status")) {
                0 -> pendingJobs.add(job)
                2 -> scheduledJobs.add(job)
                5 -> inProgressJobs.add(job)
                6 -> inProgressJobs.add(job)
                3 -> archivedJobs.add(job)

            }
        }
        notableJobsFill()
    }

    private fun listCount(){
        pendingNum!!.text = pendingJobs.size.toString()
        scheduledNum!!.text = scheduledJobs.size.toString()
        inProgressNum!!.text = inProgressJobs.size.toString()
    }

    private fun notableJobsFill(){
        if (pendingJobs.size != 0) {
            // Sort PendingJobs and fill in
            resultSort = sortJobsList(pendingJobs)
            loadNotable(0, 3)
        }
        else if (inProgressJobs.size != 0){
            //Sort inProgressJobs and fill in
            resultSort = sortJobsList(inProgressJobs)
            loadNotable(0, 2)
        }
        else if (scheduledJobs.size != 0){
            //Check if there is a scheduled job today
            var i = 0
            resultSort = sortJobsList(scheduledJobs)
            for (index in resultSort.indices){
                if (0 == (timeConvert(resultSort[index].getString("status_time_value")))) {
                    if(index == (resultSort.size - 1) && (i == 0)){
                        clockImage.setVisibility(View.GONE)
                        name.text = "No Jobs to Display"
                        repairImage.setVisibility(View.GONE)
                    }
                }
                else{
                    i = 1
                    loadNotable(index, 1)
                }
            }
        }
        else{
            clockImage.setVisibility(View.GONE)
            name.text = "No Jobs to Display"
            repairImage.setVisibility(View.GONE)

        }
    }


    @Throws(ParseException::class)
    fun timeConvert(dateStr: String): Int {
        //convert to proper format "yyyy-MM-dd HH:mm:ss"
        val eta = convertToNewFormat(dateStr)
        //GET NOW DATE + TIME
        val now = now()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        //If past due then return 0
        if (sdf.parse(eta)!!.before(sdf.parse(now))) {
           return 0
        } else {
            return 1
        }
    }

    private fun now(): String {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val now = Date()
        val c = Calendar.getInstance()
        return df.format(c.time)
    }

    private fun loadNotable(index: Int, colorStatus: Int ){

        val locationObj = resultSort[index].getJSONObject("service_location")
        val internal_client = locationObj.getJSONObject("internal_client")

        val equipmentObjList = resultSort[index].getJSONArray("equipment")

        val equipmentObj = equipmentObjList.getJSONObject(0)
        val category = equipmentObj.getString("service_category")

        //repair type
        when (category) {
            "0" ->
                //                            repairType.setText("General Appliance");
                //categories.add("General Appliance");
                repairType.text = "General Appliance"
            "1" ->
                //                            repairType.setText("HVAC");
                repairType.text = "HVAC"
            "2" ->
                //                            repairType.setText("Lighting and Electrical");
                repairType.text = "Lighting and Electrical"
            "3" ->
                //                            repairType.setText("Plumbing");
                repairType.text = "Plumbing"
        }

        //set the date/time
        if (!resultSort[index].isNull("status_time_value")) {
            val date1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(convertToNewFormat(resultSort[index].getString("status_time_value")))
            timeImage.text = date1!!.toString()

        }
        //address
        address.text = locationObj.getString("physical_address")

        //name
        name.setText(internal_client.getString("name"))

        //greeting
        name_greeting!!.text = ("Hi, " + userName + "!")

        //load logo
        val imageVal = internal_client.getString("logo")
        Picasso.with(this)
                .load(imageVal)
                .into(image)


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

    @Throws(ParseException::class)
    fun convertToNewFormat(dateStr: String): String {
        val utc = TimeZone.getTimeZone("UTC")
        val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val destFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sourceFormat.timeZone = utc
        val convertedDate = sourceFormat.parse(dateStr)
        return destFormat.format(convertedDate!!)
    }

     fun sortJobsList(list: ArrayList<JSONObject>):ArrayList<JSONObject> {

        Collections.sort(list, JSONComparator())

        return list;
    }





}
