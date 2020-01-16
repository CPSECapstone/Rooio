package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class DashboardCollapsed : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_collapsed)
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)
        val expand = findViewById<ImageView>(R.id.expand)
        expand.setOnClickListener{
            val expandIntent = Intent(this@DashboardCollapsed, Dashboard::class.java)
            startActivity(expandIntent)
        }
    }
}