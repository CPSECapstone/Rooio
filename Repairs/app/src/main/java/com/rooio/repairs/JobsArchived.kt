package com.rooio.repairs

import android.animation.ObjectAnimator
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
        //Clear swim lanes
        private fun clearLists(){
                archivedJobs.clear()
                completedJobs.clear()
                cancelledJobs.clear()
                declinedJobs.clear()
        }

        //Sort Job swim lanes
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
                                        if (i > 0) setSize("archived", archivedConstraint)
                                        setSize("archived", archivedList)}
                                4 -> {
                                        cancelledJobs.add(job)
                                        if (i > 0) setSize("cancelled", cancelledConstraint)
                                        setSize("cancelled", cancelledList) }
                                1 -> {
                                        declinedJobs.add(job)
                                        if (i > 0) setSize("declined", declinedConstraint)
                                        setSize("declined", declinedList) }
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
        var errorFunc = Function<String, Void?> { error: String? ->
                completedButton.text = error
                null
        }




        //Set the sizes of swim lanes based on # of jobs per swim lane
        private fun setSize(str: String, constraint: ViewGroup) {
                val value = 210
                when (str) {
                        "archived" -> {
                                val params = constraint.layoutParams
                                params.height += value
                                constraint.layoutParams = params
                        }

                        "cancelled" -> {
                                val params = constraint.layoutParams
                                params.height += value
                                constraint.layoutParams = params
                        }

                        "started" -> {
                                val params = constraint.layoutParams
                                params.height += value
                                constraint.layoutParams = params
                        }

                        "declined" -> {
                                val params = constraint.layoutParams
                                params.height += value
                                constraint.layoutParams = params
                        }

                }
        }

        //Shifting the layout in response to navBar position
        override fun animateActivity(boolean: Boolean){
            val viewGroup = findViewById<ViewGroup>(R.id.jobsConstraint)

            //changing the width of the notableJobs and newJobRequest
            TransitionManager.beginDelayedTransition(viewGroup)

            val archived = viewGroup.findViewById<ViewGroup>(R.id.archivedConstraint)
            val cancelled = viewGroup.findViewById<ViewGroup>(R.id.cancelledConstraint)
            val declined = viewGroup.findViewById<ViewGroup>(R.id.declinedConstraint)

            val title = viewGroup.findViewById<Button>(R.id.button)

            val amount = if (boolean) -125f else 0f
            var animation = ObjectAnimator.ofFloat(archived, "translationX", amount)
            if (boolean) animation.duration = 900 else animation.duration = 300
            animation.start()
            animation = ObjectAnimator.ofFloat(cancelled, "translationX", amount)
            if (boolean) animation.duration = 900 else animation.duration = 300
            animation.start()
            animation = ObjectAnimator.ofFloat(declined, "translationX", amount)
            if (boolean) animation.duration = 900 else animation.duration = 300
            animation.start()
            animation = ObjectAnimator.ofFloat(title, "translationX", amount)
            if (boolean) animation.duration = 900 else animation.duration = 300
            animation.start()
        }
}