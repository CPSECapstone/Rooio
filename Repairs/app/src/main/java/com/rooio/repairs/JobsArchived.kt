package com.rooio.repairs;


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.activity_jobs_archived.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList
import androidx.arch.core.util.Function


class JobsArchived  : NavigationBar() {

        private var id2: ListView? = null
        val statuses = arrayListOf<String>()

        companion object{

                @JvmStatic private var archivedJobs = ArrayList<JSONObject>()
        }

        private var CompletedConstraint: ConstraintLayout? = null

        private var completedButton: Button? = null

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)



                setContentView(R.layout.activity_jobs_archived)
                completedButton = findViewById(R.id.button)

                //sets the navigation bar onto the page
                id2 = findViewById<View>(R.id.id2) as ListView

                CompletedConstraint = findViewById<View>(R.id.completedConstraint) as ConstraintLayout

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


                onClick()
                loadJobs()

        }
        private fun onClick() {
                completedButton!!.setOnClickListener { startActivity(Intent(this@JobsArchived, Jobs::class.java)) }
        }


        private fun loadJobs(){
                val url = BaseUrl + "service-locations/$userLocationID/jobs/"
                requestGetJsonArray(JsonRequest(false, url, null, responseFunc, errorFunc, true))
        }

        private fun clearLists(){
                archivedJobs.clear()
        }

        private fun populateLists(responseObj: JSONArray){
                clearLists()
                for (i in 0 until responseObj.length()) {
                        val job = responseObj.getJSONObject(i)

                        if (job.getInt("status") == 3){
                                archivedJobs.add(job)
                                sizes("completed")
                        }

                }
                val customAdapter = JobsCustomerAdapter(this, archivedJobs)
                if (archivedJobs.size != 0) id2!!.adapter = customAdapter


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

        private fun set_size(str: String, value: Int){

                if (str == "completed"){
                        val params = completedConstraint!!.layoutParams
                        params.height += value
                        completedConstraint!!.layoutParams = params
                        val size = id2!!.layoutParams
                        size.height += value
                        id2!!.layoutParams = size
                }

        }override fun animateActivity(boolean: Boolean){
                val viewGroup = findViewById<ViewGroup>(R.id.jobsConstraint)
//
//        //changing the width of the notableJobs and newJobRequest
//        TransitionManager.beginDelayedTransition(viewGroup)
//
//        val completed = viewGroup.findViewById<ViewGroup>(R.id.completedConstraint)
//
//
//        val sideMover = viewGroup.findViewById<ViewGroup>(R.id.sideMover)
//
//
//
//        val id2 = viewGroup.findViewById<ViewGroup>(R.id.id2)
//
//
//        val completedTitle = viewGroup.findViewById<ViewGroup>(R.id.completedTitle)
//
//
//        val boxParams1 = completed.layoutParams
//        val boxParams2 = completedTitle.layoutParams
//        val boxParams3 = id2.layoutParams
//        val boxParams10 = sideMover.layoutParams
//
//        val p2 = if (boolean) 480 else 454
//        boxParams1.width = p2
//        boxParams2.width = p2
//        boxParams3.width = p2
//
//
//        //calling the transitions
//        completed.layoutParams = boxParams1
//        completedTitle.layoutParams = boxParams2
//        id2.layoutParams = boxParams3
//
//
//        val p3 = if (boolean) 85 else 20
//
//        boxParams10.width = p3
//
//        sideMover.layoutParams = boxParams10
        }
}