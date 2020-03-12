package com.rooio.repairs

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import java.util.*
import kotlin.collections.ArrayList

class ChooseEquipmentAdapter(context: Context, dataList: ArrayList<EquipmentData>) : RecyclerView.Adapter<ChooseEquipmentAdapter.ViewHolder>() {

    private val equipmentDataList = dataList
    private val equipmentData: ArrayList<EquipmentData> = ArrayList()
    private val locations: ArrayList<String> = ArrayList()
    private val applicationContext = context

    init {
        this.equipmentData.addAll(equipmentDataList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
                LayoutInflater.from(applicationContext).inflate(R.layout.equipment_item, parent, false)
        return ViewHolder(view)
    }

    private fun configureElements(holder: ViewHolder, position: Int){
        holder.equipmentView.setOnClickListener{null}
        holder.equipmentName.setTextColor(Color.parseColor("#333232"))
        holder.equipmentLayout.setBackgroundResource(R.drawable.gray_button_border)
        holder.grayDropDown.visibility = View.VISIBLE

        if (position == 0) {
            holder.locationDivider.visibility = View.GONE
        }
        if (equipmentDataList[position].name == "General HVAC (No Appliance)" ||
            equipmentDataList[position].name == "General Lighting (No Appliance)" ||
            equipmentDataList[position].name == "General Plumbing (No Appliance)")
        {
                holder.equipmentName.text = equipmentDataList[position].name
                holder.grayDropDown.visibility = View.GONE
                holder.locationName.visibility = View.GONE
                val params = holder.equipmentLocationLayout.layoutParams
                params.height = 150
        }
        else {
            equipmentExpansion(holder, position)
        }

    }

    //Function that creates the expanded equipment view for an equipment piece, when
    //a user clicks on the equipment, it will drop down and show the details
    private fun equipmentExpansion(holder: ViewHolder, position: Int){
        holder.equipmentName.text = equipmentDataList[position].name
        setElementTexts(holder.manufacturerInfo, equipmentDataList[position].manufacturer)
        setElementTexts(holder.modelInfo, equipmentDataList[position].modelNumber)
        setElementTexts(holder.locationInfo, equipmentDataList[position].location)
        setElementTexts(holder.serialInfo, equipmentDataList[position].serialNumber)
        setElementTexts(holder.lastServiceByInfo, equipmentDataList[position].lastServiceBy)
        setElementTexts(holder.lastServiceDateInfo, equipmentDataList[position].lastServiceDate)

        holder.select.setOnClickListener{
            val id = equipmentDataList[position].id
            val type = equipmentDataList[position].type
            val intent = Intent(applicationContext, ChooseServiceProvider::class.java)
            intent.putExtra("equipment", id)
            intent.putExtra("type", type.getIntRepr())
            applicationContext.startActivity(intent)
        }

        holder.equipmentView.setOnClickListener {
            TransitionManager.beginDelayedTransition(holder.transitionsContainer)
            holder.visible = !holder.visible
            val v = if (holder.visible) View.VISIBLE else View.GONE
            setVisibility(holder, v)
            val rotate = if (holder.visible) 180f else 0f
            holder.greenDropDown.rotation = rotate
            var params = holder.equipmentLayout.layoutParams
            var p = if (holder.visible) 500 else 90
            params.height = p
            if (holder.locationSpace) {
                val params = holder.equipmentLocationLayout.layoutParams
                val p = if (holder.visible) 500 else 90
                params.height = p
            } else {
                params = holder.equipmentLocationLayout.layoutParams
                p = if (holder.visible) 600 else 190
                params.height = p
            }
            if (holder.visible) {
                holder.equipmentName.setTextColor(Color.parseColor("#00CA8F"))
                holder.equipmentLayout.setBackgroundResource(R.drawable.green_button_border)
                holder.grayDropDown.visibility = View.GONE
            } else {
                holder.equipmentName.setTextColor(Color.parseColor("#333232"))
                holder.equipmentLayout.setBackgroundResource(R.drawable.gray_button_border)
                holder.grayDropDown.visibility = View.VISIBLE
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setVisibility(holder, View.GONE)
        var initial = holder.equipmentLayout.layoutParams
        initial.height = 90
        holder.locationDivider.visibility = View.VISIBLE
        holder.locationName.visibility = View.VISIBLE
        holder.locationSpace = false
        if (!locations.contains(equipmentDataList[position].location.toUpperCase())) {
            holder.locationName.text = equipmentDataList[position].location.toUpperCase()
            locations.add(equipmentDataList[position].location.toUpperCase())
        } else {
            holder.locationName.visibility = View.GONE
            holder.locationDivider.visibility = View.GONE
            holder.locationSpace = true
        }

        initial = holder.equipmentLocationLayout.layoutParams
        if (holder.locationSpace) {
            initial.height = 90
        } else {
            initial.height = 190
        }
        configureElements(holder, position)
    }
    private fun setElementTexts(element: TextView, str: String){
        if(str.isEmpty() || str == "null") element.text = "--"
        else element.text = str

    }

    override fun getItemCount(): Int {
        return equipmentDataList.size
    }

    //Switches the visibility of Equipment UI elements
    private fun setVisibility(holder: ViewHolder, v: Int) {
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
        holder.greenDropDown.visibility = v
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var visible: Boolean = false
        var equipmentName: TextView = itemView.findViewById(R.id.equipmentName)
        var locationName : TextView = itemView.findViewById(R.id.location_name)
        var transitionsContainer : ViewGroup = itemView.findViewById(R.id.relativeLayout)
        var equipmentLocationLayout : ConstraintLayout = transitionsContainer.findViewById(R.id.equipmentLocationLayout)
        var equipmentLayout : ConstraintLayout = transitionsContainer.findViewById(R.id.equipmentLayout)
        var greenDropDown: ImageView = transitionsContainer.findViewById(R.id.dropDown)
        var grayDropDown : ImageView = transitionsContainer.findViewById(R.id.graydropDown)
        var manufacturer: TextView = transitionsContainer.findViewById(R.id.manufacturerInfo)
        var modelNumber: TextView = transitionsContainer.findViewById(R.id.modelInfo)
        var location: TextView = transitionsContainer.findViewById(R.id.locationInfo)
        var serialNumber: TextView = transitionsContainer.findViewById(R.id.serialInfo)
        var manufacturerText : TextView = transitionsContainer.findViewById(R.id.manufacturerText)
        var modelText : TextView = transitionsContainer.findViewById(R.id.modelText)
        var locationText: TextView= transitionsContainer.findViewById(R.id.locationText)
        var serialText: TextView = transitionsContainer.findViewById(R.id.serialText)
        var lastServiceByText: TextView = transitionsContainer.findViewById(R.id.lastServiceByText)
        var lastServiceDateText: TextView = transitionsContainer.findViewById(R.id.lastServiceDateText)
        var equipmentDivider: ImageView = transitionsContainer.findViewById(R.id.equipmentDivider)
        var locationDivider: ImageView = transitionsContainer.findViewById(R.id.locationDivider)
        var manufacturerInfo: TextView= transitionsContainer.findViewById(R.id.manufacturerInfo)
        var modelInfo: TextView = transitionsContainer.findViewById(R.id.modelInfo)
        var locationInfo: TextView= transitionsContainer.findViewById(R.id.locationInfo)
        var serialInfo: TextView = transitionsContainer.findViewById(R.id.serialInfo)
        var lastServiceByInfo: TextView = transitionsContainer.findViewById(R.id.lastServiceByInfo)
        var lastServiceDateInfo: TextView = transitionsContainer.findViewById(R.id.lastServiceDateInfo)
        var select: Button = transitionsContainer.findViewById(R.id.select)
        var equipmentView : View= transitionsContainer.findViewById(R.id.view)
        var locationSpace = false

    }
    fun filter(typeText:  String) {
        equipmentDataList.clear()
        locations.clear()
        val charText = typeText.toLowerCase((Locale.getDefault()))
        if (charText.isEmpty()) {
            equipmentDataList.addAll(equipmentData)
        } else {
            for (equipmentName in equipmentData) {
                if (equipmentName.name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    equipmentDataList.add(equipmentName)
                }
            }
        }
        notifyDataSetChanged()
    }
}


