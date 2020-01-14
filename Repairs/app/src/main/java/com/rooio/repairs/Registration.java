package com.rooio.repairs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Registration extends RestApi  {

    private Spinner myspinner;

    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText password;
    private EditText restaurantname;
    private Button register;
    private TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        String url = "https://capstone.api.roopairs.com/v0/auth/register/";


        //Input initialization;


        myspinner = (Spinner) findViewById(R.id.spinner);

        register = (Button) findViewById(R.id.register);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        restaurantname = (EditText) findViewById(R.id.restaurantname);
        errorMessage = (TextView) findViewById(R.id.errorMessage);

        Integer mySpinner =  1;

        HashMap<String, Object> params = new HashMap<>();
        params.put("first_name", firstname.toString());
        params.put("last_name", lastname.toString());
        params.put("email", email.toString());
        params.put("password", password.toString());

        HashMap<String, Object> internal_client = new HashMap<>();
        internal_client.put("name", restaurantname.toString());
        internal_client.put("industry_type", mySpinner);

        params.put("internal_client", internal_client);

        Function<JSONObject,Void> responseFunc = (jsonArray) -> {
            Intent intent5 = new Intent(Registration.this, Login.class);
            startActivity(intent5);
            return null;
        };

        Function<String, Void> errorFunc = (string) -> {
            errorMessage.setText(string);
            return null;
        };

        /*
        //Functions for DropDown "Industry Type"
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String >(Registration.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.industry_type));
        //Set Adapter as
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(myAdapter);
        myspinner.setOnItemClickListener(this);

        */

        //Functions for the "Register Button" to Login Page
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestPostJsonObj(url, params, responseFunc, errorFunc, false);

//                Intent intent5 = new Intent(Registration.this, Login.class);
//                startActivity(intent5);
            }
        });

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