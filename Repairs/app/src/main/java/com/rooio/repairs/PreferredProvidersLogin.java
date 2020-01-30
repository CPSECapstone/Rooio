package com.rooio.repairs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    TextView addButton;
    Button doneButton;
    ListView serviceProvidersListView;

    TextView error;
    static ArrayList<ServiceProviderData> preferredProviders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_providers_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setElevation(0);

        addButton = (TextView) findViewById(R.id.addAnother);
        doneButton = (Button) findViewById(R.id.Done);
        serviceProvidersListView = (ListView) findViewById(R.id.list);
        error = (TextView) findViewById(R.id.error);

        loadPreferredProviders();

        onAddClick();
        onDoneClick();
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
                // if there is no logo for the service provider
                image = "http://rsroemani.com/rv2/wp-content/themes/rsroemani/images/no-user.jpg";
            } finally {
                ServiceProviderData serviceProviderData = new ServiceProviderData(name, image);
                preferredProviders.add(serviceProviderData);
            }
        }

        CustomAdapter customAdapter = new CustomAdapter(this, preferredProviders);
        if(preferredProviders.size() != 0)
            serviceProvidersListView.setAdapter(customAdapter);
    }

    public void onAddClick() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PreferredProvidersLogin.this, AddPreferredProvidersLogin.class));
            }
        });
    }

    public void onDoneClick() {
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PreferredProvidersLogin.this, Dashboard.class));
            }
        });
    }

}
