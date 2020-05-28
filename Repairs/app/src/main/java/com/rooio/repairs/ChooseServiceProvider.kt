package com.rooio.repairs

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.arch.core.util.Function
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import org.json.JSONArray

// For job requests, after the user chooses a piece of equipment
class ChooseServiceProvider : RestApi() {

    private lateinit var serviceProviderList: RecyclerView
    private lateinit var searchBar : AppCompatEditText
    private lateinit var preferredButton: Button
    private lateinit var networkButton: Button
    private lateinit var backButton: ImageView
    private lateinit var networkText: TextView
    private lateinit var errorMessage: TextView
    private lateinit var loadingPanel: ProgressBar
    private var providerDataList: ArrayList<ProviderData> = ArrayList()
    private lateinit var adapter: ChooseServiceProviderAdapter
    private var equipmentId: String = "1"
    private var equipmentType: Int = 1
    private lateinit var noProviderMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_service_provider)

        // gets rid of sound when the user clicks on the spinner when editing the equipment type
        onResume()

        initializeVariables()
        setActionBar()
        populateList()
        onPreferred()
        onNetwork()
        onBackClick()
        onPause()
    }

    //Initializes UI variables
    private fun initializeVariables() {
        serviceProviderList = findViewById(R.id.serviceProviderList)
        searchBar = findViewById(R.id.searchBar)
        preferredButton = findViewById(R.id.preferredButton)
        networkButton = findViewById(R.id.networkButton)
        networkText = findViewById(R.id.networkText)
        errorMessage = findViewById(R.id.errorMessage)
        backButton = findViewById(R.id.backButton)
        loadingPanel = findViewById(R.id.loadingPanel)
        noProviderMessage = findViewById(R.id.noProvider)
    }

    //Click to go back to Dashboard
    private fun onBackClick() {
        backButton.setOnClickListener{
            val intent = Intent(this@ChooseServiceProvider, ChooseEquipment::class.java)
            intent.putExtra("equipmentType", equipmentType)
            startActivity(intent)
        }
    }

    //Populates a list from API call
    private fun populateList() {
        loadingPanel.visibility = View.VISIBLE
        val id = intent.getStringExtra("equipment")
        if (id != null) equipmentId = id
        else equipmentId = ""
        equipmentType = intent.getIntExtra("type", 1)
        val request = JsonRequest(false, "service-providers/?servicable_equipment_type_id=$equipmentType", null, providerResponseFunc, providerErrorFunc, true)
        requestJson(Request.Method.GET, JsonType.ARRAY, request)
    }

    //Response from the API call after getting providers
    @JvmField
    var providerResponseFunc = Function<Any, Void?> { response: Any? ->
        loadingPanel.visibility = View.GONE
        val jsonArray = response as JSONArray
        loadServiceProviders(jsonArray)
        null
    }

    //Response from the API call if there is an error
    @JvmField
    var providerErrorFunc = Function<String, Void?> { error: String? ->
        loadingPanel.visibility = View.GONE
        errorMessage.text = error
        null
    }

    //Parses JSONArray for service provider information
    private fun loadServiceProviders(response: JSONArray) {
        providerDataList.clear()
        for (i in 0 until response.length()) {
            val provider = ProviderData(response.getJSONObject(i))
            providerDataList.add(provider)
        }
        changeAdapter()
    }

    // When preferred tab is clicked, set page to list
    private fun onPreferred() {
        preferredButton.setOnClickListener{
            preferredButton.setBackgroundResource(R.drawable.provider_tab)
            preferredButton.setTextColor(Color.parseColor("#00CA8F"))
            networkButton.setBackgroundResource(R.drawable.blank_border)
            networkButton.setTextColor(Color.parseColor("#6D7377"))
            networkText.visibility = View.GONE
            serviceProviderList.visibility = View.VISIBLE
        }
    }

    // When network tab is clicked, set page to message
    private fun onNetwork() {
        networkButton.setOnClickListener{
            networkButton.setBackgroundResource(R.drawable.provider_tab)
            networkButton.setTextColor(Color.parseColor("#00CA8F"))
            preferredButton.setBackgroundResource(R.drawable.blank_border)
            preferredButton.setTextColor(Color.parseColor("#6D7377"))
            networkText.visibility = View.VISIBLE
            serviceProviderList.visibility = View.GONE
        }
    }

    //Sets the list adapter to a custom one that handles providers
    private fun changeAdapter() {
        adapter = ChooseServiceProviderAdapter(this, providerDataList, equipmentId)
        val layoutManager = LinearLayoutManager(this)
        serviceProviderList.layoutManager = layoutManager
        serviceProviderList.adapter = adapter
        if (adapter.checkList() == 0)
        {
            //make the no equipment message visible
            noProviderMessage.visibility = View.VISIBLE
        }
        else {
            noProviderMessage.visibility = View.INVISIBLE
        }

        setFilter()
    }

    //Sets the filter on the list
    private fun setFilter() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                adapter.filter(charSequence.toString())
                if (adapter.checkList() == 0)
                {
                    //make the no equipment message visible
                    noProviderMessage.visibility = View.VISIBLE
                }
                else {
                    noProviderMessage.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })

    }

}