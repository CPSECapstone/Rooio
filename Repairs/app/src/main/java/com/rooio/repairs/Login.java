package com.rooio.repairs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
    private String url = "https://capstone.api.roopairs.com/v0/auth/login/";

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        //Load activity view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Centers "Repairs" title
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setElevation(0);

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
        HashMap<String, Object> params = new HashMap<>();

        connectAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params.put("username",  usernameField.getText().toString());
                params.put("password",  passwordField.getText().toString());

                errorMessage.setTextColor(Color.parseColor("#A6A9AC"));
                errorMessage.setText("Loading");
                sendLoginInfo(new JsonRequest(
                        false,
                        url,
                        params,
                        responseFunc,
                        errorFunc,
                        false));
            }
        });

    }

    // Switches page from Login to Landing after clicking Cancel
    private void onCancel() {
        cancelLogin.setOnClickListener(view ->
                startActivity(new Intent(Login.this, Landing.class)));
    }
    
    public Function<Object,Void> responseFunc = (jsonObj) -> {
        try {
            errorMessage.setText("");
            storeToken(jsonObj);
            startActivity(new Intent(Login.this, LocationLogin.class));

        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    };

    public Function<String,Void> errorFunc = (string) -> {
        errorMessage.setTextColor(Color.parseColor("#E4E40B0B"));
        errorMessage.setText(R.string.errorLogin);
//        errorMessage.setText(string);

        return null;
    };

    // -- Example Json handling function
    public void storeToken(Object responseObj) throws JSONException {
        JSONObject JsonResponseObject = (JSONObject) responseObj;
        String token = (String)JsonResponseObject.get("token");
        setUserToken(token);
    }

    // Sends username and password to the API and loads the next screen.
    // Returns error if the information is invalid
    public void sendLoginInfo(JsonRequest request) {
        String username = (String) request.getParams().get("username");
        String password = (String) request.getParams().get("password");
        if(!username.equals("") &&  !password.equals("")){
            requestPostJsonObj(request);
        }
        else {
            errorMessage.setTextColor(Color.parseColor("#E4E40B0B"));
            errorMessage.setText(R.string.errorLogin);
        }
    }

}