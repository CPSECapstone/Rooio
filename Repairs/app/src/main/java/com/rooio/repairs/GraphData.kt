package com.rooio.repairs

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

class GraphData {
    //Creates data for the graph
    fun createData(equipmentId: String, serviceLocationId: String, job: GraphType.JobType, option: GraphType.OptionType, time: GraphType.TimeType) : LineDataSet {
        val entries = ArrayList<Entry>()
        val xArray: ArrayList<Float> = if (time == GraphType.TimeType.MONTH) createMonthlyXAxis() else createYearlyXAxis()
        val yArray: ArrayList<Float> = createYAxis(equipmentId, serviceLocationId, job, option)
        entries.add(Entry(xArray[0], yArray[0]))
        entries.add(Entry(xArray[1], yArray[1]))
        entries.add(Entry(xArray[2], yArray[2]))
        entries.add(Entry(xArray[3], yArray[3]))
        entries.add(Entry(xArray[4], yArray[4]))
        entries.add(Entry(xArray[5], yArray[5]))
        entries.add(Entry(xArray[6], yArray[6]))
        entries.add(Entry(xArray[7], yArray[7]))
        entries.add(Entry(xArray[8], yArray[8]))
        entries.add(Entry(xArray[9], yArray[9]))
        entries.add(Entry(xArray[10], yArray[10]))
        entries.add(Entry(xArray[11], yArray[11]))

        //assign list to LineDataSet and label it
        return LineDataSet(entries, "My Type")
    }

    //Sets the y axis of the graph based on the type of job and the data the user would like to see
    private fun createYAxis(equipmentId: String, serviceLocationId: String, job: GraphType.JobType, option: GraphType.OptionType) : ArrayList<Float> {
        //TODO!! This method is where there should be a call to the API. It might be best to split the function and use a when statement in createData() based on Job and Option Type?
        //**equipmentId can be -1 when it is called from the dashboard, where the graphs are a sum of all equipment job requests
        //Example of data formatting needed for the graphs
        val yArray: ArrayList<Float> = ArrayList()
        yArray.add(11f)
        yArray.add(18f)
        yArray.add(12f)
        yArray.add(14f)
        yArray.add(27f)
        yArray.add(14f)
        yArray.add(35f)
        yArray.add(25f)
        yArray.add(22f)
        yArray.add(10f)
        yArray.add(30f)
        yArray.add(28f)
        return yArray
    }

    //Sets the x axis to months
    private fun createMonthlyXAxis() : ArrayList<Float> {
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
        return xArray
    }

    //Sets the x axis to years
    private fun createYearlyXAxis() : ArrayList<Float> {
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
        xArray.add(2009f)
        return xArray
    }
}