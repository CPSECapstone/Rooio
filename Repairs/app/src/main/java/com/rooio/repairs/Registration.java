package com.rooio.repairs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Registration extends AppCompatActivity {

    private Spinner myspinner;

    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText password;
    private EditText restaurantname;
    private Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Input initialization;
        myspinner = (Spinner) findViewById(R.id.spinner);
        register = (Button) findViewById(R.id.register);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        restaurantname = (EditText) findViewById(R.id.restaurantname);

        //Functions for DropDown "Industry Type"
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String >(Registration.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.industry_type));
        //Set Adapter as
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(myAdapter);

        //Functions for the "Register Button" to Login Page
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(Registration.this, Login.class);
                startActivity(intent5);
            }
        });
    }
}
