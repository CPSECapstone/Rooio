package com.rooio.repairs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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
    static String firstName = null;

    private RequestQueue queue;

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getUserToken() {
        return userToken;
    }

    static String getFirstName() {
        return firstName;
    }


    static String userLocationID = null;

    public String getUserLocationID() {
        return userLocationID;
    }

    public void setUserLocationID(String userLocationID) {
        this.userLocationID = userLocationID;
    }


//--------------------------------------------------------------------------------------------------

    private void addToVolleyQueue(JsonObjectRequest request) {
        queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void addToVolleyQueue(JsonArrayRequest request) {
        queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private String errorMsgHandler(VolleyError error) {
        String errorMsg = "Unexpected Error!";
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            errorMsg = "No connection or you timed out. Try again.";
        } else if (error instanceof AuthFailureError) {
            errorMsg = "You are not authorized.";
        } else if (error instanceof ServerError) {
            errorMsg = "does not exist.";
        } else if (error instanceof NetworkError) {
            errorMsg = "Network Error. Try again.";
        } else if (error instanceof ParseError) {
            errorMsg = "Parse Error. Try again.";
        }

        return errorMsg;
    }

    public JSONObject createJsonParams(JsonRequest req) {
        return new JSONObject(req.getParams());
    }

    public JsonObjectRequest createJsonObjectRequest(JSONObject jsonParams, JsonRequest req) {
        String url = req.getUrl();
        Function<Object, Void> responseFunc = req.getResponseFunc();
        Function<String, Void> errorFunc = req.getErrorFunc();
        boolean headersFlag = req.getHeadersFlag();

        return new JsonObjectRequest
                (Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responseFunc.apply(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String errorMsg = errorMsgHandler(error);
                        errorFunc.apply(errorMsg);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

//                  ----->  If true is given through headersFlag parameter the Post request will be sent with Headers
                if (headersFlag) {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Token " + getUserToken());  //<-- Token in Abstract Class RestApi
                    return headers;

//                  ----->  If false is given through headersFlag parameter the Post request will not be sent with Headers
                } else {
                    return Collections.emptyMap();
                }
            }
        };
    }


    public void requestPostJsonObj(RequestQueue que, JsonObjectRequest req) {
        que.add(req);
    }

    public void requestPostJsonObj(JsonRequest req) {
        if (req.isTest()) {
            return;
        }

        String url = req.getUrl();
        HashMap<String, Object> params = req.getParams();
        Function<Object, Void> responseFunc = req.getResponseFunc();
        Function<String, Void> errorFunc = req.getErrorFunc();
        boolean headersFlag = req.getHeadersFlag();

        //-- Transforms params HashMap into Json Object
        JSONObject jsonParams = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responseFunc.apply(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String errorMsg = errorMsgHandler(error);
                        errorFunc.apply(errorMsg);
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

        addToVolleyQueue(jsonObjectRequest);
    }

    public void requestDeleteJsonObj(JsonRequest req) {
        if (req.isTest()) {
            return;
        }

        String url = req.getUrl();
        HashMap<String, Object> params = req.getParams();
        Function<Object, Void> responseFunc = req.getResponseFunc();
        Function<String, Void> errorFunc = req.getErrorFunc();
        boolean headersFlag = req.getHeadersFlag();

        //-- Transforms params HashMap into Json Object
        JSONObject jsonParams = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.DELETE, url, jsonParams, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        responseFunc.apply(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String errorMsg = errorMsgHandler(error);
                        errorFunc.apply(errorMsg);
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

        addToVolleyQueue(jsonObjectRequest);
    }

    public void requestGetJsonObj(JsonRequest req){
        if (req.isTest()) {
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = req.getUrl();
        Function<Object, Void> responseFunc = req.getResponseFunc();
        Function<String, Void> errorFunc = req.getErrorFunc();
        boolean headersFlag = req.getHeadersFlag();

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

                        String errorMsg = errorMsgHandler(error);
                        errorFunc.apply(errorMsg);

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

    public void requestGetJsonArray(JsonRequest req){
        if (req.isTest()) {
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = req.getUrl();
        Function<Object, Void> responseFunc = req.getResponseFunc();
        Function<String, Void> errorFunc = req.getErrorFunc();
        boolean headersFlag = req.getHeadersFlag();


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

                        String errorMsg = errorMsgHandler(error);
                        errorFunc.apply(errorMsg);

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

    public void requestPostJsonArray(JsonRequest req){
        if (req.isTest()) {
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = req.getUrl();
        HashMap<String, Object> params = req.getParams();
        Function<Object, Void> responseFunc = req.getResponseFunc();
        Function<String, Void> errorFunc = req.getErrorFunc();
        boolean headersFlag = req.getHeadersFlag();

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

                        String errorMsg = errorMsgHandler(error);
                        errorFunc.apply(errorMsg);

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

    //____________________________________________________________________

    public void requestGetJsonObj(String url, final Function<JSONObject, Void> responseFunc,
                        final Function<String, Void> errorFunc, final boolean headersFlag){

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
                        String errorMsg = "Unexpected Error!";
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            errorMsg = "No connection or you timed out. Try again.";
                        } else if (error instanceof AuthFailureError) {
                            errorMsg = "You are not authorized.";
                        } else if (error instanceof ServerError) {
                            errorMsg = "does not exist.";
                        } else if (error instanceof NetworkError) {
                            errorMsg = "Network Error. Try again.";
                        } else if (error instanceof ParseError) {
                            errorMsg = "Parse Error. Try again.";
                        }

                        errorFunc.apply(errorMsg);
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

        addToVolleyQueue(jsonObjectRequest);
    }


    public void requestPostJsonObj(String url, HashMap<String,Object> params,
                        final Function<JSONObject, Void> responseFunc, final Function<String, Void> errorFunc, final boolean headersFlag){

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
                        String errorMsg = "Unexpected Error!";
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            errorMsg = "No connection or you timed out. Try again.";
                        } else if (error instanceof AuthFailureError) {
                            errorMsg = "You are not authorized.";
                        } else if (error instanceof ServerError) {
                            errorMsg = "does not exist.";
                        } else if (error instanceof NetworkError) {
                            errorMsg = "Network Error. Try again.";
                        } else if (error instanceof ParseError) {
                            errorMsg = "Parse Error. Try again.";
                        }

                        errorFunc.apply(errorMsg);
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

        addToVolleyQueue(jsonObjectRequest);
    }

    public void requestGetJsonArray(String url, final Function<JSONArray, Void> responseFunc,
                        final Function<String, Void> errorFunc, final boolean headersFlag){

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
                        String errorMsg = "Unexpected Error!";
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            errorMsg = "No connection or you timed out. Try again.";
                        } else if (error instanceof AuthFailureError) {
                            errorMsg = "You are not authorized.";
                        } else if (error instanceof ServerError) {
                            errorMsg = "does not exist.";
                        } else if (error instanceof NetworkError) {
                            errorMsg = "Network Error. Try again.";
                        } else if (error instanceof ParseError) {
                            errorMsg = "Parse Error. Try again.";
                        }

                        errorFunc.apply(errorMsg);
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

        addToVolleyQueue(jsonObjectRequest);
    }

    public void requestPostJsonArray(String url, HashMap<String,Object> params,
                        final Function<JSONArray, Void> responseFunc, final Function<String, Void> errorFunc, final boolean headersFlag){
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
                        String errorMsg = "Unexpected Error!";
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            errorMsg = "No connection or you timed out. Try again.";
                        } else if (error instanceof AuthFailureError) {
                            errorMsg = "You are not authorized.";
                        } else if (error instanceof ServerError) {
                            errorMsg = "does not exist.";
                        } else if (error instanceof NetworkError) {
                            errorMsg = "Network Error. Try again.";
                        } else if (error instanceof ParseError) {
                            errorMsg = "Parse Error. Try again.";
                        }

                        errorFunc.apply(errorMsg);
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

        addToVolleyQueue(jsonObjectRequest);
    }

}