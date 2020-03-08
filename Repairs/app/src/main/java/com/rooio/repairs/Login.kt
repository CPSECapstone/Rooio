package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
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

    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var connectAccount: Button
    private lateinit var cancelLogin: Button
    private lateinit var errorMessage: TextView
    private lateinit var loadingPanel: RelativeLayout
    private val url = "https://capstone.api.roopairs.com/v0/auth/login/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        centerTitleBar()
        initializeVariables()
        onConnectAccount()
        onCancel()
    }

    //Centers "Repairs" title
    private fun centerTitleBar() {
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        supportActionBar!!.elevation = 0f
    }

    //Initializes UI variables
    private fun initializeVariables() {
        connectAccount = findViewById(R.id.connectAccount)
        usernameField = findViewById(R.id.usernameField)
        passwordField = findViewById(R.id.passwordField)
        cancelLogin = findViewById(R.id.cancelLogin)
        errorMessage = findViewById(R.id.errorMessage)
        loadingPanel = findViewById(R.id.loadingPanel)
    }

    // Attempts to log in the user after clicking Connect Account
    private fun onConnectAccount() {
        val params = HashMap<Any?, Any?>()
        loadingPanel.visibility = View.GONE
        connectAccount.setOnClickListener {
            params["username"] = usernameField.text.toString()
            params["password"] = passwordField.text.toString()
            val request = JsonRequest(false, url, params, responseFunc, errorFunc, false)
            sendLoginInfo(request)
        }
    }

    // Switches page from Login to Landing after clicking Cancel
    private fun onCancel() {
        cancelLogin.setOnClickListener { startActivity(Intent(this@Login, Landing::class.java)) }
    }

    //Change view to Choose Service Location page after a successful request
    @JvmField
    var responseFunc = Function<Any, Void?> { response: Any ->
        loadingPanel.visibility = View.GONE
        errorMessage.text = ""
        val jsonObj = response as JSONObject
        storeToken(jsonObj)
        startActivity(Intent(this@Login, ChooseServiceProvider::class.java))
        null
    }
    //Error provided if login information is incorrect or cannot be found from request
    @JvmField
    var errorFunc = Function<String, Void?> { error -> String
        loadingPanel.visibility = View.GONE
        if (error == "Does not exist.") errorMessage.setText(R.string.error_login)
        else errorMessage.text = error
        null
    }

    //Stores user token so that user does not have to login every time
    @Throws(JSONException::class)
    private fun storeToken(jsonObj: JSONObject) {
        val token = jsonObj["token"] as String
        val name = jsonObj["first_name"] as String
        userToken = token
        userName = name
    }

    // Sends username and password to the API through a request
    private fun sendLoginInfo(request: JsonRequest) {
        val username = request.params?.get("username")
        val password = request.params?.get("password")
        if (username != "" && password != "") {
            errorMessage.text = ""
            loadingPanel.visibility = View.VISIBLE
            requestJson(Request.Method.POST, JsonType.OBJECT, request)
        } else errorMessage.setText(R.string.error_login)
    }
}
