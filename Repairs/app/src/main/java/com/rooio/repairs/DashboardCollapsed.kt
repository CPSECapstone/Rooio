package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DashboardCollapsed : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_collapsed)
        val collapse = findViewById<ImageView>(R.id.collapse)
        collapse.setOnClickListener{
            val extendIntent = Intent(this@DashboardCollapsed, Dashboard::class.java)
            startActivity(extendIntent)
        }
    }
}