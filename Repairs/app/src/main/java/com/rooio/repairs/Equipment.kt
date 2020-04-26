package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.android.volley.Request
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_equipment.*
import org.json.JSONArray
import org.json.JSONException
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter




class Equipment : NavigationBar() {

    val url = "service-locations/$userLocationID/equipment/"
    private val savedEquipmentBundle = "savedEquipment"
    private val equipmentPositionBundle = "equipmentPosition"
    private val equipmentIdBundle = "equipmentId"
    private var equipmentPosition: Int = -1
    private var equipmentId: String = ""

    private lateinit var messageText: TextView
    private lateinit var equipmentListView: RecyclerView
    private lateinit var addEquipmentConstraint: ConstraintLayout
    private lateinit var editEquipmentConstraint: ConstraintLayout
    private lateinit var equipmentDetailsConstraint: ConstraintLayout
    private lateinit var analyticsConstraint: ConstraintLayout
    private lateinit var addEquipmentButton: Button
    private lateinit var addButton: Button
    private lateinit var editButton: Button
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var addDisplayName: TextInputEditText
    private lateinit var addSerialNumber: TextInputEditText
    private lateinit var addManufacturer: TextInputEditText
    private lateinit var addLocation: TextInputEditText
    private lateinit var addModelNumber: TextInputEditText
    private lateinit var addEquipmentType: Spinner
    private lateinit var displayNameError: TextView
    private lateinit var editDisplayName: TextInputEditText
    private lateinit var editSerialNumber: TextInputEditText
    private lateinit var editManufacturer: TextInputEditText
    private lateinit var editLocation: TextInputEditText
    private lateinit var editModelNumber: TextInputEditText
    private lateinit var editEquipmentType: Spinner
    private lateinit var displayName: TextView
    private lateinit var serialNumber: TextView
    private lateinit var manufacturer: TextView
    private lateinit var location: TextView
    private lateinit var modelNumber: TextView
    private lateinit var equipmentType: TextView
    private lateinit var editDisplayNameError: TextView
    private lateinit var equipmentLoadingPanel: ProgressBar
    private lateinit var addLoadingPanel: ProgressBar
    private lateinit var editLoadingPanel: ProgressBar
    private lateinit var jobSpinner : Spinner
    private lateinit var timeSpinner : Spinner
    private lateinit var optionSpinner : Spinner

    companion object {
        private val equipmentList : ArrayList<EquipmentData> = ArrayList()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)

        //gets rid of sound when the user clicks on the spinner when editing the equipment type

        initializeVariable()
        setNavigationBar()
        setActionBar()
        setSpinners()
        createNavigationBar(NavigationType.EQUIPMENT)
        loadAfterEquipmentSave()
        loadEquipmentElements()
        onStart()
        onPause()
    }

    override fun onStart() {
        super.onStart()
        onEquipmentClick()
        onAddEquipmentClick()
        onAddClick()
        onEditClick()
        onSaveClick()
        onCancelClick()
    }

    //Loads the correct information after a piece of equipment is added or saved
    private fun loadAfterEquipmentSave() {
        val savedEquipment = intent.getStringExtra(savedEquipmentBundle)
        equipmentPosition = intent.getIntExtra(equipmentPositionBundle, -1)
        if (equipmentPosition != -1) {
            equipmentId = intent.getStringExtra(equipmentIdBundle) as String
        }
        Log.d("myTag", equipmentId)
        if (savedEquipment != null) {
            messageText.text = "$savedEquipment successfully saved!"
        }
    }

    //initialize UI variables
    private fun initializeVariable() {
        messageText = findViewById(R.id.equipmentPageNoSelectionText)
        displayNameError = findViewById(R.id.addDisplayNameError)
        editDisplayNameError = findViewById(R.id.editDisplayNameError)


        equipmentListView = findViewById(R.id.equipmentList)
        addEquipmentConstraint = findViewById(R.id.addEquipmentConstraint)
        editEquipmentConstraint = findViewById(R.id.editEquipmentConstraint)
        equipmentDetailsConstraint = findViewById(R.id.equipmentDetailsConstraint)
        analyticsConstraint = findViewById(R.id.analyticsConstraint)

        addEquipmentButton = findViewById(R.id.addEquipmentButton)
        addButton = findViewById(R.id.addButton)
        editButton = findViewById(R.id.editButton)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)

        addDisplayName = findViewById(R.id.addDisplayName)
        addSerialNumber = findViewById(R.id.addSerialNumber)
        addManufacturer = findViewById(R.id.addManufacturer)
        addLocation = findViewById(R.id.addLocation)
        addModelNumber = findViewById(R.id.addModelNumber)
        addEquipmentType = findViewById(R.id.addEquipmentTypeSpinner)

        editDisplayName = findViewById(R.id.editDisplayName)
        editSerialNumber = findViewById(R.id.editSerialNumber)
        editManufacturer = findViewById(R.id.editManufacturer)
        editLocation = findViewById(R.id.editLocation)
        editModelNumber = findViewById(R.id.editModelNumber)
        editEquipmentType = findViewById(R.id.editEquipmentType)

        displayName = findViewById(R.id.displayName)
        serialNumber = findViewById(R.id.serialNumber)
        manufacturer = findViewById(R.id.manufacturer)
        location = findViewById(R.id.location)
        modelNumber = findViewById(R.id.modelNumber)
        equipmentType = findViewById(R.id.equipmentType)

        // setting up spinners (drop down)
        addEquipmentType.adapter = ArrayAdapter<EquipmentType>(this, R.layout.spinner_item, EquipmentType.values())
        editEquipmentType.adapter = ArrayAdapter<EquipmentType>(this, R.layout.spinner_item, EquipmentType.values())
        jobSpinner = findViewById(R.id.jobSpinner)
        timeSpinner = findViewById(R.id.timeSpinner)
        optionSpinner = findViewById(R.id.optionSpinner)


        equipmentLoadingPanel = findViewById(R.id.equipmentLoadingPanel)
        addLoadingPanel = findViewById(R.id.addLoadingPanel)
        editLoadingPanel = findViewById(R.id.editLoadingPanel)
    }

    private fun setSpinners() {
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
        ArrayAdapter.createFromResource(
                this,
                R.array.option_analysis,
                R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            optionSpinner.adapter = adapter
        }
    }

    //Graph
    private fun createGraph() {
        val entries = ArrayList<Entry>()

        entries.add(Entry(0f, 1f))
        entries.add(Entry(1f, 10f))
        entries.add(Entry(2f, 2f))
        entries.add(Entry(3f, 7f))
        entries.add(Entry(4f, 20f))
        entries.add(Entry(5f, 16f))
        entries.add(Entry(6f, 20f))
        entries.add(Entry(7f, 30f))
        entries.add(Entry(8f, 10f))
        entries.add(Entry(9f, 20f))
        entries.add(Entry(10f, 17f))
        entries.add(Entry(11f, 13f))

        //assign list to LineDataSet and label it
        val vl = LineDataSet(entries, "My Type")

        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        //vl.fillAlpha = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        vl.color = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
        vl.setCircleColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))

        class MonthXAxisFormatter : ValueFormatter() {
            private val days = arrayOf("JAN","FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return days.getOrNull(value.toInt()) ?: value.toString()
            }
        }
        vl.valueFormatter = MonthXAxisFormatter()


        lineChart.xAxis.labelRotationAngle = 0f
        lineChart.xAxis.valueFormatter = MonthXAxisFormatter()
        lineChart.xAxis.labelCount = 11
        lineChart.data = LineData(vl)
        lineChart.xAxis.textSize = 15f
        lineChart.axisLeft.textSize = 15f
        lineChart.extraBottomOffset = 5f

//Part7
        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)


//Part8
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)

//Part9
        lineChart.description.isEnabled = false
        lineChart.setNoDataText("No data yet!")
        lineChart.legend.isEnabled = false

//Part10
        lineChart.animateX(1800, Easing.EaseInExpo)

//Part11



    }

    // show add equipment constraint and reset the UI for all other elements
    private fun onAddEquipmentClick() {
        addEquipmentButton.setOnClickListener {
            clearFields()
            analyticsConstraint.visibility = View.GONE
            equipmentDetailsConstraint.visibility = View.GONE
            addEquipmentConstraint.visibility = View.VISIBLE

            addEquipmentButton.setTextColor(ContextCompat.getColor(this,R.color.grayedOut))
            addEquipmentButton.setBackgroundResource(R.drawable.grayed_out_button_border)
        }
    }

    // creating JsonRequest object
    private fun onAddClick() {
        val params = HashMap<Any?, Any?>()
        addButton.setOnClickListener {

            addButton.visibility = View.GONE
            params["display_name"] = addDisplayName.text.toString()
            params["serial_number"] = addSerialNumber.text.toString()
            params["manufacturer"] = addManufacturer.text.toString()
            params["location"] = addLocation.text.toString()
            params["model_number"] = addModelNumber.text.toString()
            params["type"] = (addEquipmentType.selectedItem as EquipmentType).getIntRepr()

            val request = JsonRequest(false, url, params, responseFuncAdd, errorFuncAdd, true)
            sendAddEquipmentInfo(request)
        }
    }

    @JvmField
    // reloading the Equipment page
    var responseFuncAdd = Function<Any, Void?> {
        addLoadingPanel.visibility = View.GONE
        addButton.visibility = View.VISIBLE
        startActivity(Intent(this@Equipment, Equipment::class.java))
        null
    }

    @JvmField
    // add equipment UI disappears and shows error message
    var errorFuncAdd = Function<String, Void?> {
        addLoadingPanel.visibility = View.GONE
        addButton.visibility = View.VISIBLE
        addEquipmentConstraint.visibility = View.GONE
        messageText.visibility = View.VISIBLE
        messageText.text = it.toString()
        messageText.setTextColor(ContextCompat.getColor(this,R.color.Red))
        null
    }

    // sending JsonObject request
    private fun sendAddEquipmentInfo(request: JsonRequest) {
        val displayName = request.params?.get("display_name").toString()

        if(displayName.isNotEmpty()) {
            addLoadingPanel.visibility = View.VISIBLE
            requestJson(Request.Method.POST, JsonType.OBJECT, request)
        }
        else
            displayNameError.text = resources.getText(R.string.required)
        }

    // displaying edit equipment constraint & setting all prompts to the existing information
    private fun onEditClick() {
        editButton.setOnClickListener {
            equipmentDetailsConstraint.visibility = View.GONE
            analyticsConstraint.visibility = View.GONE
            editEquipmentConstraint.visibility = View.VISIBLE
        }
    }

    private fun onSaveClick() {
        val params = HashMap<Any?, Any?>()
        saveButton.setOnClickListener {

            params["display_name"] = editDisplayName.text.toString()
            params["serial_number"] = editSerialNumber.text.toString()
            params["manufacturer"] = editManufacturer.text.toString()
            params["location"] = editLocation.text.toString()
            params["model_number"] = editModelNumber.text.toString()
            params["type"] = (editEquipmentType.selectedItem as EquipmentType).getIntRepr()


            val url = url + equipmentId + "/"

            val request = JsonRequest(false, url, params, responseFuncSave, errorFuncSave, true)
            sendSaveEditRequest(request)

        }
    }

    @JvmField
    var responseFuncSave = Function<Any, Void?> {
        editLoadingPanel.visibility = View.GONE
        editEquipmentConstraint.visibility = View.GONE
        equipmentDetailsConstraint.visibility = View.VISIBLE
        analyticsConstraint.visibility = View.VISIBLE
        null
    }

    @JvmField
    var errorFuncSave = Function<String, Void?> {
        editLoadingPanel.visibility = View.GONE
        editEquipmentConstraint.visibility = View.GONE
        messageText.text = resources.getText(R.string.save_equipment_error)
        messageText.setTextColor(ContextCompat.getColor(this,R.color.Red))
        null
    }

    private fun sendSaveEditRequest(request : JsonRequest){
        val displayName = request.params?.get("display_name").toString()

        if(displayName.isNotEmpty()) {
            editLoadingPanel.visibility = View.VISIBLE
            requestJson(Request.Method.PUT, JsonType.OBJECT, request)
            val intent = Intent(this, Equipment::class.java)
            intent.putExtra(savedEquipmentBundle, displayName)
            startActivity(intent)
        }
        else
            editDisplayNameError.text = resources.getText(R.string.required)
    }

    // reset the UI
    private fun onCancelClick() {
        cancelButton.setOnClickListener {
            addEquipmentConstraint.visibility = View.GONE
            editEquipmentConstraint.visibility = View.GONE
            messageText.visibility = View.VISIBLE
            messageText.text = resources.getText(R.string.select_one_of_the_equipment_items_on_the_left_to_view_details_and_analytics)
            addEquipmentButton.setTextColor(ContextCompat.getColor(this,R.color.darkGray))
            addEquipmentButton.setBackgroundResource(R.drawable.gray_button_border)
        }
    }

    // clear all the input fields
    private fun clearFields() {
        addDisplayName.setText("")
        addSerialNumber.setText("")
        addManufacturer.setText("")
        addLocation.setText("")
        addModelNumber.setText("")
        displayNameError.text = ""
    }

    // send JsonRequest Object
    private fun loadEquipmentElements() {
        addLoadingPanel.visibility = View.GONE
        editLoadingPanel.visibility = View.GONE
        val request = JsonRequest(false, url, null, responseFuncLoad, errorFuncLoad, true)
        requestJson(Request.Method.GET, JsonType.ARRAY, request)
    }

    @JvmField
    // load equipments in the equipment list
    var responseFuncLoad = Function<Any, Void?> { jsonResponse: Any? ->
        equipmentLoadingPanel.visibility = View.GONE
        try {
            val jsonArray = jsonResponse as JSONArray
            loadEquipment(jsonArray)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        null
    }

    @JvmField
    // set error message
    var errorFuncLoad = Function<String, Void?> { string: String? ->
        equipmentLoadingPanel.visibility = View.GONE
        messageText.text = string
        messageText.setTextColor(ContextCompat.getColor(this,R.color.Red))
        null
    }

    // getting all the equipment for the equipment list
    // passing equipment list to custom adapter
    private fun loadEquipment(response: JSONArray) {
        if (equipmentPosition == -1) {
            Equipment.equipmentList.clear()
            for (i in 0 until response.length()) {
                val equipment = EquipmentData(response.getJSONObject(i))
                Equipment.equipmentList.add(equipment)
            }

            Equipment.equipmentList.sortWith(compareBy { it.location })
        }
        val customAdapter = EquipmentAdapter(this, Equipment.equipmentList, equipmentPosition)
        val layoutManager = LinearLayoutManager(this)
        equipmentListView.layoutManager = layoutManager
        equipmentListView.adapter = customAdapter
    }

    private fun  onEquipmentClick() {
        if (equipmentPosition != -1) {
            addEquipmentConstraint.visibility = View.GONE
            editEquipmentConstraint.visibility = View.GONE
            addEquipmentButton.setTextColor(ContextCompat.getColor(applicationContext, R.color.darkGray))
            addEquipmentButton.setBackgroundResource(R.drawable.gray_button_border)
            messageText.visibility = View.GONE
            editEquipmentDetails(Equipment.equipmentList[equipmentPosition])
            fillEquipmentDetails(Equipment.equipmentList[equipmentPosition])
            createGraph()
            analyticsConstraint.visibility = View.VISIBLE
            equipmentDetailsConstraint.visibility = View.VISIBLE
        }
    }

    // setting text fields with equipment information
    private fun fillEquipmentDetails(equipment: EquipmentData) {
        displayName.text = equipment.name
        if (equipment.serialNumber.isEmpty()) serialNumber.text = "--" else serialNumber.text = equipment.serialNumber
        if (equipment.lastServiceDate == "null") lastServiceDate.text = "--" else lastServiceDate.text = equipment.lastServiceDate
        if (equipment.manufacturer.isEmpty()) manufacturer.text = "--" else manufacturer.text = equipment.manufacturer
        if (equipment.location.isEmpty()) location.text = "--" else location.text = equipment.location
        if (equipment.modelNumber.isEmpty()) modelNumber.text = "--" else modelNumber.text = equipment.modelNumber
        if (equipment.lastServiceBy.isEmpty()) lastServiceBy.text = "--" else lastServiceBy.text = equipment.lastServiceBy
        equipmentType.text = equipment.type.toString()
    }

    private fun editEquipmentDetails(equipment: EquipmentData) {
        editDisplayName.setText(equipment.name)
        editSerialNumber.setText(equipment.serialNumber)
        editManufacturer.setText(equipment.manufacturer)
        editLocation.setText(equipment.location)
        editModelNumber.setText(equipment.modelNumber)
        editEquipmentType.setSelection(equipment.type.getIntRepr() - 1)
    }

    //Animates the main page content when the navigation bar collapses/expands
    override fun animateActivity(boolean: Boolean){
        val pageConstraint = findViewById<ConstraintLayout>(R.id.equipmentPageConstraint)

        val equipment = pageConstraint.findViewById<ConstraintLayout>(R.id.equipmentBarConstraint)

        TransitionManager.beginDelayedTransition(pageConstraint)
        val params = equipment.layoutParams
        val change = if (boolean) 463 else 649
        params.width = change

        equipment.layoutParams = params
    }

}