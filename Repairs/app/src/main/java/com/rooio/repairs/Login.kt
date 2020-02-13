package com.rooio.repairs

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.arch.core.util.Function
import com.android.volley.Request
import org.json.JSONException
import org.json.JSONObject
import java.util.*

// Activity that creates the login page of the application
// User is able to login with a username or password
// and go back to the landing page
class Login : RestApi() {

    private var usernameField: EditText? = null
    private var passwordField: EditText? = null
    private var connectAccount: Button? = null
    private var cancelLogin: Button? = null
    private var errorMessage: TextView? = null
    private val url = "https://capstone.api.roopairs.com/v0/auth/login/"

    override fun onCreate(savedInstanceState: Bundle?) {
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

        //Reset error message
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
            errorMessage!!.setText(R.string.loading)
            val request = JsonRequest(false, url, params, responseFunc, errorFunc, false)
            sendLoginInfo(request)
        }
    }

    // Switches page from Login to Landing after clicking Cancel
    private fun onCancel() {
        cancelLogin!!.setOnClickListener { startActivity(Intent(this@Login, Landing::class.java)) }
    }

    //Change view to Choose Service Location page after a successful request
    @JvmField
    var responseFunc = Function<Any, Void?> { jsonObj: Any ->
        try {
            errorMessage!!.text = ""
            storeToken(jsonObj)
            startActivity(Intent(this@Login, LocationLogin::class.java))
        } catch (e: JSONException) {
            errorMessage!!.setText(R.string.error_server)
        }
        null
    }
    //Error provided if login information is incorrect or cannot be found from request
    @JvmField
    var errorFunc = Function<String, Void?> {
        errorMessage!!.setTextColor(Color.parseColor("#E4E40B0B"))
        errorMessage!!.setText(R.string.errorLogin)
        null
    }

    //Stores user token so that user does not have to login every time
    @Throws(JSONException::class)
    fun storeToken(responseObj: Any) {
        val jsonResponseObject = responseObj as JSONObject
        val token = jsonResponseObject["token"] as String
        val name = jsonResponseObject["first_name"] as String

        userToken = token
        firstName = name
    }

    // Sends username and password to the API through a request
    fun sendLoginInfo(request: JsonRequest) {
        val username = request.params["username"]
        val password = request.params["password"]
        if (username != "" || password != "") {
            requestJsonObject(Request.Method.POST, request)
        } else {
            errorMessage!!.setTextColor(Color.parseColor("#E4E40B0B"))
            errorMessage!!.setText(R.string.errorLogin)
        }
    }
}
