package com.rooio.repairs

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.content.Context;
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.view.View;
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.json.JSONArray
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.recyclerview_adapter1.*
import org.json.JSONException
import org.json.JSONObject


class ChooseEquipment : RestApi() {

    val url = "https://capstone.api.roopairs.com/v0/service-locations/$userLocationID/equipment/"
    private var list: RecyclerView? = null
    private var search: EditText? = null
    private var recyclerAdapter: adapter? = null
    private var equipmentNameList = ArrayList<String>()
    private lateinit var textView: TextView
    private var equipmentObjectList = ArrayList<EquipmentData>()
    private var equipmentList = ArrayList<String>()
    private lateinit var backButton: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_equipment)

        val actionbarInflater = layoutInflater
        val actionbarView = actionbarInflater.inflate(R.layout.action_bar, null)
        window.addContentView(actionbarView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        supportActionBar!!.elevation = 0.0f

        val equipmentType = intent.getIntExtra("equipmentType", 0)
        backButton = findViewById<View>(R.id.back_button) as ImageView
        list = findViewById(R.id.list) as RecyclerView
        textView = findViewById(R.id.errorText)
        search = findViewById(R.id.search) as EditText
        equipmentNameList = ArrayList()
        loadEquipmentElements()
        onBackClick()

        //makes it so that the divider between items is invisible inside the recyclerview
        list!!.addItemDecoration(object : DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL) {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            }
        })
            search!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    recyclerAdapter!!.getFilter().filter(charSequence.toString())
                }

                override fun afterTextChanged(editable: Editable) {}
            })
    }

    //Click to go back to Dashboard
    private fun onBackClick() {
        backButton.setOnClickListener{
            val intent = Intent(this@ChooseEquipment, Dashboard::class.java)
            startActivity(intent);
        }
    }
    // send JsonRequest Object
    private fun loadEquipmentElements() {
        val request = JsonRequest(false, url, null, responseFuncLoad, errorFuncLoad, true)
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

    // getting all the equipment for the equipment list and passing the equipment list to custom adapter
    private fun loadEquipment(response: JSONArray) {
        val equipmentType = intent.getIntExtra("equipmentType", 0)
        equipmentObjectList.clear()
        for (i in 0 until response.length()) {
            val equipment = EquipmentData(response.getJSONObject(i))
            equipmentObjectList.add(equipment)
        }
        equipmentObjectList.sortWith(compareBy {it.location})


        for (i in 0 until equipmentObjectList.size)
        {
            if (equipmentObjectList[i].type.getIntRepr() == equipmentType) {
                equipmentList.add(equipmentObjectList[i].name)
            }
        }
        loadEquipmentType(equipmentType, equipmentNameList)
        equipmentNameList.addAll(equipmentList)
        val layoutManager = LinearLayoutManager(this)
        list!!.setLayoutManager(layoutManager)
        recyclerAdapter = adapter(this@ChooseEquipment, equipmentNameList)
        list!!.setAdapter(recyclerAdapter)

    }
    //loads the first equipment based on the enum, aka the kind of job request the user clicked on
    private fun loadEquipmentType(equipmentType: Int, equipmentNameList: ArrayList<String>){
        if (equipmentType == 1){
            equipmentNameList.add("General HVAC (No Appliance)")
        }
        else if (equipmentType == 2){
            equipmentNameList.add("General Plumbing (No Appliance)")
        }
        else if (equipmentType == 3){
            equipmentNameList.add("General Lighting (No Appliance)")
        }
        else {
            return
        }
    }

        private inner class adapter(internal var context: Context, internal var mData: List<String>) : RecyclerView.Adapter<adapter.myViewHolder>(), Filterable {

            internal var mfilter: NewFilter

            override fun getFilter(): Filter {
                return mfilter
            }

            init {
                mfilter = NewFilter(this@adapter)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapter.myViewHolder {
                val view =
                        LayoutInflater.from(context).inflate(R.layout.recyclerview_adapter1, parent, false)
                return myViewHolder(view)
            }

            private fun configureElements(holder: adapter.myViewHolder, position: Int){
                val pos : Int

                    if (equipmentNameList.size != equipmentList.size)
                    {
                        if (position == 0)
                        {
                            holder.equipmentName.text = mData[position]
                            holder.graydropDown.visibility = View.GONE
                            pos = position
                        }
                        else {
                            pos = position + 1
                        }
                    }
                    else {
                        pos = position
                    }
                    holder.equipmentName.text = mData[position]

                    setElementTexts(holder.manufacturerInfo, equipmentObjectList[pos].manufacturer)
                    setElementTexts(holder.modelInfo, equipmentObjectList[pos].modelNumber)
                    setElementTexts(holder.locationInfo, equipmentObjectList[pos].location)
                    setElementTexts(holder.serialInfo, equipmentObjectList[pos].serialNumber)
                    setElementTexts(holder.lastServiceByInfo, equipmentObjectList[pos].lastServiceBy)
                    setElementTexts(holder.lastServiceDateInfo, equipmentObjectList[pos].lastServiceDate)

                    holder.equipmentView.setOnClickListener {
                        TransitionManager.beginDelayedTransition(holder.transitionsContainer)
                        holder.visible = !holder.visible
                        val v = if (holder.visible) View.VISIBLE else View.GONE
                        setVisibility(holder, v)
                        val rotate = if (holder.visible) 180f else 0f
                        holder.greendropDown.rotation = rotate
                        val params = holder.equipmentLayout.layoutParams
                        val p = if (holder.visible) 500 else 90
                        if (holder.visible) {
                            holder.equipmentName.setTextColor(Color.parseColor("#00CA8F"))
                            holder.equipmentLayout.setBackgroundResource(R.drawable.green_button_border)
                            holder.graydropDown.visibility = View.GONE
                        } else {
                            holder.equipmentName.setTextColor(Color.parseColor("#333232"))
                            holder.equipmentLayout.setBackgroundResource(R.drawable.gray_button_border)
                            holder.graydropDown.visibility = View.VISIBLE
                            }
                        params.height = p
                        }

            }

            override fun onBindViewHolder(holder: adapter.myViewHolder, position: Int) {
                setVisibility(holder, View.GONE)
                val initial = holder.equipmentLayout.layoutParams
                initial.height = 90

                //checking if equipment type is a general appliance or one of the other enum options
                configureElements(holder, position)
            }
            private fun setElementTexts(element: TextView, str: String){
                try {
                    var jsonStr = str
                    if(jsonStr.isNullOrEmpty() || jsonStr.equals("null"))
                        element.text = "--"
                    else
                        element.text = jsonStr

                }
                catch (e: Exception) {
                    element.text = "--"
                }
            }

            override fun getItemCount(): Int {
                return mData.size
            }

            inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                internal var equipmentName: TextView
                internal var greendropDown: ImageView
                internal var equipmentLayout : ConstraintLayout
                internal var transitionsContainer : ViewGroup
                internal var visible: Boolean = false
                internal var manufacturerText : TextView
                internal var modelText : TextView
                internal var locationText: TextView
                internal var serialText: TextView
                internal var lastServiceByText: TextView
                internal var lastServiceDateText: TextView
                internal var manufacturer: TextView
                internal var modelNumber: TextView
                internal var location: TextView
                internal var serialNumber: TextView
                internal var equipmentDivider: ImageView
                internal var manufacturerInfo: TextView
                internal var modelInfo: TextView
                internal var locationInfo: TextView
                internal var serialInfo: TextView
                internal var lastServiceByInfo: TextView
                internal var lastServiceDateInfo: TextView
                internal var select: Button
                internal var equipmentView : View
                internal var graydropDown : ImageView

                init {
                    equipmentName = itemView.findViewById(R.id.equipmentName)
                    transitionsContainer = itemView.findViewById(R.id.relativeLayout)
                    equipmentLayout = transitionsContainer.findViewById(R.id.equipmentLayout)
                    greendropDown = transitionsContainer.findViewById(R.id.dropDown)
                    graydropDown = transitionsContainer.findViewById(R.id.graydropDown)
                    manufacturer = transitionsContainer.findViewById(R.id.manufacturerInfo)
                    modelNumber = transitionsContainer.findViewById(R.id.modelInfo)
                    location = transitionsContainer.findViewById(R.id.locationInfo)
                    serialNumber = transitionsContainer.findViewById(R.id.serialInfo)
                    manufacturerText = transitionsContainer.findViewById(R.id.manufacturerText)
                    modelText = transitionsContainer.findViewById(R.id.modelText)
                    locationText = transitionsContainer.findViewById(R.id.locationText)
                    serialText = transitionsContainer.findViewById(R.id.serialText)
                    lastServiceByText = transitionsContainer.findViewById(R.id.lastServiceByText)
                    lastServiceDateText = transitionsContainer.findViewById(R.id.lastServiceDateText)
                    equipmentDivider = transitionsContainer.findViewById(R.id.equipmentDivider)
                    manufacturerInfo = transitionsContainer.findViewById(R.id.manufacturerInfo)
                    modelInfo = transitionsContainer.findViewById(R.id.modelInfo)
                    locationInfo = transitionsContainer.findViewById(R.id.locationInfo)
                    serialInfo = transitionsContainer.findViewById(R.id.serialInfo)
                    lastServiceByInfo = transitionsContainer.findViewById(R.id.lastServiceByInfo)
                    lastServiceDateInfo = transitionsContainer.findViewById(R.id.lastServiceDateInfo)
                    select = transitionsContainer.findViewById(R.id.select)
                    equipmentView = transitionsContainer.findViewById(R.id.view)
                }
            }
            inner class NewFilter(var mAdapter: adapter) : Filter() {
                override fun performFiltering(charSequence: CharSequence): FilterResults {
                    equipmentNameList.clear()
                    val results = FilterResults()
                    if (charSequence.length == 0) {
                        equipmentNameList.addAll(equipmentList)
                    } else {
                        val filterPattern = charSequence.toString().toLowerCase().trim { it <= ' ' }
                        for (equipmentName in equipmentList) {
                            if (equipmentName.toLowerCase().startsWith(filterPattern)) {
                                equipmentNameList.add(equipmentName)
                            }
                        }
                    }
                    results.values = equipmentNameList
                    results.count = equipmentNameList.size
                    return results
                }

                override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                    this.mAdapter.notifyDataSetChanged()
                }

            }

            //Switches the visibility of Equipment UI elements
            private fun setVisibility(holder: adapter.myViewHolder, v: Int) {
                holder.manufacturerText.visibility = v
                holder.modelText.visibility = v
                holder.locationText.visibility = v
                holder.serialText.visibility = v
                holder.lastServiceByText.visibility = v
                holder.lastServiceDateText.visibility = v
                holder.manufacturer.visibility = v
                holder.modelNumber.visibility = v
                holder.location.visibility = v
                holder.serialNumber.visibility = v
                holder.equipmentDivider.visibility = v
                holder.manufacturerInfo.visibility = v
                holder.modelInfo.visibility = v
                holder.locationInfo.visibility = v
                holder.serialInfo.visibility = v
                holder.lastServiceByInfo.visibility = v
                holder.lastServiceDateInfo.visibility = v
                holder.select.visibility = v
                holder.greendropDown.visibility = v
            }

        }


    }