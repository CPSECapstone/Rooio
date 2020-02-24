package com.rooio.repairs

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.content.Context;
import android.content.Intent
import android.graphics.Canvas
import android.view.View;
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.json.JSONArray
import androidx.arch.core.util.Function
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.TransitionManager
import org.json.JSONException







class ChooseEquipment : RestApi() {

    private lateinit var equipmentName: TextView
    private lateinit var manufacturer: TextView
    private lateinit var modelNumber: TextView
    private lateinit var location: TextView
    private lateinit var serialNumber: TextView
    private lateinit var lastServiceBy: TextView
    private lateinit var lastServiceDate: TextView
    private lateinit var manufacturerText: TextView
    private lateinit var modelText: TextView
    private lateinit var locationText: TextView
    private lateinit var serialText: TextView
    private lateinit var lastServiceByText: TextView
    private lateinit var lastServiceDateText: TextView
    private lateinit var dropDown: ImageView
    private lateinit var equipmentDivider: ImageView
    private lateinit var equipmentLayout: ConstraintLayout
    private lateinit var viewEquipment: TextView
    private lateinit var transitionsContainer: ViewGroup
    val url = "https://capstone.api.roopairs.com/v0/service-locations/$userLocationID/equipment/"
    private var list: RecyclerView? = null
    private var search: EditText? = null
    private var recyclerAdapter: adapter? = null
    //private var country: ArrayList<String>? = null
    private var country = ArrayList<String>()
    private lateinit var textView: TextView
    //var listcountry: ArrayList<String>? = null
    private var equipmentObjectList = ArrayList<EquipmentData>()
    private var equipmentList = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_equipment)

        val actionbarInflater = layoutInflater
        val actionbarView = actionbarInflater.inflate(R.layout.action_bar, null)
        window.addContentView(actionbarView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        supportActionBar!!.elevation = 0.0f

        list = findViewById(R.id.list) as RecyclerView
        textView = findViewById(R.id.errorText)
        search = findViewById(R.id.search) as EditText
        country = ArrayList()
        loadEquipmentElements()

        list!!.addItemDecoration(object : DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL) {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                // Do not draw the divider
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



        // send JsonRequest Object
    private fun loadEquipmentElements() {
        val equipmentType = intent.getIntExtra("equipmentType", 0)
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

    // getting all the equipment for the equipment list
    // passing equipment list to custom adapter
    private fun loadEquipment(response: JSONArray) {
        equipmentObjectList.clear()
        //equipmentList.clear()
        for (i in 0 until response.length()) {
            val equipment = EquipmentData(response.getJSONObject(i))
            equipmentObjectList.add(equipment)
        }

        equipmentObjectList.sortWith(compareBy {it.location})

        for (i in 0 until equipmentObjectList.size)
        {
            equipmentList.add(equipmentObjectList[i].name)
        }

        country.addAll(equipmentList)
        val layoutManager = LinearLayoutManager(this)
        list!!.setLayoutManager(layoutManager)
        recyclerAdapter = adapter(this@ChooseEquipment, country!!)
        list!!.setAdapter(recyclerAdapter)

//        customAdapter = EquipmentCustomAdapter(this, equipmentList)
//        if (equipmentList.size != 0) equipmentListView.adapter = customAdapter

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

            override fun onBindViewHolder(holder: adapter.myViewHolder, position: Int) {
                setVisibility(holder, View.GONE)
                val initial = holder.equipmentLayout.layoutParams
                initial.height = 90
                holder.country.text = mData[position]
                holder.dropDown.setOnClickListener{
                    TransitionManager.beginDelayedTransition(holder.transitionsContainer)
                    holder.visible = !holder.visible
                    val v = if (holder.visible) View.VISIBLE else View.GONE
                    val op = if (holder.visible) View.GONE else View.VISIBLE
                    //viewEquipment.visibility = op
                    setVisibility(holder, v)
                    val rotate = if (holder.visible) 180f else 0f
                    holder.dropDown.rotation = rotate
                    val params = holder.equipmentLayout.layoutParams
                    val p = if (holder.visible) 395 else 90
                    params.height = p
                }

            }

            override fun getItemCount(): Int {
                return mData.size
            }

            inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                internal var country: TextView
                internal var dropDown: ImageView
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
                internal var lastServiceBy: TextView
                internal var lastServiceDate: TextView
                internal var equipmentDivider: ImageView


                init {
                    country = itemView.findViewById(R.id.equipmentName)
                    transitionsContainer = itemView.findViewById(R.id.relativeLayout)
                    equipmentLayout = transitionsContainer.findViewById(R.id.equipmentLayout)
//                    equipmentName = transitionsContainer.findViewById(R.id.equipmentName)
                    dropDown = transitionsContainer.findViewById(R.id.dropDown)
                    manufacturer = transitionsContainer.findViewById(R.id.manufacturerInfo)
                    modelNumber = transitionsContainer.findViewById(R.id.modelInfo)
                    location = transitionsContainer.findViewById(R.id.locationInfo)
                    serialNumber = transitionsContainer.findViewById(R.id.serialInfo)
                    lastServiceBy = transitionsContainer.findViewById(R.id.lastServiceByInfo)
                    lastServiceDate = transitionsContainer.findViewById(R.id.lastServiceDateInfo)
                    manufacturerText = transitionsContainer.findViewById(R.id.manufacturerText)
                    modelText = transitionsContainer.findViewById(R.id.modelText)
                    locationText = transitionsContainer.findViewById(R.id.locationText)
                    serialText = transitionsContainer.findViewById(R.id.serialText)
                    lastServiceByText = transitionsContainer.findViewById(R.id.lastServiceByText)
                    lastServiceDateText = transitionsContainer.findViewById(R.id.lastServiceDateText)
                    equipmentDivider = transitionsContainer.findViewById(R.id.equipmentDivider)


//                    viewEquipment = transitionsContainer.findViewById(R.id.viewEquipment)
                }

//                fun recyclerAnimation(): (View) -> Unit = {
//                    layoutPosition.also {
//                        var visible = false
//                        setVisibility(View.GONE)
//                        val initial = equipmentLayout.layoutParams
//                        initial.height = 90
//                        TransitionManager.beginDelayedTransition(transitionsContainer)
//                        visible = !visible
//                        val v = if (visible) View.VISIBLE else View.GONE
//                        val op = if (visible) View.GONE else View.VISIBLE
//                        //viewEquipment.visibility = op
//                        setVisibility(v)
//                        val rotate = if (visible) 180f else 0f
//                        dropDown.rotation = rotate
//                        val params = equipmentLayout.layoutParams
//                        val p = if (visible) 395 else 90
//                        params.height = p
//                    }
//                }

//                private fun recyclerAnimation() {
//                    var visible = false
//                    setVisibility(View.GONE)
//                    val initial = equipmentLayout.layoutParams
//                    initial.height = 90
//                    dropDown.setOnClickListener{
//                        TransitionManager.beginDelayedTransition(transitionsContainer)
//                        visible = !visible
//                        val v = if (visible) View.VISIBLE else View.GONE
//                        val op = if (visible) View.GONE else View.VISIBLE
//                        //viewEquipment.visibility = op
//                        setVisibility(v)
//                        val rotate = if (visible) 180f else 0f
//                        dropDown.rotation = rotate
//                        val params = equipmentLayout.layoutParams
//                        val p = if (visible) 395 else 90
//                        params.height = p
//                    }
//                }

            }
            inner class NewFilter(var mAdapter: adapter) : Filter() {
                override fun performFiltering(charSequence: CharSequence): FilterResults {
                    country.clear()
                    val results = FilterResults()
                    if (charSequence.length == 0) {
                        country.addAll(equipmentList)
                    } else {
                        val filterPattern = charSequence.toString().toLowerCase().trim { it <= ' ' }
                        for (listcountry in equipmentList) {
                            if (listcountry.toLowerCase().startsWith(filterPattern)) {
                                country.add(listcountry)
                            }
                        }
                    }
                    results.values = country
                    results.count = country.size
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
                holder.lastServiceBy.visibility = v
                holder.lastServiceDate.visibility = v
                holder.equipmentDivider.visibility = v
            }

        }


    }