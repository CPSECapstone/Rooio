package com.rooio.repairs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.arch.core.util.Function;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends RestApi {

    private EditText usernameField;
    private EditText passwordField;
    private Button connectAccount;
    private Button cancelLogin;
    private TextView errorMessage;

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        //Load activity view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Centers "Repairs" title
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        //Initializing UI variables
        connectAccount = findViewById(R.id.connectAccount);
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        cancelLogin = findViewById(R.id.cancelLogin);
        errorMessage = findViewById(R.id.errorMessage);
        errorMessage.setText("");

        onConnectAccount();
        onCancel();
    }

    // Attempts to log in the user after clicking Connect Account
    private void onConnectAccount(){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        connectAccount.setOnClickListener(view -> sendLoginInfo(username, password));
    }

    // Switches page from Login to Landing after clicking Cancel
    private void onCancel() {
        cancelLogin.setOnClickListener(view ->
                startActivity(new Intent(Login.this, Landing.class)));
    }

    // Sends username and password to the API and loads the next screen.
    // Returns error if the information is invalid
    private void sendLoginInfo(String username, String password) {
        if(!username.equals("") &&  !password.equals("")){

            // --- API Swagger URL link
            String url = "https://capstone.api.roopairs.com/v0/auth/login/";

            // --- Build params HashMap for Rest JSON Body
            HashMap<String, Object> params = new HashMap<>();
            params.put("username",  username);
            params.put("password",  password);

            // --- requestPost function call
//                    requestPost(url, params, false);

            Function<JSONObject,Void> responseFunc = (jsonObj) -> {
                try {
                    storeToken(jsonObj);
                    startActivity(new Intent(Login.this, LocationLogin.class));

                } catch (JSONException e){
                    e.printStackTrace();
                }
                return null;
            };

            Function<String,Void> errorFunc = (string) -> {
                errorMessage.setText("Incorrect username and/or password.");
                return null;
            };

            requestPostJsonObj(url, params, responseFunc, errorFunc, false);

        }
        else{
            errorMessage.setText("Incorrect username and/or password.");
        }
    }

    // -- Example Json handling function
    public void storeToken(JSONObject responseObj) throws JSONException {
        String token;
        token = (String)responseObj.get("token");
        setUserToken(token);
    }

}