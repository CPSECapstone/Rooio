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

        myspinner = (Spinner) findViewById(R.id.spinner);
        register = (Button) findViewById(R.id.register);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String >(Registration.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.industry_type));
        //Set Adapter as
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(myAdapter);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(Registration.this, LocationLogin.class);
                startActivity(intent5);
            }
        });
    }
}
