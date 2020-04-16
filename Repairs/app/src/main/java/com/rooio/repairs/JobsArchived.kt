package com.rooio.repairs;


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import com.android.volley.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class JobsArchived  : NavigationBar() {


        private lateinit var archivedList: ListView
        private lateinit var cancelledList: ListView
        private lateinit var declinedList: ListView

        private lateinit var archivedConstraint: ConstraintLayout
        private lateinit var cancelledConstraint: ConstraintLayout
        private lateinit var declinedConstraint: ConstraintLayout


        companion object{

                @JvmStatic private var archivedJobs = ArrayList<JSONObject>()
                @JvmStatic private var completedJobs = ArrayList<JSONObject>()
                @JvmStatic private var declinedJobs = ArrayList<JSONObject>()
                @JvmStatic private var cancelledJobs = ArrayList<JSONObject>()
        }

        private lateinit var completedButton: Button

        //Main
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                initialize()
                setNavigationBar()
                setActionBar()
                createNavigationBar(NavigationType.JOBS)
                onClick()
                clearLists()
                loadArchivedJobs()

        }
        //Initialize Variables
        private fun initialize(){
                setContentView(R.layout.activity_jobs_archived)
                completedButton = findViewById(R.id.button)
                //sets the navigation bar onto the page
                archivedList = findViewById<View>(R.id.completedList) as ListView
                archivedConstraint = findViewById<View>(R.id.completedConstraint) as ConstraintLayout
                cancelledList = findViewById<View>(R.id.cancelledList) as ListView
                cancelledConstraint = findViewById<View>(R.id.cancelledConstraint) as ConstraintLayout
                declinedList = findViewById<View>(R.id.declinedList) as ListView
                declinedConstraint = findViewById<View>(R.id.declinedConstraint) as ConstraintLayout


        }

        //When Returning to Non-Archived Jobs Page
        private fun onClick() {
                completedButton.setOnClickListener { startActivity(Intent(this@JobsArchived, Jobs::class.java)) }
        }



        //Load Jobs from API
        private fun loadArchivedJobs(){
                val archived = "?status=1&status=3&status=4&ordering=-completion_time"
                val url = "service-locations/$userLocationID/jobs/$archived"
                requestJson(Request.Method.GET, JsonType.ARRAY, JsonRequest(false, url,
                        null, responseFunc, errorFunc, true))
        }
        //Clear Swimlanes
        private fun clearLists(){
                archivedJobs.clear()
                completedJobs.clear()
                cancelledJobs.clear()
                declinedJobs.clear()
        }

        //Sort Job Swimlanes
        private fun sortJobsList(list: ArrayList<JSONObject>){

                Collections.sort(list, JSONComparator())

        }

        //Populate the Jobs Into Each Swimlane
        private fun populateLists(responseObj: JSONArray){
                for (i in 0 until responseObj.length()) {
                        val job = responseObj.getJSONObject(i)

                        when(job.getInt("status")){
                                3 -> {
                                        archivedJobs.add(job)
                                        setSize("archived")}
                                4 -> {
                                        cancelledJobs.add(job)
                                        setSize("cancelled") }
                                1 -> {
                                        declinedJobs.add(job)
                                        setSize("declined") }
                        }
                }

                sortJobsList(declinedJobs)
                sortJobsList(archivedJobs)
                sortJobsList(cancelledJobs)

                val customAdapter = JobsCustomAdapter(this, archivedJobs)
                if (archivedJobs.size != 0) archivedList.adapter = customAdapter

                val customAdapter1 = JobsCustomAdapter(this, cancelledJobs)
                if (cancelledJobs.size != 0) cancelledList.adapter = customAdapter1

                val customAdapter2 = JobsCustomAdapter(this, declinedJobs)
                if (declinedJobs.size != 0) declinedList.adapter = customAdapter2

        }

        @JvmField
        var responseFunc = Function<Any, Void?> { jsonObj: Any ->
                val responseObj = jsonObj as JSONArray
                populateLists(responseObj)

                null
        }
        @JvmField
        var errorFunc = Function<String, Void?> { string: String? ->
                completedButton.text = getResources().getString(R.string.error_server);
                null
        }




        //Set the sizes of swimlanes based on # of jobs per swimlane
        private fun setSize(str: String){
                var value = 240
                when(str){
                        "archived" -> {
                                val params = archivedConstraint.layoutParams
                                params.height += value
                                archivedConstraint.layoutParams = params
                                val size = archivedList.layoutParams
                                size.height += value
                                archivedList.layoutParams = size
                        }

                        "cancelled" -> {
                                val params = cancelledConstraint.layoutParams
                                params.height += value
                                cancelledConstraint.layoutParams = params
                                val size = cancelledList.layoutParams
                                size.height += value
                                cancelledList.layoutParams = size
                        }

                        "declined" -> {
                                val params = declinedConstraint.layoutParams
                                params.height += value
                                declinedConstraint.layoutParams = params
                                val size = declinedList.layoutParams
                                size.height += value
                                declinedList.layoutParams = size
                        }
                }
        }

        //Shifting the layout in response to navBar position
        override fun animateActivity(boolean: Boolean){
                val viewGroup = findViewById<ViewGroup>(R.id.jobsConstraint)

                //changing the width of the notableJobs and newJobRequest
                TransitionManager.beginDelayedTransition(viewGroup)

                val archived = viewGroup.findViewById<ViewGroup>(R.id.completedConstraint)
                val cancelled = viewGroup.findViewById<ViewGroup>(R.id.cancelledConstraint)
                val declined = viewGroup.findViewById<ViewGroup>(R.id.declinedConstraint)

                val sideMover = viewGroup.findViewById<ViewGroup>(R.id.sideMover)

                val archivedTitle = viewGroup.findViewById<ViewGroup>(R.id.completedTitleConstraint)
                val cancelledTitle = viewGroup.findViewById<ViewGroup>(R.id.cancelledTitleConstraint)
                val declinedTitle = viewGroup.findViewById<ViewGroup>(R.id.declinedTitleConstraint)

                val boxParams1 = archivedTitle.layoutParams
                val boxParams2 = cancelledTitle.layoutParams
                val boxParams3 = declinedTitle.layoutParams

                val boxParams4 = archived.layoutParams
                val boxParams5 = cancelled.layoutParams
                val boxParams6 = declined.layoutParams

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
                archivedTitle.layoutParams = boxParams1
                cancelledTitle.layoutParams = boxParams2
                declinedTitle.layoutParams = boxParams3
                archived.layoutParams = boxParams4
                cancelled.layoutParams = boxParams5
                declined.layoutParams = boxParams6
                sideMover.layoutParams = boxParams10
        }
}