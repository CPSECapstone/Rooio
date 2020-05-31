package com.rooio.repairs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.json.JSONException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DashboardCustomAdapter (private val context: Context, private val dataList: ArrayList<JobData>): RecyclerView.Adapter<DashboardCustomAdapter.MyViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    //notable_job_list view
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val repairType: TextView = itemView.findViewById(R.id.repairType)
        var name: TextView = itemView.findViewById(R.id.name)
        var address: TextView = itemView.findViewById(R.id.address)
        var image: ImageView = itemView.findViewById(R.id.image)
        var timeText: TextView = itemView.findViewById(R.id.timeText)
        var jobsButton: Button = itemView.findViewById(R.id.jobsButton)
        var color: ConstraintLayout = itemView.findViewById(R.id.color)
    }

    //Creates the view for every item in the dataList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = inflater.inflate(R.layout.notable_job_list, parent, false)
        return MyViewHolder(view)
    }

    //Updates the view for the correct item information based on position
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data: JobData = dataList[position]
        try {
            val jobStatus = data.status
            // set color for each job widget
            DrawableCompat.setTint(
                    DrawableCompat.wrap(holder.color.background),
                    ContextCompat.getColor(context, jobStatus.getColor())
            )

            //Sets the Equipment for Job: May be multiple equipment per job
            // Only handling one equipment per job right now
            val equipmentObjList = data.equipmentList
            var jobTitle = "General " + data.strRepr

            if (equipmentObjList.size != 0) {
                val equipmentObj = equipmentObjList[0]
                jobTitle = equipmentObj.name + " " + data.serviceType.toString()
            }

            holder.repairType.text = jobTitle


            //Get Service Location
            val locationObj = data.serviceLocation
            holder.address.text = locationObj.getString("physical_address")

            //Get Restaurant Name
            val internalClient = locationObj.getJSONObject("internal_client")
            holder.name.text = internalClient.getString("name")

            // Setting time text
            if (jobStatus === JobType.STARTED || jobStatus === JobType.PAUSED) {
                if (data.estimatedArrivalTime.isNotEmpty()) {
                    @SuppressLint("SimpleDateFormat") val date1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(convertToNewFormat(data.estimatedArrivalTime, false))
                    @SuppressLint("SimpleDateFormat") val dateFormatter = SimpleDateFormat("EEEE, MMMM d, hh:mm a zzz")
                    holder.timeText.text = dateFormatter.format(date1!!)
                }
            } else if (jobStatus === JobType.DECLINED || jobStatus === JobType.COMPLETED || jobStatus === JobType.CANCELLED) {
                if (data.statusTimeValue.isNotEmpty()) {
                    @SuppressLint("SimpleDateFormat") val date1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(convertToNewFormat(data.statusTimeValue, true))
                    @SuppressLint("SimpleDateFormat") val dateFormatter = SimpleDateFormat("EEEE, MMMM d, hh:mm a zzz")
                    holder.timeText.text = dateFormatter.format(date1!!)
                }
            } else {
                if (data.statusTimeValue.isNotEmpty()) {
                    @SuppressLint("SimpleDateFormat") val date1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(convertToNewFormat(data.statusTimeValue, false))
                    @SuppressLint("SimpleDateFormat") val dateFormatter = SimpleDateFormat("EEEE, MMMM d, hh:mm a zzz")
                    holder.timeText.text = dateFormatter.format(date1!!)
                }
            }
            //Checks if Image
            // If no Image then Move Text Over
            val imageVal = internalClient.getString("logo")
            if (imageVal.isEmpty()) {
                holder.name.translationX = -80f
                holder.repairType.translationX = -80f
                holder.address.translationX = -80f
                holder.image.visibility = View.GONE
            } else {
                Picasso.with(context)
                        .load(internalClient.getString("logo")
                        )
                        .into(holder.image)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //JobId Holds the JobId. ** Displays on UI as a string for testing purposes**
        holder.jobsButton.setOnClickListener {
            val jobId = data.id
            val intent = Intent(context, JobDetails::class.java)
            intent.putExtra("id", jobId)
            context.startActivity(intent)
        }
    }

    //Gets number of providers
    override fun getItemCount(): Int {
        return dataList.size
    }

    @Throws(ParseException::class)
    private fun convertToNewFormat(dateStr: String, status: Boolean): String {
        val utc = TimeZone.getTimeZone("UTC")
        val sourceFormat: SimpleDateFormat = if (status) {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        } else {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        }
        @SuppressLint("SimpleDateFormat") val destFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sourceFormat.timeZone = utc
        val convertedDate = sourceFormat.parse(dateStr)!!
        return destFormat.format(convertedDate)
    }
}