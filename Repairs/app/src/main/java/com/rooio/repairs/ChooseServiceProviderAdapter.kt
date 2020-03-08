package com.rooio.repairs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class ChooseServiceProviderAdapter(ctx: Context, dataList: ArrayList<ProviderData>) :
RecyclerView.Adapter<ChooseServiceProviderAdapter.MyViewHolder>() {

    private val providerDataList = dataList
    private val inflater: LayoutInflater = LayoutInflater.from(ctx)
    private val providerData: ArrayList<ProviderData> = ArrayList()

    init {
        this.providerData.addAll(providerDataList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = inflater.inflate(R.layout.provider_item, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        setElement(holder.nameInfo, providerDataList[position].name)
        setElement(holder.locationInfo, providerDataList[position].physicalAddress)
        setElement(holder.skillsInfo, providerDataList[position].skills.toString())
        setElement(holder.startingHourlyRateInfo, providerDataList[position].startingHourlyRate)
    }

    private fun setElement(element: TextView, str: String){
        if(str.isEmpty() || str == "null")
            element.text = "--"
        else
            element.text = str
    }

    override fun getItemCount(): Int {
        return providerDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameInfo: TextView = itemView.findViewById(R.id.nameInfo)
        var locationInfo: TextView = itemView.findViewById(R.id.locationInfo)
        var skillsInfo: TextView = itemView.findViewById(R.id.skillsInfo)
        var startingHourlyRateInfo: TextView  = itemView.findViewById(R.id.startingHourlyRateInfo)
        var logoImage: ImageView  = itemView.findViewById(R.id.logoImage)
    }

    // Filter Class
    fun filter(charText: String) {
        charText.toLowerCase(Locale.getDefault())
        providerDataList.clear()
        if (charText.isEmpty()) {
            providerDataList.addAll(providerData)
        } else {
            for (wp in providerData) {
                if (wp.name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    providerDataList.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }

}