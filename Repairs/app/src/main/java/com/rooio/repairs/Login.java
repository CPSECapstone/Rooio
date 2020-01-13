package com.rooio.repairs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

        login = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username_field);
        password = (EditText) findViewById(R.id.password_field);
        unsuccess = (TextView) findViewById(R.id.unsuccess);
        unsuccess3 = (TextView) findViewById(R.id.unsuccess3);
        createaccount = (Button) findViewById(R.id.CreateAccount);

        unsuccess3.setText("Password must be at least 6 alphanumeric characters.");
        unsuccess.setText("");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((validate(username.getText().toString(), password.getText().toString())) == true){

                    // --- API Swagger url link
                    String url = "https://capstone.api.roopairs.com/v0/auth/login/";

                    // --- Build params HashMap for Rest Json Body
                    HashMap<String, String> params = new HashMap<>();
                    params.put("username", username.getText().toString());
                    params.put("password", password.getText().toString());

                    // --- requestPost function call
                    requestPost(url, params, false);



                }
                else{
                    unsuccess3.setText("");
                    unsuccess.setText("Password must be at least 6 alphanumeric characters");

                }
            }
        });

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(Login.this, Registration.class);
                startActivity(intent5);
            }
        });


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

    public void requestPost(String url, HashMap<String,String> params, final boolean headersFlag){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

//     -- Transforms params HashMap into Json Object
        JSONObject jsonObject = new JSONObject(params);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject responseObj = response;

//          TODO: ----->  Start specialized json handling function

                        unsuccess.setText("");
                        unsuccess3.setText("Loading");

                        storeToken(responseObj);

//                        unsuccess3.setText(getUserToken());

                        Intent intent1 = new Intent(Login.this, LocationLogin.class);
                        startActivity(intent1);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

//          TODO: ----->  Error Handling functions

                        unsuccess3.setText("");
                        unsuccess.setText("Incorrect Username and/or Password.");


                    }}) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

//                  ----->  If true is given through headersFlag parameter the Post request will be sent with Headers
                if (headersFlag){

                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Token " + getUserToken());  //<-- Token in Abstract Class RestApi
                    return headers;

//                  ----->  If false is given through headersFlag parameter the Post request will not be sent with Headers
                } else {
                    return Collections.emptyMap();
                }
            }
        };

//  --> Equivalent of sending the request. Required to WORK...
        queue.add(jsonObjectRequest);

    }

    // -- Example Json handling function
    public void storeToken(JSONObject responseObj){
        String token = null;
        try {
            token = (String)responseObj.get("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        unsuccess.setText(token);
        setUserToken(token);

    }

}