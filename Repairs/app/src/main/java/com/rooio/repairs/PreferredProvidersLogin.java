package com.rooio.repairs;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.w3c.dom.Text;

public class PreferredProvidersLogin extends RestApi implements AdapterView.OnItemClickListener {

        EditText et;
        Button bt;
        Button bt2;
        ListView lv;
        ArrayAdapter<String> adapter;

        TextView test;
        static ArrayList<String> address_list = new ArrayList<String>();

        String incoming_name = null;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_preferred_providers_login);

                bt = (Button) findViewById(R.id.add_another_provider);
                bt2 = (Button) findViewById(R.id.Done);
                lv = (ListView) findViewById(R.id.Service2);

                String url = "https://capstone.api.roopairs.com/v0/service-providers/";

                test = (TextView) findViewById(R.id.test);
//                test.setText(getUserToken());
                Intent incoming_intent = getIntent();
                incoming_name = incoming_intent.getStringExtra("result2");
                if ((incoming_name != null ) &  (incoming_name != "")){
                        test.setText("not null");
                        //getPost here
                        adapt();
                        adapter.notifyDataSetChanged();
                        //     -- Example params initiations
                        HashMap<String, String> params = new HashMap<>();
                        params.put("phone", incoming_name);

                        requestPost(url, params, true);
                }
                else{
                        test.setText("null");
                        requestGetArray(url, true);

                }

//        adapter = new ArrayAdapter<String>(LocationLogin.this, android.R.layout.simple_list_item_1, address_list);
//        lv.setAdapter(adapter);

                //Check for new addresses added from AddLocation Page


                //Launch listener for "Add New Location"
                onBtnClick();
                onBtnClick2();
                lv.setOnItemClickListener(this);

        }

        public void onBtnClick() {
                bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent1 = new Intent(PreferredProvidersLogin.this, AddPreferredProvidersLogin.class);
                                startActivity(intent1);
                        }
                });
        }

        public void onBtnClick2() {
                bt2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                Intent intent3 = new Intent(PreferredProvidersLogin.this, Splash_Screen.class);
                                startActivity(intent3);
                        }
                });
        }


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }

        public void requestPost(final String url, HashMap<String, String> params, final boolean headersFlag) {
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
//                                        try {
//                                                getProvider(responseObj);
//
//                                        } catch (JSONException e) {
//                                                e.printStackTrace();
//                                        }
//                                        adapt();
//                ----->
                                }
                        }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {

//          TODO: ----->  Error Handling functions

                                        requestGetArray(url, true);
//                                      test.setText("error123"+error.toString());

//                ----->
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

//  --> Equivalent of sending the request. Required to WORK...
                queue.add(jsonObjectRequest);

        }

        public void requestGetArray(String url, final boolean headersFlag) {
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                        JSONArray responseObj = response;

//          TODO: ----->  Start specialized json handling function
                                        try {
                                                test.setText(responseObj.toString());
                                                addElements(responseObj);

                                        } catch (JSONException e) {
                                                e.printStackTrace();
                                        }
                                        adapt();
                                        //test.setText(responseObj.toString());
//                ----->
                                }
                        }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {

//          TODO: ----->  Error Handling functions

                        //test.setText("Token: " + getUserToken() + "\n" + error.toString());

//                ----->
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

//  --> Equivalent of sending the request. Required to WORK...
                queue.add(jsonObjectRequest);

        }

        public void getProvider(JSONObject response) throws JSONException {
                //test.setText(response.toString());
                String provider_name = (String) response.get("name");
                address_list.add(provider_name);

        }

        public void addElements(JSONArray response) throws JSONException {
//                test.setText((Integer.toString(response.length())));
                address_list.clear();
                for (int i = 0; i < response.length(); i++) {
                        JSONObject restaurant = response.getJSONObject(i);
                        String physical_address = (String) restaurant.get("name");
                        address_list.add(physical_address);

                }
        }

        public void adapt(){
                adapter = new ArrayAdapter<String>(PreferredProvidersLogin.this, android.R.layout.simple_list_item_1, address_list);
                lv.setAdapter(adapter);
        }
}
