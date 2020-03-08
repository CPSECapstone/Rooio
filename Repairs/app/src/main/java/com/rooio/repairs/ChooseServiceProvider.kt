package com.rooio.repairs

import android.os.Bundle
import android.view.ViewGroup
import androidx.arch.core.util.Function
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import org.json.JSONArray

class ChooseServiceProvider : RestApi() {

    private lateinit var serviceProviderList: RecyclerView
    private lateinit var searchBar : AppCompatEditText
    private var providerDataList: ArrayList<ProviderData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_service_provider)

        initializeVariables()
        setActionBar()
        populateList()
    }

    // Sets the action bar onto the page
    private fun setActionBar() {
        //sets the action bar onto the page
        val actionbarInflater = layoutInflater
        val actionbarView = actionbarInflater.inflate(R.layout.action_bar, null)
        window.addContentView(actionbarView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        supportActionBar!!.elevation = 0.0f
    }

    //Initializes UI variables
    private fun initializeVariables() {
        serviceProviderList = findViewById(R.id.serviceProviderList)
        searchBar = findViewById(R.id.searchBar)
    }

    //Populates a list from API call
    private fun populateList() {
        val request = JsonRequest(false, BaseUrl + "service-providers/", HashMap(), providerResponseFunc, providerErrorFunc, true)
        requestJson(Request.Method.GET, JsonType.ARRAY, request)
    }

    @JvmField
    // load equipments in the equipment list
    var providerResponseFunc = Function<Any, Void?> { response: Any? ->
        val jsonArray = response as JSONArray
        loadServiceProviders(jsonArray)
        null
    }

    @JvmField
    // set error message
    var providerErrorFunc = Function<String, Void?> { error: String? ->
        //textView.text = string
        //textView.setTextColor(ContextCompat.getColor(this,R.color.Red))
        null
    }

    private fun loadServiceProviders(response: JSONArray) {
        providerDataList.clear()
        for (i in 0 until response.length()) {
            val provider = ProviderData(response.getJSONObject(i))
            providerDataList.add(provider)
        }
        changeAdapter()
    }

    private fun changeAdapter() {
        val layoutManager = LinearLayoutManager(this)
        serviceProviderList.layoutManager = layoutManager
        serviceProviderList.adapter = ChooseServiceProviderAdapter(this, providerDataList)
    }
}