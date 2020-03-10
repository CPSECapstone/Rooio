package com.rooio.repairs

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.arch.core.util.Function
import androidx.appcompat.widget.AppCompatEditText
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
    private lateinit var networkText: TextView
    private lateinit var errorMessage: TextView
    private var providerDataList: ArrayList<ProviderData> = ArrayList()
    private lateinit var adapter: ChooseServiceProviderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_service_provider)

        initializeVariables()
        setActionBar()
        populateList()
        setFilter()
        onPreferred()
        onNetwork()
    }

    //Initializes UI variables
    private fun initializeVariables() {
        serviceProviderList = findViewById(R.id.serviceProviderList)
        searchBar = findViewById(R.id.searchBar)
        preferredButton = findViewById(R.id.preferredButton)
        networkButton = findViewById(R.id.networkButton)
        networkText = findViewById(R.id.networkText)
        errorMessage = findViewById(R.id.errorMessage)
    }

    //Populates a list from API call
    private fun populateList() {
        val request = JsonRequest(false, "service-providers/", HashMap(), providerResponseFunc, providerErrorFunc, true)
        requestJson(Request.Method.GET, JsonType.ARRAY, request)
    }

    //Response from the API call after getting providers
    @JvmField
    var providerResponseFunc = Function<Any, Void?> { response: Any? ->
        val jsonArray = response as JSONArray
        loadServiceProviders(jsonArray)
        null
    }

    //Response from the API call if there is an error
    @JvmField
    var providerErrorFunc = Function<String, Void?> { error: String? ->
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
        val bundle: Bundle? = intent.extras
        var equipmentId = ""
        if (bundle != null) {
            equipmentId = bundle.getString("equipment") as String
        }
        adapter = ChooseServiceProviderAdapter(this, providerDataList, equipmentId)
        val layoutManager = LinearLayoutManager(this)
        serviceProviderList.layoutManager = layoutManager
        serviceProviderList.adapter = adapter
    }

    //Sets the filter on the list
    private fun setFilter() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                adapter.filter(charSequence.toString())
            }
            override fun afterTextChanged(editable: Editable) {}
        })
    }
}