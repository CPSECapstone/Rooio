package com.rooio.repairs

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_equipment.*
import org.json.JSONArray
import org.json.JSONObject

abstract class Graph : NavigationBar() {

    val url = "service-locations/$userLocationID/equipment/"
    var graphJob: GraphType.JobType = GraphType.JobType.REPAIR
    var graphOption: GraphType.OptionType = GraphType.OptionType.TOTAL_COST
    var graphTime: GraphType.TimeType = GraphType.TimeType.MONTH
    var xAxisArrayList = ArrayList<Float>()
    var yAxisArrayList = ArrayList<Float>()

    //Handles when the graph spinners change
    fun setSpinners(type: GraphType) {
        val jobSpinner = findViewById<Spinner>(R.id.jobSpinner)
        val timeSpinner = findViewById<Spinner>(R.id.timeSpinner)
        val optionSpinner = findViewById<Spinner>(R.id.optionSpinner)
        // setting on click listeners for the spinner items
        jobSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (parent.getItemAtPosition(position).toString()) {
                    "Repair" -> graphJob = GraphType.JobType.REPAIR
                    "Maintenance" -> graphJob = GraphType.JobType.MAINTENANCE
                    "Installation" -> graphJob = GraphType.JobType.INSTALLATION
                    "All" -> graphJob = GraphType.JobType.ALL
                }
                setUpGraph()
            }
        }
        optionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (type == GraphType.EQUIPMENT)
                    when (parent.getItemAtPosition(position).toString()) {
                        "Cost" -> graphOption = GraphType.OptionType.COST
                        "Job Requests" -> graphOption = GraphType.OptionType.JOBS
                    }
                else
                    when (parent.getItemAtPosition(position).toString()) {
                        "Cost" -> graphOption = GraphType.OptionType.TOTAL_COST
                        "Job Requests" -> graphOption = GraphType.OptionType.TOTAL_JOBS
                    }
                setUpGraph()
            }
        }
        timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (parent.getItemAtPosition(position).toString()) {
                    "Monthly" -> graphTime = GraphType.TimeType.MONTH
                    "Yearly" -> graphTime = GraphType.TimeType.YEAR
                }
                setUpGraph()
            }
        }
    }

    //Sets the appearance and text of adapters
    fun setAdapters(type: GraphType) {
        ArrayAdapter.createFromResource(
                this,
                R.array.job_analysis,
                R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            jobSpinner.adapter = adapter
        }
        ArrayAdapter.createFromResource(
                this,
                R.array.time_analysis,
                R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            timeSpinner.adapter = adapter
        }
        if (type == GraphType.EQUIPMENT)
            ArrayAdapter.createFromResource(
                    this,
                    R.array.option_analysis,
                    R.layout.spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                optionSpinner.adapter = adapter
            }
        else
            ArrayAdapter.createFromResource(
                    this,
                    R.array.option_total_analysis,
                    R.layout.spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                optionSpinner.adapter = adapter
            }
    }

    class MonthXAxisFormatter : ValueFormatter() {
        private val days = arrayOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()) ?: value.toString()
        }
    }

    class YearXAxisFormatter : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return value.toInt().toString()
        }
    }

    //Creates graph based on equipment
    fun createGraph(response: JSONArray, job: GraphType.JobType, option: GraphType.OptionType, time: GraphType.TimeType) {
        val vl = createData(response, job, option, time)
        //Initial graph settings
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        //Sets colors of the graph
        vl.fillColor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        vl.color = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        vl.setCircleColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))

        //X Axis time format
        if (time == GraphType.TimeType.MONTH) {
            vl.valueFormatter = MonthXAxisFormatter()
            lineChart.xAxis.valueFormatter = MonthXAxisFormatter()
        } else {
            vl.valueFormatter = YearXAxisFormatter()
            lineChart.xAxis.valueFormatter = YearXAxisFormatter()
        }
        //Sets the offsets, fonts, and sizes of the graph
        lineChart.xAxis.labelRotationAngle = 0f
        lineChart.xAxis.labelCount = 11
        lineChart.data = LineData(vl)
        lineChart.xAxis.textSize = 15f
        lineChart.axisLeft.textSize = 15f
        lineChart.extraBottomOffset = 5f
        //Sets the axis lines
        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)
        //Sets the zoom on the graph
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        //Removes unnecessary chart information
        lineChart.description.isEnabled = false
        lineChart.setNoDataText("No data yet!")
        lineChart.legend.isEnabled = false
        //Handles the zoom animation
        lineChart.animateX(1800, Easing.EaseInExpo)
    }

    //Creates data for the graph
    private fun createData(response: JSONArray, job: GraphType.JobType, option: GraphType.OptionType, time: GraphType.TimeType): LineDataSet {
        val entries = ArrayList<Entry>()
        if (time == GraphType.TimeType.MONTH) createMonthlyXAxis() else createYearlyXAxis()
        createYAxis(response, job, option, time)
        entries.add(Entry(xAxisArrayList[0], yAxisArrayList[0]))
        entries.add(Entry(xAxisArrayList[1], yAxisArrayList[1]))
        entries.add(Entry(xAxisArrayList[2], yAxisArrayList[2]))
        entries.add(Entry(xAxisArrayList[3], yAxisArrayList[3]))
        entries.add(Entry(xAxisArrayList[4], yAxisArrayList[4]))
        entries.add(Entry(xAxisArrayList[5], yAxisArrayList[5]))
        entries.add(Entry(xAxisArrayList[6], yAxisArrayList[6]))
        entries.add(Entry(xAxisArrayList[7], yAxisArrayList[7]))
        entries.add(Entry(xAxisArrayList[8], yAxisArrayList[8]))
        entries.add(Entry(xAxisArrayList[9], yAxisArrayList[9]))
        entries.add(Entry(xAxisArrayList[10], yAxisArrayList[10]))
        entries.add(Entry(xAxisArrayList[11], yAxisArrayList[11]))

        //assign list to LineDataSet and label it
        return LineDataSet(entries, "My Type")
    }

    //Sets the y axis of the graph based on the type of job and the data the user would like to see
    private fun createYAxis(response: JSONArray, jobType: GraphType.JobType, option: GraphType.OptionType, time: GraphType.TimeType) {
        //TODO!! This method is where there should be a call to the API. It might be best to split the function and use a when statement in createData() based on Job and Option Type?
        //**equipmentId can be -1 when it is called from the dashboard, where the graphs are a sum of all equipment job requests
        //Example of data formatting needed for the graphs
        val yArray: ArrayList<Float> = ArrayList()

        val jobMap = HashMap<Float, ArrayList<JSONObject>>()

        for (i in 0 until response.length()) {
            val obj = response.getJSONObject(i)
            if (time == GraphType.TimeType.MONTH) {
                val completedTime = obj.getString("completed_time").split("-").get(1).toFloat()
                if (jobMap.containsKey(completedTime)) {
                    val existingList = jobMap[completedTime]
                    existingList!!.add(obj)
                    jobMap[completedTime] = existingList
                } else {
                    val newList = ArrayList<JSONObject>()
                    newList.add(obj)
                    jobMap[completedTime] = newList
                }
            } else {
                val completedTime = obj.getString("completed_time").split("-").get(0).toFloat()
                if (jobMap.containsKey(completedTime)) {
                    val existingList = jobMap[completedTime]
                    existingList!!.add(obj)
                    jobMap[completedTime] = existingList
                } else {
                    val newList = ArrayList<JSONObject>()
                    newList.add(obj)
                    jobMap[completedTime] = newList
                }
            }
        }

        for (key in jobMap.keys) {
            val values = jobMap[key]
            for (job in values!!.iterator()) {
                if (jobType == GraphType.JobType.ALL) {
                    Unit
                } else if (job.getInt("service_type") != jobType.getInt()) {
                    values.remove(job)
                }
            }
            jobMap[key] = values
        }

        for (x in xAxisArrayList) {
            val jobsPerX = jobMap[x]
            if (jobsPerX == null) {
                yArray.add(0f)
            } else {
                when (option) {
                    //TODO: it's off by one for the months
                    GraphType.OptionType.JOBS -> yArray.add(jobsPerX.size.toFloat())

                    GraphType.OptionType.COST -> {
                        var total: Float = 0f
                        for (job in jobsPerX) {
                            val allInvoices = job.getJSONArray("invoices")
                            for (i in 0 until allInvoices.length()) {
                                val invoiceTotal = allInvoices.getJSONObject(i).getString("total").toFloat()
                                total += invoiceTotal
                            }
                        }
                        yArray.add(total)
                    }

                    else -> yArray.add(0f)
                }
            }
        }

        yAxisArrayList = yArray
    }

    //Sets the x axis to months
    private fun createMonthlyXAxis() {
        val xArray: ArrayList<Float> = ArrayList()
        xArray.add(0f)
        xArray.add(1f)
        xArray.add(2f)
        xArray.add(3f)
        xArray.add(4f)
        xArray.add(5f)
        xArray.add(6f)
        xArray.add(7f)
        xArray.add(8f)
        xArray.add(9f)
        xArray.add(10f)
        xArray.add(11f)
        xAxisArrayList = xArray
    }

    //Sets the x axis to years
    private fun createYearlyXAxis() {
        val xArray: ArrayList<Float> = ArrayList()
        xArray.add(1998f)
        xArray.add(1999f)
        xArray.add(2000f)
        xArray.add(2001f)
        xArray.add(2002f)
        xArray.add(2003f)
        xArray.add(2004f)
        xArray.add(2005f)
        xArray.add(2006f)
        xArray.add(2007f)
        xArray.add(2008f)
        xArray.add(2020f)
        xAxisArrayList = xArray
    }

    abstract fun setUpGraph()
}