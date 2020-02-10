package com.rooio.repairs

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.arch.core.util.Function
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Login : RestApi() {
    private var usernameField: EditText? = null
    private var passwordField: EditText? = null
    private var connectAccount: Button? = null
    private var cancelLogin: Button? = null
    private var errorMessage: TextView? = null
    private val url = "https://capstone.api.roopairs.com/v0/auth/login/"

    override fun onCreate(savedInstanceState: Bundle?) { //Load activity view
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Centers "Repairs" title
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        supportActionBar!!.elevation = 0f
        //Initializing UI variables
        connectAccount = findViewById(R.id.connectAccount)
        usernameField = findViewById(R.id.usernameField)
        passwordField = findViewById(R.id.passwordField)
        cancelLogin = findViewById(R.id.cancelLogin)
        errorMessage = findViewById(R.id.errorMessage)
        errorMessage!!.text = ""
        onConnectAccount()
        onCancel()
    }

    // Attempts to log in the user after clicking Connect Account
    private fun onConnectAccount() {
        val params = HashMap<String, Any>()
        connectAccount!!.setOnClickListener {
            params["username"] = usernameField!!.text.toString()
            params["password"] = passwordField!!.text.toString()
            errorMessage!!.setTextColor(Color.parseColor("#A6A9AC"))
            errorMessage!!.text = "Loading"
            val request = JsonRequest(false, url, params, responseFunc, errorFunc, false)
            sendLoginInfo(Volley.newRequestQueue(applicationContext), request,
                    createJsonObjectRequest(createJsonParams(request), request))
        }
    }

    // Switches page from Login to Landing after clicking Cancel
    private fun onCancel() {
        cancelLogin!!.setOnClickListener { startActivity(Intent(this@Login, Landing::class.java)) }
    }

    @JvmField
    var responseFunc = Function<Any, Void?> { jsonObj: Any ->
        try {
            errorMessage!!.text = ""
            storeToken(jsonObj)
            startActivity(Intent(this@Login, LocationLogin::class.java))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        null
    }
    @JvmField
    var errorFunc = Function<String, Void?> {
        errorMessage!!.setTextColor(Color.parseColor("#E4E40B0B"))
        errorMessage!!.setText(R.string.errorLogin)
        null
    }

    // -- Example Json handling function
    @Throws(JSONException::class)
    fun storeToken(responseObj: Any) {
        val jsonResponseObject = responseObj as JSONObject
        val token = jsonResponseObject.get("token") as String
        userToken = token
    }

    // Sends username and password to the API and loads the next screen.
// Returns error if the information is invalid
    fun sendLoginInfo(queue: RequestQueue, request: JsonRequest, req: JsonObjectRequest) {
        requestPostJsonObj(queue, req)
    }
}