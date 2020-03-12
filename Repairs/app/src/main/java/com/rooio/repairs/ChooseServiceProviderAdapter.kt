package com.rooio.repairs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList


// Custom adapter for choosing a service provider with a special view
class ChooseServiceProviderAdapter(private val applicationContext: Context, dataList: ArrayList<ProviderData>, private val equipmentId: String) :
RecyclerView.Adapter<ChooseServiceProviderAdapter.MyViewHolder>() {

    private val providerDataList = dataList
    private val inflater: LayoutInflater = LayoutInflater.from(applicationContext)
    private val providerData: ArrayList<ProviderData> = ArrayList()

    //Adds every provider from the API to the adapter
    init {
        this.providerData.addAll(providerDataList)
    }

    //Creates the view for every item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = inflater.inflate(R.layout.provider_item, parent, false)
        return MyViewHolder(view)
    }

    //Updates the view for the correct item information based on position
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            val id = providerDataList[position].id
            val intent = Intent(applicationContext, CreateJobDetails::class.java)
            intent.putExtra("company", id)
            intent.putExtra("equipment", equipmentId)
            val activity = applicationContext as Activity
            intent.putExtra("type", activity.intent.getIntExtra("type", 0))
            applicationContext.startActivity(intent)
        }
        setElement(holder.nameInfo, providerDataList[position].name)
        setElement(holder.locationInfo, providerDataList[position].physicalAddress.toUpperCase(Locale.getDefault()))
        setElement(holder.skillsInfo, setSkills(providerDataList[position].skills))
        setHourlyRate(holder.startingHourlyRateInfo, providerDataList[position].startingHourlyRate)
        setImage(holder.logoImage, providerDataList[position].logo)
    }

    //Sets the image in the view
    private fun setImage(element: ImageView, str: String) {
        element.visibility = View.VISIBLE
        if(str.isNotEmpty()) {
            Picasso.with(applicationContext)
                    .load(str)
                    .into(element)
        }
        else {
            element.visibility = View.GONE
        }
    }

    //Sets the element in the view, and -- if it is null
    private fun setElement(element: TextView, str: String){
        if(str.isEmpty() || str == "null")
            element.text = "--"
        else
            element.text = str
    }

    //Sets the skills text for the item by parsing an array of skills
    private fun setSkills(skills: ArrayList<String>): String {
        var text = ""
        val comma = ", "
        for (skill in skills) {
            text += "$skill$comma"
        }
        return text.subSequence(0, text.length - 2) as String
    }

    //Sets the hourly rate for a view
    private fun setHourlyRate(element: TextView, str: String) {
        val hoursText = applicationContext.getString(R.string.details_price_exception_message, str)
        val standardText = SpannableStringBuilder(" starting cost")
        standardText.setSpan(ForegroundColorSpan(Color.parseColor("#00CA8F")), 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        element.text = standardText.insert(1, "$hoursText ")
    }

    //Gets number of providers
    override fun getItemCount(): Int {
        return providerDataList.size
    }

    //The view created
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameInfo: TextView = itemView.findViewById(R.id.nameInfo)
        var locationInfo: TextView = itemView.findViewById(R.id.locationInfo)
        var skillsInfo: TextView = itemView.findViewById(R.id.skillsInfo)
        var startingHourlyRateInfo: TextView  = itemView.findViewById(R.id.startingHourlyRateInfo)
        var logoImage: ImageView  = itemView.findViewById(R.id.logoImage)
    }

    // Filters class: filters by name of service provider
    fun filter(typeText: String) {
        val charText = typeText.toLowerCase(Locale.getDefault())
        providerDataList.clear()
        if (charText.isEmpty()) {
            providerDataList.addAll(providerData)
        } else {
            for (provider in providerData) {
                if (provider.name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    providerDataList.add(provider)
                }
            }
        }
        notifyDataSetChanged()
    }

}