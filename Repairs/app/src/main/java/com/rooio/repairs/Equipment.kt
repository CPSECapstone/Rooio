package com.rooio.repairs

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import com.android.volley.Request
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONException

class Equipment : NavigationBar() {

    val url = "https://capstone.api.roopairs.com/v0/service-locations/$userLocationID/equipment/"

    private var textView: TextView? = null
    private var equipmentListView: ListView? = null
    private var addEquipmentButton: Button? = null
    private var addEquipmentConstraint: ConstraintLayout? = null
    private var addButton: Button? = null
    private var cancelButton: Button? = null
    private var displayName: TextInputEditText? = null
    private var serialNumber: TextInputEditText? = null
    private var manufacturer: TextInputEditText? = null
    private var location: TextInputEditText? = null
    private var modelNumber: TextInputEditText? = null
    private var displayNameError: TextView? = null
    private var locationError: TextView? = null

    private val equipmentList = ArrayList<EquipmentData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)

        //sets the navigation bar onto the page
        val navInflater = layoutInflater
        val tmpView = navInflater.inflate(R.layout.activity_navigation_bar, null)

        window.addContentView(tmpView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        //sets the action bar onto the page
        val actionbarInflater = layoutInflater
        val actionbarView = actionbarInflater.inflate(R.layout.action_bar, null)
        window.addContentView(actionbarView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        supportActionBar!!.elevation = 0.0f

        //making navigation bar w/ Equipment text highlighted
        createNavigationBar("equipment")

        textView = findViewById<TextView>(R.id.equipmentPageNoSelectionText)
        equipmentListView = findViewById<View>(R.id.equipmentList) as ListView
        addEquipmentButton = findViewById<Button>(R.id.addEquipmentButton)
        addEquipmentConstraint = findViewById<ConstraintLayout>(R.id.addEquipmentConstraint)
        addButton = findViewById<Button>(R.id.addButton)
        cancelButton = findViewById(R.id.cancelButton)
        displayNameError = findViewById(R.id.addDisplayNameError)
        locationError = findViewById(R.id.addLocationError)

        displayName = findViewById(R.id.addDisplayName)
        serialNumber = findViewById(R.id.addSerialNumber)
        manufacturer = findViewById(R.id.addManufacturer)
        location = findViewById(R.id.addLocation)
        modelNumber = findViewById(R.id.addModelNumber)

        onAddEquipmentClick();
        onAddClick();
        onCancelClick();

        //get equipment
        loadEquipment();
    }

    private fun onAddEquipmentClick() {
        addEquipmentButton!!.setOnClickListener {
            addEquipmentConstraint!!.visibility = View.VISIBLE
            addEquipmentButton!!.setTextColor(resources.getColor(R.color.grayedOut))
            addEquipmentButton!!.setBackgroundResource(R.drawable.grayed_out_button_border)
        }
    }

    private fun onAddClick() {
        val params = HashMap<String, Any>()
        addButton!!.setOnClickListener {
            params["display_name"] = displayName!!.text.toString()
            params["serial_number"] = serialNumber!!.text.toString()
            params["manufacturer"] = manufacturer!!.text.toString()
            params["location"] = location!!.text.toString()
            params["model_number"] = modelNumber!!.text.toString()
            params["type"] = 1

            val request = JsonRequest(false, url, params, responseFuncAdd, errorFuncAdd, true)
            sendAddEquipmentInfo(request)
        }
    }

    //reload the Equipment page
    @JvmField
    var responseFuncAdd = Function<Any, Void?> { jsonObj: Any ->
        startActivity(Intent(this@Equipment, Equipment::class.java))
        null
    }

    //Error provided if login information is incorrect or cannot be found from request
    @JvmField
    var errorFuncAdd = Function<String, Void?> {
        addEquipmentConstraint!!.visibility = View.GONE
        textView!!.visibility = View.VISIBLE
        textView!!.text = it.toString()
        null
    }

    private fun sendAddEquipmentInfo(request: JsonRequest) {
        val displayName = request.params["display_name"].toString()
        val location = request.params["location"].toString()

        if(!displayName.isNullOrEmpty() && !location.isNullOrEmpty())
            requestJsonObject(Request.Method.POST, request)
        else{
            if(displayName.isNullOrEmpty())
                displayNameError!!.text = resources.getText(R.string.required)

            if(location.isNullOrEmpty())
                locationError!!.text = resources.getText(R.string.required)
        }
    }

    private fun onCancelClick() {
        cancelButton!!.setOnClickListener {
            addEquipmentConstraint!!.visibility = View.GONE
        }
    }

    var responseFuncLoad = Function<Any, Void?> { jsonResponse: Any? ->
        try {
            val jsonArray = jsonResponse as JSONArray
            loadElements(jsonArray)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        null
    }

    var errorFuncLoad = Function<String, Void?> { string: String? ->
        textView!!.text = string
        textView!!.setTextColor(Color.parseColor("#E4E40B0B"))
        null
    }

    private fun loadEquipment() {
        val request = JsonRequest(false, url, null, responseFuncLoad, errorFuncLoad, true)
        requestGetJsonArray(request)
    }

    private fun loadElements(response: JSONArray) {
        equipmentList.clear()
        for (i in 0 until response.length()) {
            val equipment = EquipmentData(response.getJSONObject(i))
            equipmentList.add(equipment);
        }

        val customAdapter = EquipmentCustomAdapter(this, equipmentList)
        if (equipmentList?.size != 0) equipmentListView!!.adapter = customAdapter
    }

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