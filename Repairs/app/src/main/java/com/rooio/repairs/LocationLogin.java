package com.rooio.repairs;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.arch.core.util.Function;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationLogin extends RestApi implements AdapterView.OnItemClickListener {

    Button bt;
    ListView lv;
    TextView error_message;

    ArrayAdapter<String> adapter;

    //TextView test;
    static ArrayList<String> address_list = new ArrayList<String>();

    String incoming_name = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setElevation(0);

        bt = (Button) findViewById(R.id.add_location);
        lv = (ListView) findViewById(R.id.Service);
        error_message = (TextView) findViewById(R.id.Error_Messages);
//        test = (TextView) findViewById(R.id.test);

        String url = "https://capstone.api.roopairs.com/v0/service-locations/";

        //New Address Recieved
        Intent incoming_intent = getIntent();
        incoming_name = incoming_intent.getStringExtra("result");
        if (incoming_name != null) {

            //getPost here

            address_list.add(incoming_name);
            adapt();
            adapter.notifyDataSetChanged();
            //     -- Example params initiations
            HashMap<String, Object> params = new HashMap<>();
            params.put("physical_address", incoming_name);

            JsonRequest request = new JsonRequest(false, url, params, responseFunc1, errorFunc, true);
            requestPostJsonObj(request);
        }
        else{
            address_list.clear();

            JsonRequest request = new JsonRequest(false, url, null, responseFunc2, errorFunc, true);
            requestGetJsonArray(request);
        }

        //Launch listener for "Add New Location"
        onBtnClick();

        lv.setOnItemClickListener(this);

    }

    public Function<Object,Void> responseFunc1 = (jsonObj) -> null;

    public Function<Object,Void> responseFunc2 = (jsonResponse) -> {
        try {
            JSONArray jsonArray = (JSONArray) jsonResponse;
            addElements(jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapt();
        return null;
    };


    public Function<String,Void> errorFunc = (string) -> {
        error_message.setText("Invalid Address");
//        error_message.setText(string);

        return null;
    };

    public void onBtnClick() {
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
        TextView tv = (TextView) view;
        Toast.makeText(this, "You chose " + tv.getText() + position, Toast.LENGTH_SHORT).show();

        Intent intent1 = new Intent(LocationLogin.this, PreferredProvidersLogin.class);
        startActivity(intent1);

    }
    
    public void addElements(JSONArray response) throws JSONException {
        for (int i = 0; i < response.length(); i++) {
            JSONObject restaurant = response.getJSONObject(i);
            String physical_address = (String) restaurant.get("physical_address");
            address_list.add(physical_address);
        }
    }

    public void adapt(){
        adapter = new ArrayAdapter<String>(LocationLogin.this, android.R.layout.simple_list_item_1, address_list);
        lv.setAdapter(adapter);
    }
}
