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

import androidx.appcompat.app.ActionBar;
import androidx.arch.core.util.Function;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Registration extends RestApi   {


    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText password;
    private EditText restaurantname;
    private Button register;
    private Button cancelRegistration;
    private Integer industry_int;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Load activity view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Centers "Repairs" title
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setElevation(0);

        String url = "https://capstone.api.roopairs.com/v0/auth/register/";

        //Input initialization;


        //Initializing UI variables;
        register = findViewById(R.id.register);
        cancelRegistration = findViewById(R.id.cancelRegistration);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        restaurantname = findViewById(R.id.restaurantname);
        industry_int = 2;

        //Cancel button returns to landing page
        cancelRegistration.setOnClickListener(view -> startActivity(new Intent(Registration.this, Landing.class)));


        //Functions for DropDown "Industry Type"


        //Register button sends registration information
        register.setOnClickListener(view -> sendRegistrationInfo());
    }

    // Sends required information to API and loads the next screen.
    // Error if information is invalid or incorrect
    private void sendRegistrationInfo() {
        //Roopairs Register URL
        String url = "https://capstone.api.roopairs.com/v0/auth/register/";

        Function<JSONObject,Void> responseFunc = (jsonArray) -> {
            Intent intent5 = new Intent(Registration.this, LocationLogin.class);
            startActivity(intent5);
            return null;
        };

        Function<String, Void> errorFunc = (string) -> {
            return null;
        };

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



    /*
    public boolean validate(String userName, String userPassword){
        if((isValid(userName, userPassword)) == true){
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean isValid(String username, String passwordhere) {

        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
        boolean flag = true;

        if (username.length() <= 0) {
            flag=false;
            return flag;
        }
        if (passwordhere.length() < 6) {
            flag=false;
            return flag;
        }
        if (!digitCasePatten.matcher(passwordhere).find()) {
            flag=false;
            return flag;
        }
        return flag;
    }*/




}