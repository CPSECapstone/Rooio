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
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.regex.Pattern

class Registration : RestApi() {

    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var passwordVerify: EditText
    private lateinit var restaurantName: EditText
    private lateinit var registerButton: Button
    private lateinit var cancelButton: Button
    private lateinit var errorMessage: TextView
    private lateinit var loadingPanel: RelativeLayout
    private var industryInt: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        centerTitleBar()
        initializeVariables()
        onRegister()
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
        registerButton = findViewById(R.id.register)
        cancelButton = findViewById(R.id.cancelRegistration)
        firstName = findViewById(R.id.firstName)
        lastName = findViewById(R.id.lastName)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        passwordVerify = findViewById(R.id.verifyPassword)
        restaurantName = findViewById(R.id.restaurantName)
        errorMessage = findViewById(R.id.errorMessage)
        loadingPanel = findViewById(R.id.loadingPanel)
        industryInt = 2
    }
    // Sends required information to API and loads the next screen.
    // Error if information is invalid or incorrect
    private fun sendRegistrationInfo() {
        val url = "auth/register/"
        val params = HashMap<Any?, Any?>()
        params["first_name"] = firstName.text.toString()
        params["last_name"] = lastName.text.toString()
        params["email"] = email.text.toString()
        params["password"] = password.text.toString()
        val internalClient = HashMap<String, Any?>()
        internalClient["name"] = restaurantName.text.toString()
        internalClient["industry_type"] = industryInt
        params["internal_client"] = internalClient
        loadingPanel.visibility = View.VISIBLE
        val request = JsonRequest(false, url, params, responseFunc, errorFunc, false)
        requestPostJsonObj(request)
    }

    val responseFunc = Function<Any, Void?> { response: Any ->
        loadingPanel.visibility = View.GONE
        try {
            val jsonObj = response as JSONObject
            storeToken(jsonObj)
            startActivity(Intent(this@Registration, LocationLogin::class.java))
        } catch (e: JSONException) {
            errorMessage.setText(R.string.error_server)
        }
        null
    }

    val errorFunc = Function<String, Void?> { error: String? ->
        loadingPanel.visibility = View.GONE
        if (error == "Does not exist.") errorMessage.setText(R.string.error_register)
        else errorMessage.text = error
        null
    }

    @Throws(JSONException::class)
    fun storeToken(responseObj: JSONObject) {
        val token = responseObj["token"] as String
        val name = responseObj["first_name"] as String
        userToken = token
        userName = name
    }

    // validates password
    private fun isValidPassword(passwordVer: String, userPassword: String): Boolean {
        if (passwordVer == userPassword){
            val digitCasePattern = Pattern.compile("[0-9 ]")
            return userPassword.length >= 6 &&
                    digitCasePattern.matcher(userPassword).find()
        }
        else
            return false
    }

    // validates email
    private fun isValidEmail(email: String?): Boolean {
        val emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$")
        return emailPattern.matcher(email).find()
    }

    // makes sure all fields are filled out
    private val isFilled: Boolean
        get() = firstName.text.toString().isNotEmpty() &&
                lastName.text.toString().isNotEmpty() &&
                email.text.toString().isNotEmpty() &&
                password.text.toString().isNotEmpty() &&
                passwordVerify.text.toString().isNotEmpty() &&
                restaurantName.text.toString().isNotEmpty()

    private fun onRegister() {
        loadingPanel.visibility = View.GONE
        registerButton.setOnClickListener {
            errorMessage.text = ""

            if (isFilled)
                if (isValidEmail(email.text.toString()))
                    if (isValidPassword(passwordVerify.text.toString(), password.text.toString())) {
                        sendRegistrationInfo()
                    }
                    else errorMessage.text = "Invalid password."
                else errorMessage.text = "Invalid email address."
            else errorMessage.text = "Please fill out all fields."
        }
    }

    private fun onCancel() {
        cancelButton.setOnClickListener { startActivity(Intent(this@Registration, Landing::class.java)) }
    }
}