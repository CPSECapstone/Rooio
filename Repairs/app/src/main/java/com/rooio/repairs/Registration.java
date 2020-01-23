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

public class Registration extends RestApi  implements AdapterView.OnItemSelectedListener {

    private Spinner myspinner;

    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText password;
    private EditText restaurantname;
    private Button register;
    private Button cancelRegistration;
    private TextView errorMessage;
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

        //Initializing UI variables;
        myspinner = findViewById(R.id.spinner);
        register = findViewById(R.id.register);
        cancelRegistration = findViewById(R.id.cancelRegistration);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        restaurantname = findViewById(R.id.restaurantname);
        errorMessage = findViewById(R.id.errorMessage);
        errorMessage.setText(null);
        industry_int = 2;

        //Cancel button returns to landing page
        cancelRegistration.setOnClickListener(view -> startActivity(new Intent(Registration.this, Landing.class)));

        //Functions for DropDown "Industry Type"
        ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(this, R.array.industry_type, android.R.layout.simple_spinner_item);

        //Set Adapter as
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(myAdapter);
        myspinner.setOnItemSelectedListener(this);

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
            errorMessage.setText(string);
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