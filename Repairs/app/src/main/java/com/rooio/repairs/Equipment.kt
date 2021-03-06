package com.rooio.repairs

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.android.volley.Request
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_equipment.*
import org.json.JSONArray
import org.json.JSONException

class Equipment : Graph() {

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

    companion object {
        private val equipmentList : ArrayList<EquipmentData> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)

        //gets rid of sound when the user clicks on the spinner when editing the equipment type
        onResume()

        initializeVariable()
        setNavigationBar()
        setActionBar()
        setGraphSpinners()
        setGraphAdapters(GraphType.EQUIPMENT)
        createNavigationBar(NavigationType.EQUIPMENT)
        loadAfterEquipmentSave()
        loadEquipmentElements()
        onEquipmentClick()
        onAddEquipmentClick()
        onAddClick()
        onEditClick()
        onSaveClick()
        onCancelClick()
        onPause()
    }

    //Loads the correct information after a piece of equipment is added or saved
    private fun loadAfterEquipmentSave() {
        val savedEquipment = intent.getStringExtra(savedEquipmentBundle)
        equipmentPosition = intent.getIntExtra(equipmentPositionBundle, -1)
        if (equipmentPosition != -1) {
            equipmentId = intent.getStringExtra(equipmentIdBundle) as String
        }
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

        equipmentLoadingPanel = findViewById(R.id.equipmentLoadingPanel)
        addLoadingPanel = findViewById(R.id.addLoadingPanel)
        editLoadingPanel = findViewById(R.id.editLoadingPanel)
    }

    fun getEquipmentList() : ArrayList<EquipmentData> {
        return Equipment.equipmentList
    }

    fun setEquipmentList(list : ArrayList<EquipmentData>) {
        Equipment.equipmentList.clear()
        for(i in 0 until list.size){
            Equipment.equipmentList.add(list[i])
        }

        equipmentListView.adapter = EquipmentCustomAdapter(this, Equipment.equipmentList, 0)
    }

    fun isValidEquipmentRequest(request: JsonRequest) : Boolean {
        val displayName = request.params?.get("display_name").toString()
        return displayName.isNotEmpty()
    }

    // show add equipment constraint and reset the UI for all other elements
    private fun onAddEquipmentClick() {
        addEquipmentButton.setOnClickListener {
            clearFields()
            analyticsConstraint.visibility = View.GONE
            equipmentDetailsConstraint.visibility = View.GONE
            editEquipmentConstraint.visibility = View.GONE
            addEquipmentConstraint.visibility = View.VISIBLE

            // making sure equipment list is all grey buttons
            equipmentPosition = -1
            equipmentListView.adapter = EquipmentCustomAdapter(this, Equipment.equipmentList, equipmentPosition)

            addEquipmentButton.setTextColor(ContextCompat.getColor(this,R.color.grayedOut))
            addEquipmentButton.setBackgroundResource(R.drawable.grayed_out_button_border)
        }
    }

    // creating JsonRequest object
    private fun onAddClick() {
        val params = HashMap<Any?, Any?>()
        addButton.setOnClickListener {
            params["display_name"] = addDisplayName.text.toString()
            params["serial_number"] = addSerialNumber.text.toString()
            params["manufacturer"] = addManufacturer.text.toString()
            params["location"] = addLocation.text.toString()
            params["model_number"] = addModelNumber.text.toString()
            params["type"] = (addEquipmentType.selectedItem as EquipmentType).getIntRepr()

            val url = url + "equipment/"

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
        if(isValidEquipmentRequest(request)) {
            addButton.visibility = View.GONE
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

    //Handles when the save button is clicked
    private fun onSaveClick() {
        val params = HashMap<Any?, Any?>()
        saveButton.setOnClickListener {

            params["display_name"] = editDisplayName.text.toString()
            params["serial_number"] = editSerialNumber.text.toString()
            params["manufacturer"] = editManufacturer.text.toString()
            params["location"] = editLocation.text.toString()
            params["model_number"] = editModelNumber.text.toString()
            params["type"] = (editEquipmentType.selectedItem as EquipmentType).getIntRepr()


            val url = url + "equipment/$equipmentId/"

            val request = JsonRequest(false, url, params, responseFuncSave, errorFuncSave, true)
            sendSaveEditRequest(request)
        }
    }

    //Handles when the job request is saved
    @JvmField
    var responseFuncSave = Function<Any, Void?> {
        editLoadingPanel.visibility = View.GONE
        editEquipmentConstraint.visibility = View.GONE
        equipmentDetailsConstraint.visibility = View.VISIBLE
        analyticsConstraint.visibility = View.VISIBLE
        null
    }

    //Handles when there is an error when the job request is saved
    @JvmField
    var errorFuncSave = Function<String, Void?> {
        editLoadingPanel.visibility = View.GONE
        editEquipmentConstraint.visibility = View.GONE
        messageText.text = resources.getText(R.string.save_equipment_error)
        messageText.setTextColor(ContextCompat.getColor(this,R.color.Red))
        null
    }

    //Saves the edits made to the equipment item
    private fun sendSaveEditRequest(request : JsonRequest){
        if(isValidEquipmentRequest(request)) {
            editLoadingPanel.visibility = View.VISIBLE
            requestJson(Request.Method.PUT, JsonType.OBJECT, request)
            val intent = Intent(this, Equipment::class.java)
            val displayName = request.params?.get("display_name").toString()
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

        val url = url + "equipment/"

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

        val customAdapter = EquipmentCustomAdapter(this, Equipment.equipmentList, equipmentPosition)
        equipmentListView.adapter = customAdapter
        val layoutManager = LinearLayoutManager(this)
        equipmentListView.layoutManager = layoutManager
    }

    //Checks if an equipment has been clicked on
    private fun  onEquipmentClick() {
        if (equipmentPosition != -1) {
            addEquipmentConstraint.visibility = View.GONE
            editEquipmentConstraint.visibility = View.GONE
            addEquipmentButton.setTextColor(ContextCompat.getColor(applicationContext, R.color.darkGray))
            addEquipmentButton.setBackgroundResource(R.drawable.gray_button_border)
            messageText.visibility = View.GONE
            editEquipmentDetails(Equipment.equipmentList[equipmentPosition])
            fillEquipmentDetails(Equipment.equipmentList[equipmentPosition])
            setUpGraph()
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

    //Setting edit fields with equipment information
    private fun editEquipmentDetails(equipment: EquipmentData) {
        editDisplayName.setText(equipment.name)
        editSerialNumber.setText(equipment.serialNumber)
        editManufacturer.setText(equipment.manufacturer)
        editLocation.setText(equipment.location)
        editModelNumber.setText(equipment.modelNumber)
        editEquipmentType.setSelection(equipment.type.getIntRepr() - 1)
    }

    //Sets up the graph in the way the equipment page needs
    override fun setUpGraph() {
        val params = HashMap<Any?, Any?>()
        params["service_location_id"] = userLocationID
        params["equipment_id"] = equipmentId

        val url = url + "equipment/$equipmentId/job-history/"
        val request = JsonRequest(false, url, params, responseFuncGraph, errorFuncGraph, true)
        requestJson(Request.Method.GET, JsonType.ARRAY, request)
    }

    @JvmField
    var responseFuncGraph = Function<Any, Void?> { jsonResponse: Any? ->
        try {
            val jsonArray = jsonResponse as JSONArray
            createGraph(jsonArray, graphJob, graphOption, graphTime)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        null
    }

    @JvmField
    var errorFuncGraph = Function<String, Void?> {
        null
    }

    //Animates the main page content when the navigation bar collapses/expands
    override fun animateActivity(boolean: Boolean){
        val pageConstraint = findViewById<ConstraintLayout>(R.id.equipmentPageConstraint)

        val equipment = pageConstraint.findViewById<ConstraintLayout>(R.id.equipmentBarConstraint)
        if (boolean) {
            val autoTransition = AutoTransition()
            autoTransition.duration = 1400
            TransitionManager.beginDelayedTransition(pageConstraint, autoTransition)
        } else {
            TransitionManager.beginDelayedTransition(pageConstraint)
        }
        val params = equipment.layoutParams
        val change = if (boolean) 463 else 649
        params.width = change
        equipment.layoutParams = params

        val amount = if (boolean) -125f else 0f
        var animation = ObjectAnimator.ofFloat(analyticsConstraint, "translationX", amount)
        if (boolean) animation.duration = 1400 else animation.duration = 300
        animation.start()
        animation = ObjectAnimator.ofFloat(equipmentDetailsConstraint, "translationX", amount)
        if (boolean) animation.duration = 1400 else animation.duration = 300
        animation.start()
        animation = ObjectAnimator.ofFloat(editEquipmentConstraint, "translationX", amount)
        if (boolean) animation.duration = 1400 else animation.duration = 300
        animation.start()
        animation = ObjectAnimator.ofFloat(addEquipmentConstraint, "translationX", amount)
        if (boolean) animation.duration = 1400 else animation.duration = 300
        animation.start()
    }

}