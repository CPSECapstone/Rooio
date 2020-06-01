package com.rooio.repairs


import android.os.Bundle

//Displays current service locations attached to the account on first login
class LocationLogin : Location() {

    private val url = "service-locations/"
    private val jsonRequest = JsonRequest(false, url, null, responseFunc, errorFunc, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_login)

        onResume()
        centerTitleBar()
        initializeCommonVariables(LocationType.LOGIN)
        loadLocations(jsonRequest)
        onAddAnother()
        onPause()
    }


    //Animation function for nav bar
    override fun animateActivity(boolean: Boolean) {
        //Not implemented because no navigation bar
    }
}
