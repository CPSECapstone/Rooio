package com.rooio.repairs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.arch.core.util.Function;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PreferredProvidersLogin extends RestApi {

    Button addButton;
    Button doneButton;
    ListView serviceProvidersListView;
    ArrayAdapter<String> adapter;

    TextView error;
    static ArrayList<ServiceProviderData> preferredProviders = new ArrayList<>();

    String addedProviderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_providers_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        addButton = (Button) findViewById(R.id.add_another_provider);
        doneButton = (Button) findViewById(R.id.Done);
        serviceProvidersListView = (ListView) findViewById(R.id.list);
        error = (TextView) findViewById(R.id.error);

        loadPreferredProviders();

        onAddClick();
        onDoneClick();
        //serviceProvidersListView.setOnItemClickListener(this);
    }

    public void loadPreferredProviders() {
        String url = "https://capstone.api.roopairs.com/v0/service-providers/";

        Function<JSONArray, Void> responseFunc = (jsonArray) -> {
            try {
                loadElements(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        };

        Function<String, Void> errorFunc = (string) -> {
            error.setText(string);
            return null;
        };

        requestGetJsonArray(url, responseFunc, errorFunc, true);
    }

    public void loadElements(JSONArray response) throws JSONException {
        preferredProviders.clear();
        for (int i = 0; i < response.length(); i++) {
            JSONObject restaurant = response.getJSONObject(i);
            String name = (String) restaurant.get("name");
            String image = "";
            try{
                image = (String) restaurant.get("logo");

            } catch (Exception e){
                image = "https://roopairs-capstone.s3.amazonaws.com/media/service_agency_logos/chang-electric/mark.gif?AWSAccessKeyId=AKIAJUE3LGYRGQFUDYYQ&Signature=A%2BAz2fPJqYE%2FoJlFIm56nP%2F7yf0%3D&Expires=1579995508";
            } finally {
                ServiceProviderData serviceProviderData = new ServiceProviderData(name, image);
                preferredProviders.add(serviceProviderData);
            }
        }

        CustomAdapter customAdapter = new CustomAdapter(this, preferredProviders);
        serviceProvidersListView.setAdapter(customAdapter);
    }

    public void onAddClick() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PreferredProvidersLogin.this, AddPreferredProvidersLogin.class);
                startActivity(intent1);
            }
        });
    }

    public void onDoneClick() {
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent3 = new Intent(PreferredProvidersLogin.this, Dashboard.class);
                startActivity(intent3);
            }
        });
    }

}
