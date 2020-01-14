package com.rooio.repairs;


import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class RestApi extends AppCompatActivity {

    static String userToken = null;

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserToken() {
        return userToken;
    }

    String userLocationID = null;

    public String getUserLocationID() {
        return userLocationID;
    }

    public void setUserLocationID(String userLocationID) {
        this.userLocationID = userLocationID;
    }

//--------------------------------------------------------------------------------------------------

    public void requestGetJsonObj(String url, final Function<JSONObject, Void> responseFunc,
                        final Function<String, Void> errorFunc, final boolean headersFlag){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject responseObj = response;

                        responseFunc.apply(responseObj);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        errorFunc.apply(error.toString());

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

    public void requestPostJsonObj(String url, HashMap<String,Object> params,
                        final Function<JSONObject, Void> responseFunc, final Function<String, Void> errorFunc, final boolean headersFlag){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

//     -- Example params initiations
//        HashMap<String, String> params = new HashMap<>();
//        params.put("username", username.getText().toString());
//        params.put("password", password.getText().toString());

//     -- Transforms params HashMap into Json Object
        JSONObject jsonParams = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject responseObj = response;

                        responseFunc.apply(responseObj);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        errorFunc.apply(error.toString());

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

    public void requestGetJsonArray(String url, final Function<JSONArray, Void> responseFunc,
                        final Function<String, Void> errorFunc, final boolean headersFlag){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray responseObj = response;

                        responseFunc.apply(responseObj);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        errorFunc.apply(error.toString());

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

    public void requestPostJsonArray(String url, HashMap<String,Object> params,
                        final Function<JSONArray, Void> responseFunc, final Function<String, Void> errorFunc, final boolean headersFlag){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

//     -- Example params initiations
//        HashMap<String, String> params = new HashMap<>();
//        params.put("username", username.getText().toString());
//        params.put("password", password.getText().toString());

//     -- Transforms params HashMap into Json Object
        JSONObject jsonParams = new JSONObject(params);


        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.POST, url, jsonParams, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray responseObj = response;

                        responseFunc.apply(responseObj);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        errorFunc.apply(error.toString());

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

}

/*
---- Example requestPost function call and prestaging:
 // --- API Swagger url link
        String url = "https://capstone.api.roopairs.com/v0/auth/login/";

 // --- Build params HashMap for Rest Json Body
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username.getText().toString());
        params.put("password", password.getText().toString());

 // --- requestPost function call
        requestPost(url, params, false);


---- Example Json handling function
    public void storeToken(JSONObject responseObj){
        String token = null;
        try {
            token = (String)responseObj.get("Token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        success.setText(token);
        setUserToken(token);
    }
----------------------------------------------------------------------------------------------------

*/