package com.rooio.repairs

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.widget.*
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.android.volley.Request
import org.json.JSONArray
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChooseEquipment : RestApi() {

    val url = "service-locations/$userLocationID/equipment/"
    private lateinit var list: RecyclerView
    private lateinit var search: EditText
    private lateinit var recyclerChooseAdapter: ChooseEquipmentAdapter
    //private var equipmentNameList = ArrayList<String>()
    private lateinit var textView: TextView
    private var equipmentObjectList: ArrayList<EquipmentData> = ArrayList()
    //private var equipmentList = ArrayList<String>()
    private val locations = ArrayList<String>()
    //private var savedEquipmentList = ArrayList<String>()
    private lateinit var backButton: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_equipment)

        setActionBar()
        initializeVariables()
        onBackClick()
        loadEquipmentElements()
        setFilter()
        //makes it so that the divider between items is invisible inside the recyclerview
        //list.addItemDecoration(object : DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL) {
            //override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            //}
        //})

    }

    //Initializes variables that are used in loadElements()
    private fun initializeVariables() {
        //val equipmentType = intent.getIntExtra("equipmentType", 0)
        backButton = findViewById(R.id.back_button)
        list = findViewById(R.id.list)
        textView = findViewById(R.id.errorText)
        search = findViewById(R.id.search)
        //equipmentNameList = ArrayList()
    }

    //Click to go back to Dashboard
    private fun onBackClick() {
        backButton.setOnClickListener{
            startActivity(Intent(this@ChooseEquipment, Dashboard::class.java))
        }
    }
    // send JsonRequest Object
    private fun loadEquipmentElements() {
        val request = JsonRequest(false, url, HashMap(), responseFuncLoad, errorFuncLoad, true)
        requestJson(Request.Method.GET, JsonType.ARRAY, request)
    }

    @JvmField
    // load equipments in the equipment list
    var responseFuncLoad = Function<Any, Void?> { jsonResponse: Any? ->
        val jsonArray = jsonResponse as JSONArray
        loadEquipment(jsonArray)
        null
    }

    @JvmField
    // set error message
    var errorFuncLoad = Function<String, Void?> { string: String? ->
        textView.text = string
        textView.setTextColor(ContextCompat.getColor(this,R.color.Red))
        null
    }

    // getting all the equipment for the equipment list and passing the equipment list to custom ChooseAdapter
    private fun loadEquipment(response: JSONArray) {
        val equipmentType = intent.getIntExtra("equipmentType", 0)
        loadEquipmentType(equipmentType)
        for (i in 0 until response.length()) {
            val equipment = EquipmentData(response.getJSONObject(i))
            equipmentObjectList.add(equipment)
        }
        equipmentObjectList.sortWith(compareBy {it.location})
        //adding the different equipment types to the equipmentObjectList
        var equipmentDataList: ArrayList<EquipmentData> = ArrayList()
        for (i in 0 until equipmentObjectList.size)
        {
            //savedEquipmentList.add(equipmentObjectList[i].name)
            if (equipmentObjectList[i].type.getIntRepr() == equipmentType) {
                equipmentDataList.add(equipmentObjectList[i])
            }
        }

        val layoutManager = LinearLayoutManager(this)
        list.layoutManager = layoutManager
        recyclerChooseAdapter = ChooseEquipmentAdapter(this@ChooseEquipment, equipmentDataList)
        list.adapter = recyclerChooseAdapter

    }
    //loads the first equipment based on the enum, aka the kind of job request the user clicked on
    private fun loadEquipmentType(equipmentType: Int){
        when (equipmentType) {
            1 -> equipmentObjectList.add(EquipmentData ("General HVAC (No Appliance)", EquipmentType.HVAC))
            2 -> equipmentObjectList.add(EquipmentData("General Plumbing (No Appliance)", EquipmentType.PLUMBING))
            3 -> equipmentObjectList.add(EquipmentData("General Lighting (No Appliance)", EquipmentType.LIGHTING_AND_ELECTRICAL))
            else -> return
        }
    }

    private fun setFilter() {
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                recyclerChooseAdapter.filter(charSequence.toString())
            }
            override fun afterTextChanged(editable: Editable) {}
        })
    }

}