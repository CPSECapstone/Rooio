package com.rooio.repairs;


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_jobs_archived.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList
import androidx.arch.core.util.Function


class JobsArchived  : NavigationBar() {

        private var completedList: ListView? = null
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
                completedList = findViewById<View>(R.id.completedList) as ListView

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


                createNavigationBar(NavigationType.JOBS)


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
                if (archivedJobs.size != 0) completedList!!.adapter = customAdapter


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
                        val size = completedList!!.layoutParams
                        size.height += value
                        completedList!!.layoutParams = size
                }

        }override fun animateActivity(boolean: Boolean){

        }
}