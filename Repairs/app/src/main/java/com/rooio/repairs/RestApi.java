package com.rooio.repairs;


import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public abstract class RestApi extends AppCompatActivity {

    String userToken = null;

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

    public abstract void requestPost(String url, HashMap<String, String> params, final boolean headersFlag);

    public abstract void requestGet(String url, final boolean headersFlag);
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

TODO: ----------------------------------------------------------------------------------------------

    public void requestPost(String url, HashMap<String,String> params, final boolean headersFlag){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

//     -- Example params initiations
//        HashMap<String, String> params = new HashMap<>();
//        params.put("username", username.getText().toString());
//        params.put("password", password.getText().toString());

//     -- Transforms params HashMap into Json Object
        JSONObject jsonObject = new JSONObject(params);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject responseObj = response;

//          TODO: ----->  Start specialized json handling function

                        storeToken(responseObj);

//                ----->
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

//          TODO: ----->  Error Handling functions

                        success.setText(error.toString());

//                ----->
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

TODO: ----------------------------------------------------------------------------------------------

// -- Example Json handling function
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