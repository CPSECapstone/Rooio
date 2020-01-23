package com.rooio.repairs

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setCustomView(R.layout.action_bar)

        val collapse = findViewById<ImageView>(R.id.collapse)
        collapse.setOnClickListener{
            val collapseIntent = Intent(this@Dashboard, DashboardCollapsed::class.java)
            startActivity(collapseIntent)
        }
    }
}
