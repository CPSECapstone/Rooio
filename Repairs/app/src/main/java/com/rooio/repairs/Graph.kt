package com.rooio.repairs

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.activity_equipment.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

abstract class Graph : NavigationBar() {

    val url = "service-locations/$userLocationID/"

    var graphJob: GraphType.JobType = GraphType.JobType.REPAIR
    var graphOption: GraphType.OptionType = GraphType.OptionType.COST
    var graphTime: GraphType.TimeType = GraphType.TimeType.MONTH

    //Handles when the graph spinners change
    fun setGraphSpinners() {
        val jobSpinner = findViewById<Spinner>(R.id.jobSpinner)
        val timeSpinner = findViewById<Spinner>(R.id.timeSpinner)
        val optionSpinner = findViewById<Spinner>(R.id.optionSpinner)
        // setting on click listeners for the spinner items
        jobSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
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

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> graphOption = GraphType.OptionType.COST
                    1 -> graphOption = GraphType.OptionType.JOBS
                }
                setUpGraph()
            }
        }
        timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (parent.getItemAtPosition(position).toString()) {
                    "Monthly" -> graphTime = GraphType.TimeType.MONTH
                    "Yearly" -> graphTime = GraphType.TimeType.YEAR
                }
                setUpGraph()
            }
        }
    }

    //Sets the appearance and text of adapters
    fun setGraphAdapters(type: GraphType) {
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

    class DollarYAxisFormatter : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return "$" + value.toInt().toString()
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
        vl.fillAlpha = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
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
        if (option == GraphType.OptionType.COST) {
            lineChart.axisLeft.valueFormatter = DollarYAxisFormatter()
        } else {
            lineChart.axisLeft.valueFormatter = null
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
        //Sets the marker on the graph
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(false)
        val markerView = CustomMarker(this@Graph, R.layout.marker_view, option)
        lineChart.marker = markerView
        //Removes unnecessary chart information
        lineChart.description.isEnabled = false
        lineChart.setNoDataText("No data yet!")
        lineChart.legend.isEnabled = false
        //Handles the zoom animation
        lineChart.animateX(1800, Easing.EaseInExpo)
    }

    //Creates data for the graph
    private fun createData(response: JSONArray, jobType: GraphType.JobType, optionType: GraphType.OptionType, time: GraphType.TimeType): LineDataSet {
        val entries = ArrayList<Entry>()
        val jobMap = filterData(response, jobType, time)

        val xAxisArrayList = if (time == GraphType.TimeType.MONTH) createMonthlyXAxis() else createYearlyXAxis(jobMap)
        val yAxisArrayList = createYAxis(jobMap, xAxisArrayList, optionType)

        for (x in 0 until xAxisArrayList.size) {
            entries.add(Entry(xAxisArrayList[x], yAxisArrayList[x]))
        }

        //assign list to LineDataSet and label it
        return LineDataSet(entries, "My Type")
    }

    // filtering the JSON response by time and job type
    private fun filterData(response: JSONArray, jobType: GraphType.JobType, time: GraphType.TimeType): HashMap<Float, ArrayList<JSONObject>> {
        val filteredByTime = filterByTime(response, time)
        return filterByJobType(filteredByTime, jobType)
    }

    // filter the jobs by time
    private fun filterByTime(response: JSONArray, time: GraphType.TimeType): HashMap<Float, ArrayList<JSONObject>>{
        val jobMap = HashMap<Float, ArrayList<JSONObject>>()

        for (i in 0 until response.length()) {
            val obj = response.getJSONObject(i)
            val completedTime =
                    if (time == GraphType.TimeType.MONTH)
                    // the month number minus one so that it works with the xAxisArray
                        obj.getString("completed_time").split("-")[1].toFloat() - 1
                    else
                        obj.getString("completed_time").split("-")[0].toFloat()

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
        return jobMap
    }

    // filter the jobs by job type
    private fun filterByJobType(jobMap: HashMap<Float, ArrayList<JSONObject>>, jobType: GraphType.JobType) : HashMap<Float, ArrayList<JSONObject>>{
        for (key in jobMap.keys) {
            val jobsList = jobMap[key]
            if (jobsList != null) {
                // have to use an iterator because of the remove method
                val iterator = jobsList.iterator()
                while (iterator.hasNext()) {
                    val job = iterator.next()
                    if (jobType != GraphType.JobType.ALL && job.getInt("service_type") != jobType.getInt()) {
                        iterator.remove()
                    }
                }
                jobMap[key] = jobsList
            }
        }
        return jobMap
    }

    //Sets the y axis of the graph based on the type of job and the data the user would like to see
    private fun createYAxis(jobMap: HashMap<Float, ArrayList<JSONObject>>, xAxisArrayList: ArrayList<Float>, option: GraphType.OptionType): ArrayList<Float> {
        var yArray: ArrayList<Float> = ArrayList()

        for (x in xAxisArrayList) {
            val jobsPerX = jobMap[x]
            if (jobsPerX == null) {
                yArray.add(0f)
            } else {
                yArray = calculateYValue(yArray, option, jobsPerX)
            }
        }
        return yArray
    }

    // calculating the y value based on the option type
    private fun calculateYValue(yArray: ArrayList<Float>, option: GraphType.OptionType, jobsPerX: ArrayList<JSONObject>) : ArrayList<Float>{
        when (option) {
            GraphType.OptionType.JOBS -> yArray.add(jobsPerX.size.toFloat())

            GraphType.OptionType.COST -> {
                var totalCost = 0f
                for (job in jobsPerX) {
                    val allInvoices = job.getJSONArray("invoices")
                    for (i in 0 until allInvoices.length()) {
                        val invoiceTotal = allInvoices.getJSONObject(i).getString("total").toFloat()
                        totalCost += invoiceTotal
                    }
                }
                yArray.add(totalCost)
            }
        }

        return yArray
    }

    //Sets the x axis to months
    private fun createMonthlyXAxis(): ArrayList<Float> {
        val xArray: ArrayList<Float> = ArrayList()
        for (i in 0 until 12){
            xArray.add(i.toFloat())
        }
        return xArray
    }

    //Sets the x axis to years
    private fun createYearlyXAxis(jobMap: HashMap<Float, ArrayList<JSONObject>>): ArrayList<Float> {
        val xArray = jobMap.keys.toMutableList()

        // creates x axis starting from the current year if there are no existing jobs
        if (xArray.isEmpty()) {
            xArray.add(Calendar.getInstance().get(Calendar.YEAR).toFloat())
        }

        // just to make sure the graph has enough values to display if there aren't enough years
        while (xArray.size < 12) {
            val max = xArray.max()
            if (max != null) {
                xArray.add(max + 1)
            }
        }

        // casting sorted List to ArrayList
        return ArrayList(xArray.sorted())
    }

    abstract fun setUpGraph()
}