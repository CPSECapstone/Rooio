package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

//First page that will show up if a user token is not stored
class Landing : AppCompatActivity() {
    private lateinit var createAccount: Button
    private lateinit var connectAccount: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        centerTitleBar()
        initializeVariables()
        onCreateAccount()
        onConnectAccount()
    }

    //Centers "Repairs" title
    private fun centerTitleBar() {
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        supportActionBar!!.elevation = 0f
    }

    //Initializes UI variables
    private fun initializeVariables() {
        createAccount = findViewById(R.id.createAccount)
        connectAccount = findViewById(R.id.connectAccount)
    }

    private fun onCreateAccount() {
        createAccount.setOnClickListener {
            val intent1 = Intent(this@Landing, Registration::class.java)
            startActivity(intent1)
        }
    }

    private fun onConnectAccount() {
        connectAccount.setOnClickListener {
            val intent1 = Intent(this@Landing, Login::class.java)
            startActivity(intent1)
        }
    }
}