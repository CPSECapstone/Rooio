package com.rooio.repairs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.arch.core.util.Function;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Login extends RestApi {

    private EditText username;
    private EditText password;
    private Button login;
    private TextView unsuccess;
    private TextView unsuccess3;
    private Button createaccount;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        Log.v("myTag", "This is in create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login = findViewById(R.id.login);
        username = findViewById(R.id.username_field);
        password = findViewById(R.id.password_field);
        unsuccess = findViewById(R.id.unsuccess);
        unsuccess3 = findViewById(R.id.unsuccess3);
        createaccount = findViewById(R.id.CreateAccount);

        unsuccess3.setText("Password must be at least 6 alphanumeric characters.");
        unsuccess.setText("");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((validate(username.getText().toString(), password.getText().toString())) == true){

                    // --- API Swagger url link
                    String url = "https://capstone.api.roopairs.com/v0/auth/login/";

                    // --- Build params HashMap for Rest Json Body
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("username", username.getText().toString());
                    params.put("password", password.getText().toString());

                    // --- requestPost function call
//                    requestPost(url, params, false);

                    Function<JSONObject,Void> responseFunc = (jsonObj) -> {
                        try {
                            storeToken(jsonObj);
                            Intent intent1 = new Intent(Login.this, LocationLogin.class);
                            startActivity(intent1);

                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                        return null;
                    };

                    Function<String,Void> errorFunc = (string) -> {
                        unsuccess3.setText("");
                        unsuccess.setText("Incorrect Username and/or Password.");
                        return null;
                    };

                    requestPostJsonObj(url, params, responseFunc, errorFunc, false);

                }
                else{
                    unsuccess3.setText("");
                    unsuccess.setText("Password must be at least 6 alphanumeric characters");

                }
            }
        });

        createaccount.setOnClickListener(v ->
                startActivity(new Intent(Login.this, Registration.class))
        );


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


    // -- Example Json handling function
    public void storeToken(JSONObject responseObj) throws JSONException {
        String token;
        token = (String)responseObj.get("token");
        setUserToken(token);

//        unsuccess.setText(token);
    }

}