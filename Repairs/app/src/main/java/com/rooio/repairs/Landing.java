package com.rooio.repairs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        getSupportActionBar().setElevation(0);

        //Initializing UI variables
        createAccount = findViewById(R.id.createAccount);
        connectAccount = findViewById(R.id.connectAccount);

        createAccountBtn();
        connectAccountBtn();
    }

    public void createAccountBtn() {
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Landing.this, Registration.class);
                startActivity(intent1);
            }
        });
    }

    public void connectAccountBtn() {
        connectAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Landing.this, Login.class);
                startActivity(intent1);
            }
        });
    }
}