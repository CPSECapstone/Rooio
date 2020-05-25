package com.rooio.repairs

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


//The overarching class that any Activity inherits from so that they can use REST API functionality
//and access information such as the user token, location, or first name
abstract class RestApi : AppCompatActivity() {

    val BaseUrl = "https://capstone.api.roopairs.com/v0/"

    //Static objects that can be accessed from any Activity
    companion object {
        var queue: RequestQueue? = null
        lateinit var userToken: String
        lateinit var userName: String
        lateinit var userLocationID: String
        lateinit var employeeId: String
        var savedStreamMuted = true
        lateinit var am : AudioManager
    }


    //Sends a request to the API or mocks the API call if it is a test
    fun requestJson(type: Int?, jsonType: JsonType?, request: JsonRequest) {
        if (!request.isTest) {
            when (jsonType) {
                JsonType.OBJECT -> {
                    val jsonObjectRequest = createJsonObjectRequest(type, request)
                    addToVolleyQueue(jsonObjectRequest)
                }
                JsonType.ARRAY -> {
                    val jsonArrayRequest = createJsonArrayRequest(type, request)
                    addToVolleyQueue(jsonArrayRequest)
                }
            }
        }
    }

    //Creates a JSONObject request based on REST type and information provided
    private fun createJsonObjectRequest(type: Int?, request: JsonRequest): JsonObjectRequest {
        val url = BaseUrl + request.url
        val responseFunc = request.responseFunc
        val errorFunc = request.errorFunc
        val headersFlag = request.headersFlag
        val params = request.params



        val  jsonParams : JSONObject? = if (!request.params.isNullOrEmpty()) {
            JSONObject(params)
        } else null

        return object : JsonObjectRequest(type!!, url, jsonParams,
                Response.Listener { input: JSONObject -> responseFunc.apply(input) },
                Response.ErrorListener { error: VolleyError -> errorFunc.apply(errorMsgHandler(error)) }) {
            override fun getHeaders(): Map<String, String> {
                //If true is given through headersFlag parameter the Post request will be sent with Headers
                return if (headersFlag) {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Token $userToken"
                    headers
                 //If false is given through headersFlag parameter the Post request will not be sent with Headers
                } else {
                    emptyMap()
                }
            }
        }
    }

    //Creates a JSONArray request based on REST type and information provided
    private fun createJsonArrayRequest(type: Int?, request: JsonRequest): JsonArrayRequest {
        val url = BaseUrl + request.url
        val responseFunc = request.responseFunc
        val errorFunc = request.errorFunc
        val headersFlag = request.headersFlag
        val params = request.params

        val  jsonParams : JSONObject? = if (!request.params.isNullOrEmpty()) {
            JSONObject(params)
        } else null

        return object : JsonArrayRequest(type!!, url, jsonParams,
                Response.Listener { input: JSONArray -> responseFunc.apply(input) },
                Response.ErrorListener { error: VolleyError -> errorFunc.apply(errorMsgHandler(error)) }) {
            override fun getHeaders(): Map<String, String> {
                //If true is given through headersFlag parameter the Post request will be sent with Headers
                return if (headersFlag) {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Token $userToken"
                    headers
                 //If false is given through headersFlag parameter the Post request will not be sent with Headers
                } else {
                    emptyMap()
                }
            }
        }
    }

    //Sends a JSONObject request to the API
    private fun addToVolleyQueue(request: JsonObjectRequest) {
        if (queue == null) {
            queue = Volley.newRequestQueue(applicationContext)
        }
        queue!!.add(request)
    }

    //Sends a JSONArray request to the API
    private fun addToVolleyQueue(request: JsonArrayRequest) {
        if (queue == null) {
            queue = Volley.newRequestQueue(applicationContext)
        }
        queue!!.add(request)
    }

    //Handles volley error messages if there is a server error
    private fun errorMsgHandler(error: VolleyError): String {
        var errorMsg = "Unexpected Error!"
        if (error is TimeoutError || error is NoConnectionError) {
            errorMsg = "No connection or you timed out. Try again."
        } else if (error is AuthFailureError) {
            errorMsg = "You are not authorized."
        } else if (error is ServerError) {
            errorMsg = "Does not exist."
        } else if (error is NetworkError) {
            errorMsg = "Network Error. Try again."
        } else if (error is ParseError) {
            errorMsg = "Parse Error. Try again."
        }
        return errorMsg
    }

    // Sets the navigation bar onto the page
    fun setNavigationBar() {
        //sets the navigation bar onto the page
        val navInflater = layoutInflater
        val tmpView = navInflater.inflate(R.layout.activity_navigation_bar, null)

        window.addContentView(tmpView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    // Sets the action bar onto the page
    fun setActionBar() {
        //sets the action bar onto the page
        val actionbarInflater = layoutInflater
        val actionbarView = actionbarInflater.inflate(R.layout.action_bar, null)
        window.addContentView(actionbarView,
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        supportActionBar!!.elevation = 0.0f
    }

    //Centers "Repairs" title on pages without navigation
    fun centerTitleBar() {
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        supportActionBar!!.elevation = 0f
    }
  
    //Functions to mute sound
    public override fun onResume() {
        super.onResume()
        am = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!am.isStreamMute(AudioManager.STREAM_SYSTEM)) {
                savedStreamMuted = true
                am.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0)
            }
        }
    }

    public override fun onPause() {
        super.onPause()
        am = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (savedStreamMuted) {
                am.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0)
                savedStreamMuted = false
            }
        }
    }

}