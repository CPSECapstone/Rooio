package com.rooio.repairs

import androidx.appcompat.app.AppCompatActivity
import androidx.arch.core.util.Function
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

    //Creates a JSONObject request based on REST type and information provided
    private fun createJsonObjectRequest(type: Int?, request: JsonRequest): JsonObjectRequest {
        val url = BaseUrl + request.url
        val responseFunc = request.responseFunc
        val errorFunc = request.errorFunc
        val headersFlag = request.headersFlag

        var  jsonParams : JSONObject? = if (!request.params.isNullOrEmpty()) {
            JSONObject(request.params)
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

        var  jsonParams : JSONObject? = if (!request.params.isNullOrEmpty()) {
            JSONObject(request.params)
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

    fun requestPostJsonObj(req: JsonRequest) {
        if (req.isTest) {
            return
        }
        val url = req.url
        val params = req.params
        val responseFunc = req.responseFunc
        val errorFunc = req.errorFunc
        val headersFlag = req.headersFlag
        //-- Transforms params HashMap into Json Object
        val jsonParams = JSONObject(params)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener { response -> responseFunc.apply(response) }, Response.ErrorListener { error ->
            val errorMsg = errorMsgHandler(error)
            errorFunc.apply(errorMsg)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> { //                  ----->  If true is given through headersFlag parameter the Post request will be sent with Headers
                return if (headersFlag) {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Token $userToken" //<-- Token in Abstract Class RestApi
                    headers
                    //                  ----->  If false is given through headersFlag parameter the Post request will not be sent with Headers
                } else {
                    emptyMap()
                }
            }
        }
        addToVolleyQueue(jsonObjectRequest)
    }

    fun requestDeleteJsonObj(req: JsonRequest) {
        if (req.isTest) {
            return
        }
        val url = req.url
        val params = req.params
        val responseFunc = req.responseFunc
        val errorFunc = req.errorFunc
        val headersFlag = req.headersFlag
        //-- Transforms params HashMap into Json Object
        val jsonParams = JSONObject(params)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.DELETE, url, jsonParams, Response.Listener { response -> responseFunc.apply(response) }, Response.ErrorListener { error ->
            val errorMsg = errorMsgHandler(error)
            errorFunc.apply(errorMsg)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> { //                  ----->  If true is given through headersFlag parameter the Post request will be sent with Headers
                return if (headersFlag) {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Token $userToken" //<-- Token in Abstract Class RestApi
                    headers
                    //                  ----->  If false is given through headersFlag parameter the Post request will not be sent with Headers
                } else {
                    emptyMap()
                }
            }
        }
        addToVolleyQueue(jsonObjectRequest)
    }

    fun requestPutJsonObj(req: JsonRequest) {
        if (req.isTest) {
            return
        }
        val url = req.url
        val params = req.params
        val responseFunc = req.responseFunc
        val errorFunc = req.errorFunc
        val headersFlag = req.headersFlag
        //-- Transforms params HashMap into Json Object
        val jsonParams = JSONObject(params)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.PUT, url, jsonParams, Response.Listener { response -> responseFunc.apply(response) }, Response.ErrorListener { error ->
            val errorMsg = errorMsgHandler(error)
            errorFunc.apply(errorMsg)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> { //                  ----->  If true is given through headersFlag parameter the Post request will be sent with Headers
                return if (headersFlag) {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Token $userToken" //<-- Token in Abstract Class RestApi
                    headers
                    //                  ----->  If false is given through headersFlag parameter the Post request will not be sent with Headers
                } else {
                    emptyMap()
                }
            }
        }
        addToVolleyQueue(jsonObjectRequest)
    }

    fun requestGetJsonObj(req: JsonRequest) {
        if (req.isTest) {
            return
        }
        val queue = Volley.newRequestQueue(applicationContext)
        val url = req.url
        val responseFunc = req.responseFunc
        val errorFunc = req.errorFunc
        val headersFlag = req.headersFlag
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null, Response.Listener { response ->
            responseFunc.apply(response)
        }, Response.ErrorListener { error ->
            val errorMsg = errorMsgHandler(error)
            errorFunc.apply(errorMsg)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> { //                  ----->  If true is given through headersFlag parameter the Post request will be sent with Headers
                return if (headersFlag) {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Token $userToken" //<-- Token in Abstract Class RestApi
                    headers
                    //                  ----->  If false is given through headersFlag parameter the Post request will not be sent with Headers
                } else {
                    emptyMap()
                }
            }
        }
        //  --> Equivalent of sending the request. Required to WORK...
        queue.add(jsonObjectRequest)
    }

    fun requestGetJsonArray(req: JsonRequest) {
        if (req.isTest) {
            return
        }
        val queue = Volley.newRequestQueue(applicationContext)
        val url = req.url
        val responseFunc = req.responseFunc
        val errorFunc = req.errorFunc
        val headersFlag = req.headersFlag
        val jsonObjectRequest: JsonArrayRequest = object : JsonArrayRequest(Method.GET, url, Response.Listener { response ->
            responseFunc.apply(response)
        }, Response.ErrorListener { error ->
            val errorMsg = errorMsgHandler(error)
            errorFunc.apply(errorMsg)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> { //                  ----->  If true is given through headersFlag parameter the Post request will be sent with Headers
                return if (headersFlag) {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Token $userToken" //<-- Token in Abstract Class RestApi
                    headers
                    //                  ----->  If false is given through headersFlag parameter the Post request will not be sent with Headers
                } else {
                    emptyMap()
                }
            }
        }
        //  --> Equivalent of sending the request. Required to WORK...
        queue.add(jsonObjectRequest)
    }

    fun requestPostJsonArray(req: JsonRequest) {
        if (req.isTest) {
            return
        }
        val queue = Volley.newRequestQueue(applicationContext)
        val url = req.url
        val params = req.params
        val responseFunc = req.responseFunc
        val errorFunc = req.errorFunc
        val headersFlag = req.headersFlag
        //     -- Transforms params HashMap into Json Object
        val jsonParams = JSONObject(params)
        val jsonObjectRequest: JsonArrayRequest = object : JsonArrayRequest(Method.POST, url, jsonParams, Response.Listener { response ->
            responseFunc.apply(response)
        }, Response.ErrorListener { error ->
            val errorMsg = errorMsgHandler(error)
            errorFunc.apply(errorMsg)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> { //                  ----->  If true is given through headersFlag parameter the Post request will be sent with Headers
                return if (headersFlag) {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Token $userToken" //<-- Token in Abstract Class RestApi
                    headers
                    //                  ----->  If false is given through headersFlag parameter the Post request will not be sent with Headers
                } else {
                    emptyMap()
                }
            }
        }
        //  --> Equivalent of sending the request. Required to WORK...
        queue.add(jsonObjectRequest)
    }

    //____________________________________________________________________
    fun requestGetJsonObj(url: String?, responseFunc: Function<JSONObject?, Void?>,
                          errorFunc: Function<String?, Void?>, headersFlag: Boolean) {
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null, Response.Listener { response ->
            responseFunc.apply(response)
        }, Response.ErrorListener { error ->
            var errorMsg = "Unexpected Error!"
            if (error is TimeoutError || error is NoConnectionError) {
                errorMsg = "No connection or you timed out. Try again."
            } else if (error is AuthFailureError) {
                errorMsg = "You are not authorized."
            } else if (error is ServerError) {
                errorMsg = "does not exist."
            } else if (error is NetworkError) {
                errorMsg = "Network Error. Try again."
            } else if (error is ParseError) {
                errorMsg = "Parse Error. Try again."
            }
            errorFunc.apply(errorMsg)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> { //                  ----->  If true is given through headersFlag parameter the Post request will be sent with Headers
                return if (headersFlag) {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Token $userToken" //<-- Token in Abstract Class RestApi
                    headers
                    //                  ----->  If false is given through headersFlag parameter the Post request will not be sent with Headers
                } else {
                    emptyMap()
                }
            }
        }
        addToVolleyQueue(jsonObjectRequest)
    }

    fun requestPostJsonObj(url: String?, params: HashMap<String?, Any?>?,
                           responseFunc: Function<JSONObject?, Void?>, errorFunc: Function<String?, Void?>, headersFlag: Boolean) { //     -- Transforms params HashMap into Json Object
        val jsonParams = JSONObject(params)
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener { response ->
            responseFunc.apply(response)
        }, Response.ErrorListener { error ->
            var errorMsg = "Unexpected Error!"
            if (error is TimeoutError || error is NoConnectionError) {
                errorMsg = "No connection or you timed out. Try again."
            } else if (error is AuthFailureError) {
                errorMsg = "You are not authorized."
            } else if (error is ServerError) {
                errorMsg = "does not exist."
            } else if (error is NetworkError) {
                errorMsg = "Network Error. Try again."
            } else if (error is ParseError) {
                errorMsg = "Parse Error. Try again."
            }
            errorFunc.apply(errorMsg)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> { //                  ----->  If true is given through headersFlag parameter the Post request will be sent with Headers
                return if (headersFlag) {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Token $userToken" //<-- Token in Abstract Class RestApi
                    headers
                    //                  ----->  If false is given through headersFlag parameter the Post request will not be sent with Headers
                } else {
                    emptyMap()
                }
            }
        }
        addToVolleyQueue(jsonObjectRequest)
    }

    fun requestGetJsonArray(url: String?, responseFunc: Function<JSONArray?, Void?>,
                            errorFunc: Function<String?, Void?>, headersFlag: Boolean) {
        val jsonObjectRequest: JsonArrayRequest = object : JsonArrayRequest(Method.GET, url, Response.Listener { response ->
            responseFunc.apply(response)
        }, Response.ErrorListener { error ->
            var errorMsg = "Unexpected Error!"
            if (error is TimeoutError || error is NoConnectionError) {
                errorMsg = "No connection or you timed out. Try again."
            } else if (error is AuthFailureError) {
                errorMsg = "You are not authorized."
            } else if (error is ServerError) {
                errorMsg = "does not exist."
            } else if (error is NetworkError) {
                errorMsg = "Network Error. Try again."
            } else if (error is ParseError) {
                errorMsg = "Parse Error. Try again."
            }
            errorFunc.apply(errorMsg)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> { //                  ----->  If true is given through headersFlag parameter the Post request will be sent with Headers
                return if (headersFlag) {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Token $userToken" //<-- Token in Abstract Class RestApi
                    headers
                    //                  ----->  If false is given through headersFlag parameter the Post request will not be sent with Headers
                } else {
                    emptyMap()
                }
            }
        }
        addToVolleyQueue(jsonObjectRequest)
    }

    fun requestPostJsonArray(url: String?, params: HashMap<String?, Any?>?,
                             responseFunc: Function<JSONArray?, Void?>, errorFunc: Function<String?, Void?>, headersFlag: Boolean) { //     -- Transforms params HashMap into Json Object
        val jsonParams = JSONObject(params)
        val jsonObjectRequest: JsonArrayRequest = object : JsonArrayRequest(Method.POST, url, jsonParams, Response.Listener { response ->
            responseFunc.apply(response)
        }, Response.ErrorListener { error ->
            var errorMsg = "Unexpected Error!"
            if (error is TimeoutError || error is NoConnectionError) {
                errorMsg = "No connection or you timed out. Try again."
            } else if (error is AuthFailureError) {
                errorMsg = "You are not authorized."
            } else if (error is ServerError) {
                errorMsg = "does not exist."
            } else if (error is NetworkError) {
                errorMsg = "Network Error. Try again."
            } else if (error is ParseError) {
                errorMsg = "Parse Error. Try again."
            }
            errorFunc.apply(errorMsg)
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> { //                  ----->  If true is given through headersFlag parameter the Post request will be sent with Headers
                return if (headersFlag) {
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Token $userToken" //<-- Token in Abstract Class RestApi
                    headers
                    //                  ----->  If false is given through headersFlag parameter the Post request will not be sent with Headers
                } else {
                    emptyMap()
                }
            }
        }
        addToVolleyQueue(jsonObjectRequest)
    }


}