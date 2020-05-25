package com.rooio.repairs

import android.Manifest
import android.accounts.Account
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IInterface
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v1.ResultStatus
import com.clover.sdk.v1.ServiceConnector
import com.clover.sdk.v3.employees.Employee
import com.clover.sdk.v3.employees.EmployeeConnector
import com.clover.sdk.v3.employees.EmployeeConnector.EmployeeCallback

//First page that will show up if a user token is not stored
class Landing : RestApi(), ServiceConnector.OnServiceConnectedListener, EmployeeConnector.OnActiveEmployeeChangedListener {

    private lateinit var createAccount: Button
    private lateinit var connectAccount: Button

    private val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1
    private var mEmployeeConnector: EmployeeConnector? = null
    private var account: Account? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        centerTitleBar()
        initializeVariables()
        onCreateAccount()
        onConnectAccount()
    }

    //Initializes UI variables
    private fun initializeVariables() {
        createAccount = findViewById(R.id.createAccount)
        connectAccount = findViewById(R.id.connectAccount)
    }

    private fun loggedInUserCheck(empId: String): Boolean {
        //Use Shared Preferences Login
        val prefs = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val savedToken = prefs.getString(empId + "__token", "")
        val name = prefs.getString(empId + "__name", "")
        val userLocation = prefs.getString(empId + "__userLocationId", "")

        employeeId = empId

        return if (name != null && name.isNotEmpty() && savedToken != null && savedToken.isNotEmpty() && userLocation != null && userLocation.isNotEmpty()) {
            userToken = savedToken
            userName = name
            userLocationID = userLocation
            startActivity(Intent(this@Landing, Dashboard::class.java))
            true
        } else {
            false
        }
    }

    private fun onCreateAccount() {
        createAccount.setOnClickListener {
            val registration = Intent(this@Landing, Registration::class.java)
            startActivity(registration)
        }
    }

    private fun onConnectAccount() {
        connectAccount.setOnClickListener {
            val login = Intent(this@Landing, Login::class.java)
            startActivity(login)
        }
    }

    override fun onResume() {
        super.onResume()

        // getting clover account
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.GET_ACCOUNTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.GET_ACCOUNTS), MY_PERMISSIONS_REQUEST_READ_CONTACTS)
            }
        } else {
            // Permission has already been granted
            // Retrieve the Clover account
            if (account == null) {
                account = CloverAccount.getAccount(this)
                if (account == null) {
                    Toast.makeText(this, "No Clover Account Found", Toast.LENGTH_SHORT).show()
                    finish()
                    return
                }
            }

            // Create and Connect to the EmployeeConnector
            connect()

            // Get the employee object
            getEmployee()
        }
    }

    private fun connect() {
        disconnect()
        if (account != null) {
            mEmployeeConnector = EmployeeConnector(this, account, this)
            mEmployeeConnector!!.connect()
        }
    }

    private fun disconnect() { //remember to disconnect!
        if (mEmployeeConnector != null) {
            mEmployeeConnector!!.disconnect()
            mEmployeeConnector = null
        }
    }

    private fun getEmployee() {
        mEmployeeConnector?.getEmployee(object : EmployeeCallback<Employee>() {
            override fun onServiceSuccess(result: Employee, status: ResultStatus) {
                super.onServiceSuccess(result, status)
                val id = result.id
                val name = result.name.toString()
                Toast.makeText(this@Landing, "Welcome $name!", Toast.LENGTH_SHORT).show()
                when(result.role.toString()){
                    "EMPLOYEE" -> {
                        val hasAccount = loggedInUserCheck(id)
                        if (!hasAccount) {
                            startActivity(Intent(this@Landing, Login::class.java))
                        }
                    }

                    else -> {
                        loggedInUserCheck(id)
                    }
                }
            }
        })
    }

    override fun onActiveEmployeeChanged(employee: Employee?) {}

    override fun onServiceConnected(serviceConnector: ServiceConnector<out IInterface?>?) {}

    override fun onServiceDisconnected(serviceConnector: ServiceConnector<out IInterface?>?) {}
}