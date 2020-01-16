package com.rooio.repairs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.arch.core.util.Function;

import org.json.JSONObject;

import java.util.HashMap;

public class Registration extends RestApi  implements AdapterView.OnItemSelectedListener {

    private Spinner myspinner;

    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText password;
    private EditText restaurantname;
    private Button register;
    private Button register2;
    private TextView errorMessage;
    private String industry_string;
    private Integer industry_int;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        String url = "https://capstone.api.roopairs.com/v0/auth/register/";

        //Input initialization;

        myspinner = (Spinner) findViewById(R.id.spinner);

        register = (Button) findViewById(R.id.register);
        register2 = (Button) findViewById(R.id.register2);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        restaurantname = (EditText) findViewById(R.id.restaurantname);
        errorMessage = (TextView) findViewById(R.id.errorMessage);
        errorMessage.setText(null);
        industry_int = 2;


        Function<JSONObject,Void> responseFunc = (jsonArray) -> {
            Intent intent5 = new Intent(Registration.this, Login.class);
            startActivity(intent5);
            return null;
        };

        Function<String, Void> errorFunc = (string) -> {
            errorMessage.setText(string);
            return null;
        };

        register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(Registration.this, Login.class);
                startActivity(intent5);
            }
        });

        //Functions for DropDown "Industry Type"
        ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(this, R.array.industry_type, android.R.layout.simple_spinner_item);
        //Set Adapter as
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(myAdapter);
        myspinner.setOnItemSelectedListener(this);



        //Functions for the "Register Button" to Login Page
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                HashMap<String, Object> params = new HashMap<>();
                params.put("first_name", firstname.getText().toString());
                params.put("last_name", lastname.getText().toString());
                params.put("email", email.getText().toString());
                params.put("password", password.getText().toString());

                HashMap<String, Object> internal_client = new HashMap<>();
                internal_client.put("name", restaurantname.getText().toString());
                internal_client.put("industry_type", industry_int);

                params.put("internal_client", internal_client);

                requestPostJsonObj(url, params, responseFunc, errorFunc, false);

//                Intent intent5 = new Intent(Registration.this, Login.class);
//                startActivity(intent5);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.length() == 11) {
            industry_int = 1;

        }
        else if (text.length() == 10){
            industry_int = 3;

        }
        else{
            industry_int = 2;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /*
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    */

}