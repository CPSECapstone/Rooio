package com.rooio.repairs;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.arch.core.util.Function;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//import com.android.volley.toolbox.JsonArrayRequest;

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

                Function<String, Void> errorFunc = (string) -> {
                        test.setText(string);
                        return null;
                };

                if ((incoming_name != null ) &  (incoming_name != "")){
                        test.setText("not null");
                        //getPost here
                        adapt();
                        adapter.notifyDataSetChanged();
                        //     -- Example params initiations
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("phone", incoming_name);

//                        requestPost(url, params, true);

                        Function<JSONArray,Void> responseFunc = (jsonArray) -> {
                                try {
                                        getProvider(jsonArray);
                                } catch (JSONException e){
                                        e.printStackTrace();
                                }
                                return null;
                        };
                        requestPostJsonArray(url, params, responseFunc, errorFunc,true);
                }
                else{
//                        test.setText("null");
//                        requestGetArray(url, true);

                        Function<JSONArray,Void> responseFunc = (jsonArray) -> {
                                try {
                                        addElements(jsonArray);
                                } catch (JSONException e){
                                        e.printStackTrace();
                                }
                                return null;
                        };
                        requestGetJsonArray(url, responseFunc, errorFunc, true);

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

                                Intent intent3 = new Intent(PreferredProvidersLogin.this, Dashboard.class);
                                startActivity(intent3);
                        }
                });
        }


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }


        public void getProvider(JSONArray response) throws JSONException {
                //test.setText(response.toString());
                String provider_name = (String) response.getJSONObject(0).get("name");
                address_list.add(provider_name);
                adapt();
        }

        public void addElements(JSONArray response) throws JSONException {
//                test.setText((Integer.toString(response.length())));
                address_list.clear();
                for (int i = 0; i < response.length(); i++) {
                        JSONObject restaurant = response.getJSONObject(i);
                        String physical_address = (String) restaurant.get("name");
                        address_list.add(physical_address);
                }
                adapt();
        }

        public void adapt(){
                adapter = new ArrayAdapter<String>(PreferredProvidersLogin.this, android.R.layout.simple_list_item_1, address_list);
                lv.setAdapter(adapter);
        }
}
