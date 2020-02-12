package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.arch.core.util.Function
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.regex.Pattern

class Registration : RestApi() {
    private var firstname: EditText? = null
    private var lastname: EditText? = null
    private var email: EditText? = null
    private var password: EditText? = null
    private var password_verify: EditText? = null

    private var restaurantname: EditText? = null
    private var registerButton: Button? = null
    private var cancelButton: Button? = null
    private var error: TextView? = null
    private var industry_int: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        supportActionBar!!.elevation = 0f
        registerButton = findViewById(R.id.register)
        cancelButton = findViewById(R.id.cancelRegistration)
        firstname = findViewById(R.id.firstname)
        lastname = findViewById(R.id.lastname)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        password_verify = findViewById(R.id.Password_verify)

        restaurantname = findViewById(R.id.restaurantname)
        error = findViewById(R.id.error)
        industry_int = 2
        onRegisterClick()
        onCancelClick()
    }

    // Sends required information to API and loads the next screen.
// Error if information is invalid or incorrect
    private fun sendRegistrationInfo() {
        val url = "https://capstone.api.roopairs.com/v0/auth/register/"
        val params = HashMap<String, Any>()
        params["first_name"] = firstname!!.text.toString()
        params["last_name"] = lastname!!.text.toString()
        params["email"] = email!!.text.toString()
        params["password"] = password!!.text.toString()
        val internal_client = HashMap<String, Any?>()
        internal_client["name"] = restaurantname!!.text.toString()
        internal_client["industry_type"] = industry_int
        params["internal_client"] = internal_client
        val responseFunc = Function<JSONObject, Void?> { jsonObj: JSONObject ->
            try {
                storeToken(jsonObj)
                startActivity(Intent(this@Registration, LocationLogin::class.java))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            null
        }
        val errorFunc = Function<String, Void?> { string: String? ->
            error!!.text = "Email is already registered with a Roopairs account."
            null
        }
        requestPostJsonObj(url, params, responseFunc, errorFunc, false)
    }

    @Throws(JSONException::class)
    fun storeToken(responseObj: JSONObject) {
        val token = responseObj["token"] as String
        val name = responseObj["first_name"] as String
        userToken = token
        firstName = name
    }

    // validates password
    fun isValidPassword(passwordver: String, userPassword: String): Boolean {
        if (passwordver == userPassword){
            val digitCasePattern = Pattern.compile("[0-9 ]")
            return userPassword.length >= 6 &&
                    digitCasePattern.matcher(userPassword).find()
        }
        else
            return false
    }

    // validates email
    fun isValidEmail(email: String?): Boolean {
        val emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$")
        return emailPattern.matcher(email).find()
    }

    // makes sure all fields are filled out
    private val isFilled: Boolean
        private get() = !firstname!!.text.toString().isEmpty() &&
                !lastname!!.text.toString().isEmpty() &&
                !email!!.text.toString().isEmpty() &&
                !password!!.text.toString().isEmpty() &&
                !password_verify!!.text.toString().isEmpty() &&
                !restaurantname!!.text.toString().isEmpty()

    private fun onRegisterClick() {
        registerButton!!.setOnClickListener {
            error!!.text = ""
            if (isFilled) {
                if (isValidEmail(email!!.text.toString())) if (isValidPassword(password_verify!!.text.toString(), password!!.text.toString())) sendRegistrationInfo() else error!!.text = "Invalid password." else error!!.text = "Invalid email address."
            } else error!!.text = "Please fill out all fields."
        }
    }

    private fun onCancelClick() {
        cancelButton!!.setOnClickListener { startActivity(Intent(this@Registration, Landing::class.java)) }
    }
}