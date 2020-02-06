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
    static ArrayList<String> address_list = new ArrayList<>();
    static HashMap<String, String> locationIds = new HashMap<>();

    String incoming_address = null;
    String incoming_id = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setElevation(0);

        bt = (Button) findViewById(R.id.addLocation);
        lv = (ListView) findViewById(R.id.Service);
        error_message = (TextView) findViewById(R.id.Error_Messages);
//        test = (TextView) findViewById(R.id.test);

        String url = "https://capstone.api.roopairs.com/v0/service-locations/";

        //New Address Recieved
        Intent incoming_intent = getIntent();
        incoming_address = incoming_intent.getStringExtra("address");
        if (incoming_address != null) {

            incoming_id = incoming_intent.getStringExtra("id");
            locationIds.put(incoming_address, incoming_id);

            address_list.add(incoming_address);
            adapt();
            adapter.notifyDataSetChanged();
        }
        else{
            address_list.clear();
            locationIds.clear();

            JsonRequest request = new JsonRequest(false, url, null, responseFunc, errorFunc, true);
            requestGetJsonArray(request);
        }

        //Launch listener for "Add New Location"
        onBtnClick();

        lv.setOnItemClickListener(this);

    }


    public Function<Object,Void> responseFunc = (jsonResponse) -> {
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
        error_message.setText(string);
        return null;
    };

    public void onBtnClick() {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LocationLogin.this, AddLocationLogin.class);
                startActivity(intent1);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = (TextView) view;
        Toast.makeText(this, "You chose " + tv.getText() + " " + position, Toast.LENGTH_SHORT).show();

        setUserLocationID(locationIds.get(tv.getText()));

        error_message.setText(getUserLocationID());

        Intent intent1 = new Intent(LocationLogin.this, PreferredProvidersLogin.class);
        startActivity(intent1);

    }

    public void addElements(JSONArray response) throws JSONException {
        for (int i = 0; i < response.length(); i++) {
            JSONObject restaurant = response.getJSONObject(i);
            String physical_address = restaurant.getString("physical_address");
            address_list.add(physical_address);

            locationIds.put(physical_address, restaurant.getString("id"));
        }
    }

    public void adapt(){
        adapter = new ArrayAdapter<String>(LocationLogin.this, android.R.layout.simple_list_item_1, address_list);
        lv.setAdapter(adapter);
    }
}
