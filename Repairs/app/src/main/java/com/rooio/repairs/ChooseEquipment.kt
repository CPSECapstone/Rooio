package com.rooio.repairs

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.content.Context;
import android.view.View;
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.json.JSONArray
import androidx.arch.core.util.Function
import org.json.JSONException


class ChooseEquipment : RestApi() {

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
        list!!.addItemDecoration(DividerItemDecoration(list!!.getContext(), layoutManager.orientation))
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
                holder.country.text = mData[position]
            }

            override fun getItemCount(): Int {
                return mData.size
            }

            inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                internal var country: TextView

                init {
                    country = itemView.findViewById(R.id.country)
                }
            }

            inner class NewFilter(var mAdapter: adapter) : Filter() {
                override fun performFiltering(charSequence: CharSequence): FilterResults {
                    country!!.clear()
                    val results = FilterResults()
                    if (charSequence.length == 0) {
                        country!!.addAll(equipmentList!!)
                    } else {
                        val filterPattern = charSequence.toString().toLowerCase().trim { it <= ' ' }
                        for (listcountry in equipmentList!!) {
                            if (listcountry.toLowerCase().startsWith(filterPattern)) {
                                country!!.add(listcountry)
                            }
                        }
                    }
                    results.values = country
                    results.count = country!!.size
                    return results
                }

                override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                    this.mAdapter.notifyDataSetChanged()
                }

            }
        }

    }