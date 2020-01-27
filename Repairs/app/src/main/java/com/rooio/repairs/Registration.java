package com.rooio.repairs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.arch.core.util.Function;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Registration extends RestApi {

    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText password;
    private EditText restaurantname;
    private Button registerButton;
    private Button cancelButton;
    private TextView error;
    private Integer industry_int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setElevation(0);

        registerButton = findViewById(R.id.register);
        cancelButton = findViewById(R.id.cancelRegistration);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        restaurantname = findViewById(R.id.restaurantname);
        error = findViewById(R.id.error);
        industry_int = 2;

        onRegisterClick();
        onCancelClick();
    }

    // Sends required information to API and loads the next screen.
    // Error if information is invalid or incorrect
    private void sendRegistrationInfo() {
        String url = "https://capstone.api.roopairs.com/v0/auth/register/";

        HashMap<String, Object> params = new HashMap<>();
        params.put("first_name", firstname.getText().toString());
        params.put("last_name", lastname.getText().toString());
        params.put("email", email.getText().toString());
        params.put("password", password.getText().toString());

        HashMap<String, Object> internal_client = new HashMap<>();
        internal_client.put("name", restaurantname.getText().toString());
        internal_client.put("industry_type", industry_int);

        params.put("internal_client", internal_client);

        Function<JSONObject,Void> responseFunc = (jsonArray) -> {
            startActivity(new Intent(Registration.this, LocationLogin.class));
            return null;
        };

        Function<String, Void> errorFunc = (string) -> {
            error.setText("Email is already registered with a Roopairs account.");
            return null;
        };

        requestPostJsonObj(url, params, responseFunc, errorFunc, false);
    }

    // validates password
    public boolean isValidPassword(String userPassword){
        Pattern digitCasePattern = Pattern.compile("[0-9 ]");

        return !(userPassword.length() < 6) &&
                digitCasePattern.matcher(userPassword).find();
    }

    // validates email
    public boolean isValidEmail(String email){
        Pattern emailPattern =
                Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\." +
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$");

        return emailPattern.matcher(email).find();
    }

    // makes sure all fields are filled out
    private boolean isFilled() {
        return !firstname.getText().toString().isEmpty() &&
                !lastname.getText().toString().isEmpty() &&
                !email.getText().toString().isEmpty() &&
                !password.getText().toString().isEmpty() &&
                !restaurantname.getText().toString().isEmpty();
    }

    private void onRegisterClick() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setText("");

                if (isFilled()){
                    if(isValidEmail(email.getText().toString()))
                        if(isValidPassword(password.getText().toString()))
                            sendRegistrationInfo();
                        else
                            error.setText("Invalid password.");
                    else
                        error.setText("Invalid email address.");
                }
                else
                    error.setText("Please fill out all fields.");
            }
        });
    }

    private void onCancelClick() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, Landing.class));
            }
        });
    }
}