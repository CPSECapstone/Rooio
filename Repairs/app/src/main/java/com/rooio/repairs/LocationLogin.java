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

public class LocationLogin extends RestApi implements AdapterView.OnItemClickListener{

    EditText et;
    Button bt;
    ListView lv;
    ArrayList<String> address_list;
    ArrayAdapter<String> adapter;

    TextView test;

    String incoming_name = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_login);

        bt = (Button)findViewById(R.id.add_location);
        lv = (ListView)findViewById(R.id.Service);
        test = (TextView)findViewById(R.id.test);

        String url = "https://capstone.api.roopairs.com/v0/service-locations/";

        //Array to hold all the addresses
        address_list = new ArrayList<String>();

        requestGetArray(url,true);
//        test.setText("Token: " + getUserToken());

        adapter = new ArrayAdapter<String>(LocationLogin.this, android.R.layout.simple_list_item_1, address_list);
        lv.setAdapter(adapter);

        //Check for new addresses added from AddLocation Page
        Intent incoming_intent = getIntent();
        incoming_name = incoming_intent.getStringExtra("result");
        if(incoming_name!=null){

            //getPost here

            address_list.add(incoming_name);
        }
        adapter.notifyDataSetChanged();

        //Launch listener for "Add New Location"
        onBtnClick();

        lv.setOnItemClickListener(this);

    }

    public void onBtnClick(){
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LocationLogin.this, AddLocation.class);
                startActivity(intent1);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = (TextView)view;
        Toast.makeText(this, "You chose"+tv.getText()+position, Toast.LENGTH_SHORT).show();

        Intent intent1 = new Intent(LocationLogin.this, PreferredProvidersLogin.class);
        startActivity(intent1);

    }

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


//                ----->
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

//          TODO: ----->  Error Handling functions


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

    public void requestGetArray(String url, final boolean headersFlag){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray responseObj = response;

//          TODO: ----->  Start specialized json handling function
                        test.setText("hello");

//                ----->
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

//          TODO: ----->  Error Handling functions
<<<<<<< HEAD
                        //test.setText("Not working");
=======
                        test.setText("Token: " + getUserToken()+ "\n" + error.toString());
>>>>>>> edd5324030ab752d627b09c1ef6dbc6a3a134e25

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
}
