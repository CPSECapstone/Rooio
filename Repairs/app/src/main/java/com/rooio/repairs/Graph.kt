package com.rooio.repairs

import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.activity_equipment.*

abstract class Graph  : NavigationBar() {

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
    fun createGraph(equipmentId: String, serviceLocationId: String, job: GraphType.JobType, option: GraphType.OptionType, time: GraphType.TimeType) {
        val vl = GraphData().createData(equipmentId, serviceLocationId, job, option, time)
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
}