package com.rooio.repairs

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
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

        private lateinit var archivedText: TextView
        private lateinit var cancelledText: TextView
        private lateinit var declinedText: TextView
        private lateinit var loadingPanel: ProgressBar


        companion object{
                @JvmStatic private var archivedJobs = ArrayList<JSONObject>()
                @JvmStatic private var completedJobs = ArrayList<JSONObject>()
                @JvmStatic private var declinedJobs = ArrayList<JSONObject>()
                @JvmStatic private var cancelledJobs = ArrayList<JSONObject>()
        }

        //Main
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                initialize()
                setNavigationBar()
                setActionBar()
                createNavigationBar(NavigationType.ARCHIVED)
                clearLists()
                loadArchivedJobs()

        }
        //Initialize Variables
        private fun initialize(){
                setContentView(R.layout.activity_jobs_archived)
                //sets the navigation bar onto the page
                archivedConstraint = findViewById(R.id.archivedConstraint)
                archivedList = findViewById(R.id.archivedList)
                cancelledList = findViewById(R.id.cancelledList)
                cancelledConstraint = findViewById(R.id.cancelledConstraint)
                declinedList = findViewById(R.id.declinedList)
                declinedConstraint = findViewById(R.id.declinedConstraint)
                archivedText = findViewById(R.id.archivedText)
                cancelledText = findViewById(R.id.cancelledText)
                declinedText = findViewById(R.id.declinedText)
                loadingPanel = findViewById(R.id.loadingPanel)
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
                                        if (i > 1) setSize(archivedConstraint)
                                        setSize(archivedList)}
                                4 -> {
                                        cancelledJobs.add(job)
                                        if (i > 1) setSize(cancelledConstraint)
                                        setSize(cancelledList) }
                                1 -> {
                                        declinedJobs.add(job)
                                        if (i > 1) setSize(declinedConstraint)
                                        setSize(declinedList) }
                        }
                }

                sortJobsList(declinedJobs)
                sortJobsList(archivedJobs)
                sortJobsList(cancelledJobs)

                val customAdapter = JobsCustomAdapter(this, archivedJobs)
                if (archivedJobs.size != 0) archivedList.adapter = customAdapter
                else setEmptyText(JobType.COMPLETED)

                val customAdapter1 = JobsCustomAdapter(this, cancelledJobs)
                if (cancelledJobs.size != 0) cancelledList.adapter = customAdapter1
                else setEmptyText(JobType.CANCELLED)

                val customAdapter2 = JobsCustomAdapter(this, declinedJobs)
                if (declinedJobs.size != 0) declinedList.adapter = customAdapter2
                else setEmptyText(JobType.DECLINED)
        }

        @JvmField
        var responseFunc = Function<Any, Void?> { jsonObj: Any ->
                loadingPanel.visibility = View.INVISIBLE
                val responseObj = jsonObj as JSONArray
                populateLists(responseObj)

                null
        }
        @JvmField
        var errorFunc = Function<String, Void?> { error: String? ->
                null
        }

        //Sets the empty list text
        private fun setEmptyText(type: JobType) {
            when (type) {
                JobType.COMPLETED -> archivedText.visibility = View.VISIBLE
                JobType.CANCELLED ->  cancelledText.visibility = View.VISIBLE
                JobType.DECLINED ->  declinedText.visibility = View.VISIBLE
            }
        }


        //Set the sizes of swim lanes based on # of jobs per swim lane
        private fun setSize(constraint: ViewGroup) {
                val value = 170
                val params = constraint.layoutParams
                params.height += value
                constraint.layoutParams = params
        }

        //Shifting the layout in response to navBar position
        override fun animateActivity(boolean: Boolean){
            val viewGroup = findViewById<ViewGroup>(R.id.jobsConstraint)

            //changing the width of the notableJobs and newJobRequest
            TransitionManager.beginDelayedTransition(viewGroup)

            val archived = viewGroup.findViewById<ViewGroup>(R.id.archivedConstraint)
            val cancelled = viewGroup.findViewById<ViewGroup>(R.id.cancelledConstraint)
            val declined = viewGroup.findViewById<ViewGroup>(R.id.declinedConstraint)


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
        }
}