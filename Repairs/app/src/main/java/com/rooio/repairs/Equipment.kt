package com.rooio.repairs

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.transition.TransitionManager
import com.android.volley.Request
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONException


class Equipment : NavigationBar() {

    val url = "https://capstone.api.roopairs.com/v0/service-locations/$userLocationID/equipment/"

    private lateinit var textView: TextView
    private lateinit var equipmentListView: ListView
    private lateinit var addEquipmentConstraint: ConstraintLayout
    private lateinit var equipmentDetailsConstraint: ConstraintLayout
    private lateinit var analyticsConstraint: ConstraintLayout
    private lateinit var addEquipmentButton: Button
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button
    private lateinit var displayName: TextInputEditText
    private lateinit var serialNumber: TextInputEditText
    private lateinit var manufacturer: TextInputEditText
    private lateinit var location: TextInputEditText
    private lateinit var modelNumber: TextInputEditText
    private lateinit var equipmentType: Spinner
    private lateinit var displayNameError: TextView

    private val equipmentList = ArrayList<EquipmentData>()
    private var customAdapter = EquipmentCustomAdapter(this, equipmentList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)

        initializeVariable()
        setNavigationBar()
        setActionBar()
        createNavigationBar("equipment")
        onAddEquipmentClick()
        onAddClick()
        onCancelClick()
        loadEquipmentElements()
    }

    //initialize UI variables
    private fun initializeVariable() {
        textView = findViewById(R.id.equipmentPageNoSelectionText)
        equipmentListView = findViewById(R.id.equipmentList)
        addEquipmentConstraint = findViewById(R.id.addEquipmentConstraint)
        equipmentDetailsConstraint = findViewById(R.id.equipmentDetailsConstraint)
        analyticsConstraint = findViewById(R.id.analyticsConstraint)
        addEquipmentButton = findViewById(R.id.addEquipmentButton)
        addButton = findViewById(R.id.addButton)
        cancelButton = findViewById(R.id.cancelButton)
        displayNameError = findViewById(R.id.addDisplayNameError)

        displayName = findViewById(R.id.addDisplayName)
        serialNumber = findViewById(R.id.addSerialNumber)
        manufacturer = findViewById(R.id.addManufacturer)
        location = findViewById(R.id.addLocation)
        modelNumber = findViewById(R.id.addModelNumber)
        equipmentType = findViewById(R.id.addEquipmentTypeSpinner)

        equipmentType.adapter = ArrayAdapter<EquipmentType>(this, android.R.layout.simple_list_item_1, EquipmentType.values())
    }

    // Sets the navigation bar onto the page
    private fun setNavigationBar() {
        //sets the navigation bar onto the page
        val navInflater = layoutInflater
        val tmpView = navInflater.inflate(R.layout.activity_navigation_bar, null)

        window.addContentView(tmpView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    // Sets the action bar onto the page
    private fun setActionBar() {
        //sets the action bar onto the page
        val actionbarInflater = layoutInflater
        val actionbarView = actionbarInflater.inflate(R.layout.action_bar, null)
        window.addContentView(actionbarView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        supportActionBar!!.elevation = 0.0f
    }

    // show add equipment constraint and reset the UI for all other elements
    private fun onAddEquipmentClick() {
        addEquipmentButton.setOnClickListener {
            clearFields()
            analyticsConstraint.visibility = View.GONE
            equipmentDetailsConstraint.visibility = View.GONE
            addEquipmentConstraint.visibility = View.VISIBLE

            for(b in customAdapter.buttons){
                b.setBackgroundResource(R.drawable.dark_gray_button_border)
                b.setTextColor(Color.parseColor("#747479"))
            }

            addEquipmentButton.setTextColor(ContextCompat.getColor(this,R.color.grayedOut))
            addEquipmentButton.setBackgroundResource(R.drawable.grayed_out_button_border)
        }
    }

    // creating JsonRequest object
    private fun onAddClick() {
        val params = HashMap<String, Any>()
        addButton.setOnClickListener {
            params["display_name"] = displayName.text.toString()
            params["serial_number"] = serialNumber.text.toString()
            params["manufacturer"] = manufacturer.text.toString()
            params["location"] = location.text.toString()
            params["model_number"] = modelNumber.text.toString()
            params["type"] = (equipmentType.selectedItem as EquipmentType).getIntRepr()

            val request = JsonRequest(false, url, params, responseFuncAdd, errorFuncAdd, true)
            sendAddEquipmentInfo(request)
        }
    }

    @JvmField
    // reloading the Equipment page
    var responseFuncAdd = Function<Any, Void?> {
        startActivity(Intent(this@Equipment, Equipment::class.java))
        null
    }

    @JvmField
    // add equipment UI disappears and shows error message
    var errorFuncAdd = Function<String, Void?> {
        addEquipmentConstraint.visibility = View.GONE
        textView.visibility = View.VISIBLE
        textView.text = it.toString()
        textView.setTextColor(ContextCompat.getColor(this,R.color.Red))
        null
    }

    // sending JsonObject request
    private fun sendAddEquipmentInfo(request: JsonRequest) {
        val displayName = request.params["display_name"].toString()

        if(displayName.isNotEmpty())
            requestJsonObject(Request.Method.POST, request)
        else
            displayNameError.text = resources.getText(R.string.required)
        }

    // reset the UI
    private fun onCancelClick() {
        cancelButton.setOnClickListener {
            addEquipmentConstraint.visibility = View.GONE
            textView.visibility = View.VISIBLE
            addEquipmentButton.setTextColor(ContextCompat.getColor(this,R.color.GrayText))
            addEquipmentButton.setBackgroundResource(R.drawable.gray_button_border)
        }
    }

    // clear all the input fields
    private fun clearFields() {
        displayName.setText("")
        serialNumber.setText("")
        manufacturer.setText("")
        location.setText("")
        modelNumber.setText("")
        displayNameError.text = ""
    }

    // send JsonRequest Object
    private fun loadEquipmentElements() {
        val request = JsonRequest(false, url, HashMap(), responseFuncLoad, errorFuncLoad, true)
        requestGetJsonArray(request)
    }

    @JvmField
    // load equipments in the equipment list
    var responseFuncLoad = Function<Any, Void?> { jsonResponse: Any? ->
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
        textView.text = string
        textView.setTextColor(ContextCompat.getColor(this,R.color.Red))
        null
    }

    // getting all the equipment for the equipment list
    // passing equipment list to custom adapter
    private fun loadEquipment(response: JSONArray) {
        equipmentList.clear()
        for (i in 0 until response.length()) {
            val equipment = EquipmentData(response.getJSONObject(i))
            equipmentList.add(equipment)
        }

        equipmentList.sortWith(compareBy {it.location})

        customAdapter = EquipmentCustomAdapter(this, equipmentList)
        if (equipmentList.size != 0) equipmentListView.adapter = customAdapter
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