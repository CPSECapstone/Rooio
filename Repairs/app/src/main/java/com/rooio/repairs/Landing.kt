package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class Landing : AppCompatActivity() {
    private var createAccount: Button? = null
    private var connectAccount: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) { //Load activity view
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        //Centers "Repairs" title
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        supportActionBar!!.elevation = 0f
        //Initializing UI variables
        createAccount = findViewById(R.id.createAccount)
        connectAccount = findViewById(R.id.connectAccount)
        createAccountBtn()
        connectAccountBtn()
    }

    fun createAccountBtn() {
        createAccount!!.setOnClickListener {
            val intent1 = Intent(this@Landing, Registration::class.java)
            startActivity(intent1)
        }
    }

    fun connectAccountBtn() {
        connectAccount!!.setOnClickListener {
            val intent1 = Intent(this@Landing, Login::class.java)
            startActivity(intent1)
        }
    }
}