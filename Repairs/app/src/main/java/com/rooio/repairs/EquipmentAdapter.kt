package com.rooio.repairs

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

// Custom adapter for choosing a service provider with a special view
class EquipmentAdapter(private val applicationContext: Context, private val dataList: ArrayList<EquipmentData>, private val equipmentId: Int):
        RecyclerView.Adapter<EquipmentAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(applicationContext)
    private val locations: ArrayList<String> = ArrayList()

    //Creates the view for every item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = inflater.inflate(R.layout.equipment_list_item, parent, false)
        return MyViewHolder(view)
    }

    //Updates the view for the correct item information based on position
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.button.setOnClickListener {
            val intent = Intent(applicationContext, Equipment::class.java)
            intent.putExtra("equipmentPosition", position)
            intent.putExtra("equipmentId", dataList[position].id)
            applicationContext.startActivity(intent)
        }
        if (equipmentId == position) {
            holder.button.setBackgroundResource(R.drawable.green_button_border)
            holder.button.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
        } else {
            holder.button.setBackgroundResource(R.drawable.dark_gray_button_border)
            holder.button.setTextColor(ContextCompat.getColor(applicationContext, R.color.darkGray))
        }
        holder.button.text = dataList[position].name
        val location = dataList[position].location
        if (!locations.contains(location.toUpperCase(Locale.US))) {
            holder.location.text = location.toUpperCase(Locale.US)
            locations.add(location.toUpperCase(Locale.US))
        } else holder.location.visibility = View.GONE
    }

    //Gets number of providers
    override fun getItemCount(): Int {
        return dataList.size
    }

    //The view created
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val location : TextView = itemView.findViewById(R.id.location)
        val button : Button = itemView.findViewById(R.id.equipmentItem)
    }
}