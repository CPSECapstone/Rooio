package com.rooio.repairs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Landing extends AppCompatActivity {

    private Button createAccount;
    private Button connectAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        //Load activity view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        //Centers "Repairs" title
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        //Initializing UI variables
        createAccount = findViewById(R.id.createAccount);
        connectAccount = findViewById(R.id.connectAccount);

    }
}