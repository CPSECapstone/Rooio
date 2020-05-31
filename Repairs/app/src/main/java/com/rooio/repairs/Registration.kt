package com.rooio.repairs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.arch.core.util.Function
import com.android.volley.Request
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
    private lateinit var loadingPanel: ProgressBar
    private var industryInt: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        onResume()
        centerTitleBar()
        initializeVariables()
        onRegister()
        onCancel()
        onPause()
    }

    //Initializes UI variables
    private fun initializeVariables() {
        registerButton = findViewById(R.id.register)
        cancelButton = findViewById(R.id.cancelRegistration)
        firstName = findViewById(R.id.firstName)
        firstName.requestFocus()
        lastName = findViewById(R.id.lastName)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        passwordVerify = findViewById(R.id.verifyPassword)
        restaurantName = findViewById(R.id.restaurantName)
        errorMessage = findViewById(R.id.errorMessage)
        loadingPanel = findViewById(R.id.loadingPanel)
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
        registerButton.visibility = View.GONE

        requestJson(Request.Method.POST, JsonType.OBJECT, JsonRequest(false, url, params,
                responseFunc, errorFunc, false))
    }

    val responseFunc = Function<Any, Void?> { response: Any ->
        loadingPanel.visibility = View.GONE
        registerButton.visibility = View.VISIBLE
        val jsonObj = response as JSONObject
        storeToken(jsonObj)
        startActivity(Intent(this@Registration, LocationLogin::class.java))
        null
    }

    val errorFunc = Function<String, Void?> { error: String? ->
        loadingPanel.visibility = View.GONE
        registerButton.visibility = View.VISIBLE
        if (error == "Does not exist.") errorMessage.setText(R.string.error_register)
        else errorMessage.text = error
        null
    }

    //Stores the username and password in system
    @Throws(JSONException::class)
    private fun storeToken(responseObj: JSONObject) {
        val token = responseObj["token"] as String
        val name = responseObj["first_name"] as String

        val prefs = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(employeeId + "__token", token)
        editor.putString(employeeId + "__name", name)

        editor.apply()

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
        else return false
    }

    // validates email
    private fun isValidEmail(email: String): Boolean {
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
        registerButton.visibility = View.VISIBLE
        errorMessage.text = ""
        registerButton.setOnClickListener {
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

    //When the user presses the cancel button to return to the landing page
    private fun onCancel() {
        cancelButton.setOnClickListener { startActivity(Intent(this@Registration, Landing::class.java)) }
    }
}