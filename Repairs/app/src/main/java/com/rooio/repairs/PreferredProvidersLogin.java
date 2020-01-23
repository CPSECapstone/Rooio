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

public class PreferredProvidersLogin extends RestApi implements AdapterView.OnItemClickListener {

    Button addButton;
    Button doneButton;
    ListView serviceProvidersListView;
    ArrayAdapter<String> adapter;

    TextView error;
    static ArrayList<String> preferredProviders = new ArrayList<String>();

    String addedProviderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_providers_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        addButton = (Button) findViewById(R.id.add_another_provider);
        doneButton = (Button) findViewById(R.id.Done);
        serviceProvidersListView = (ListView) findViewById(R.id.Service2);
        error = (TextView) findViewById(R.id.error);

        loadPreferredProviders();

        onAddClick();
        onDoneClick();
        serviceProvidersListView.setOnItemClickListener(this);
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
            preferredProviders.add(name);
        }
        adapt();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    public void adapt() {
        adapter = new ArrayAdapter<String>(PreferredProvidersLogin.this, android.R.layout.simple_list_item_1, preferredProviders);
        serviceProvidersListView.setAdapter(adapter);
    }
}
