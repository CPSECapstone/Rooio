package com.rooio.repairs

import android.app.Activity
import android.content.Context
import android.database.DataSetObserver
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_equipment.*
import java.util.*

internal class EquipmentCustomAdapter(private val context: Context, private val arrayList: ArrayList<EquipmentData>, private val lineChart: LineChart) : ListAdapter {
    private val locations = ArrayList<String>()
    val buttons = ArrayList<Button>()
    var equipmentId: String? = null
        private set

    override fun areAllItemsEnabled(): Boolean {
        return false
    }

    override fun isEnabled(position: Int): Boolean {
        return true
    }

    override fun registerDataSetObserver(observer: DataSetObserver) {}
    override fun unregisterDataSetObserver(observer: DataSetObserver) {}
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        val data = arrayList[position]
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.equipment_list_item, parent, false)
            val text = (context as Activity).findViewById<TextView>(R.id.equipmentPageNoSelectionText)
            val equipmentDetails: ConstraintLayout = context.findViewById(R.id.equipmentDetailsConstraint)
            val editEquipmentDetails: ConstraintLayout = context.findViewById(R.id.editEquipmentConstraint)
            val equipmentAnalytics: ConstraintLayout = context.findViewById(R.id.analyticsConstraint)
            // displaying locations
            val location = convertView.findViewById<TextView>(R.id.location)
            if (!locations.contains(data.location.toUpperCase())) {
                location.text = data.location.toUpperCase()
                locations.add(data.location.toUpperCase())
            } else location.visibility = View.GONE
            // displaying equipment buttons
            val equipment = convertView.findViewById<Button>(R.id.equipmentItem)
            equipment.text = data.name
            //adding button to list of existing buttons
            buttons.add(equipment)
            equipment.setOnClickListener { v: View? ->
                //reset the page's UI
                val addEquipmentLayout: ConstraintLayout = context.findViewById(R.id.addEquipmentConstraint)
                addEquipmentLayout.visibility = View.GONE
                editEquipmentDetails.visibility = View.GONE
                val addEquipmentButton = context.findViewById<Button>(R.id.addEquipmentButton)
                addEquipmentButton.setTextColor(context.getResources().getColor(R.color.darkGray))
                addEquipmentButton.setBackgroundResource(R.drawable.gray_button_border)
                // changing all other buttons back to gray
                for (b in buttons) {
                    b.setBackgroundResource(R.drawable.dark_gray_button_border)
                    b.setTextColor(Color.parseColor("#747479"))
                }
                // set the color of the selected button to green
                equipment.setBackgroundResource(R.drawable.green_button_border)
                equipment.setTextColor(Color.parseColor("#00CA8F"))
                // getting rid of "select an equipment" text
                text.visibility = View.GONE
                // displaying equipment details in edit equipment details fields
                editEquipmentDetails(data, editEquipmentDetails)
                // displaying equipment details
                equipmentDetails.visibility = View.VISIBLE
                equipmentDetails(data, equipmentDetails)
                //saving the equipmentID
                equipmentId = data.id
                // displaying equipment analytics
                equipmentAnalytics.visibility = View.VISIBLE
            }
        }
        return convertView
    }




    // setting text fields with equipment information
    private fun equipmentDetails(equipment: EquipmentData, constraintLayout: ConstraintLayout) {
        val displayName = constraintLayout.findViewById<TextView>(R.id.displayName)
        displayName.text = equipment.name
        val serialNumber = constraintLayout.findViewById<TextView>(R.id.serialNumber)
        if (equipment.serialNumber.isEmpty()) serialNumber.text = "--" else serialNumber.text = equipment.serialNumber
        val lastServiceDate = constraintLayout.findViewById<TextView>(R.id.lastServiceDate)
        if (equipment.lastServiceDate == "null") lastServiceDate.text = "--" else lastServiceDate.text = equipment.lastServiceDate
        val manufacturer = constraintLayout.findViewById<TextView>(R.id.manufacturer)
        if (equipment.manufacturer.isEmpty()) manufacturer.text = "--" else manufacturer.text = equipment.manufacturer
        val location = constraintLayout.findViewById<TextView>(R.id.location)
        if (equipment.location.isEmpty()) location.text = "--" else location.text = equipment.location
        val modelNum = constraintLayout.findViewById<TextView>(R.id.modelNumber)
        if (equipment.modelNumber.isEmpty()) modelNum.text = "--" else modelNum.text = equipment.modelNumber
        val lastServiceBy = constraintLayout.findViewById<TextView>(R.id.lastServiceBy)
        if (equipment.lastServiceBy.isEmpty()) lastServiceBy.text = "--" else lastServiceBy.text = equipment.lastServiceBy
        val equipmentType = constraintLayout.findViewById<TextView>(R.id.equipmentType)
        equipmentType.text = equipment.type.toString()
    }

    private fun editEquipmentDetails(equipment: EquipmentData, constraintLayout: ConstraintLayout) {
        val displayName: TextInputEditText = constraintLayout.findViewById(R.id.editDisplayName)
        displayName.setText(equipment.name)
        val serialNumber: TextInputEditText = constraintLayout.findViewById(R.id.editSerialNumber)
        serialNumber.setText(equipment.serialNumber)
        val manufacturer: TextInputEditText = constraintLayout.findViewById(R.id.editManufacturer)
        manufacturer.setText(equipment.manufacturer)
        val location: TextInputEditText = constraintLayout.findViewById(R.id.editLocation)
        location.setText(equipment.location)
        val modelNum: TextInputEditText = constraintLayout.findViewById(R.id.editModelNumber)
        modelNum.setText(equipment.modelNumber)
        val equipmentType = constraintLayout.findViewById<Spinner>(R.id.editEquipmentType)
        equipmentType.setSelection(equipment.type.getIntRepr() - 1)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getViewTypeCount(): Int {
        return arrayList.size
    }

    override fun isEmpty(): Boolean {
        return false
    }

}