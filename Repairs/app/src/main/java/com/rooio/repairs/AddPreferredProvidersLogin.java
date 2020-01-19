package com.rooio.repairs;

import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import androidx.arch.core.util.Function;

public class AddPreferredProvidersLogin extends RestApi {

    private Button addButton;
    private Button backButton;
    private EditText newProvider;
    private TextView error;
    String phoneInput;
    static ArrayList<String> addedProvidersList = new ArrayList<String>();
    String addedProviderRet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preferred_providers_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        addButton = (Button) findViewById(R.id.add_provider);
        backButton = (Button) findViewById(R.id.back);
        newProvider = (EditText) findViewById(R.id.new_phone);
        error = (TextView) findViewById(R.id.error);

        onAddClick();
        onBackClick();
    }

    private void onBackClick() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPreferredProvidersLogin.this, PreferredProvidersLogin.class);
                startActivity(intent);
            }
        });
    }

    private void onAddClick() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setText("");
                phoneInput = newProvider.getText().toString();
                if(!phoneInput.isEmpty())
                    addPreferredServiceProvider(phoneInput);
                else
                    error.setText("Please enter a phone number!");
            }
        });
    }

    public void addPreferredServiceProvider(String phoneInput){
        String url = "https://capstone.api.roopairs.com/v0/service-providers/";

        HashMap<String, Object> params = new HashMap<>();
        params.put("phone", phoneInput);

        Function<JSONArray,Void> responseFunc = (jsonArray) -> {
            checkIfAlreadyAdded();
            return null;
        };

        Function<String, Void> errorFunc = (string) -> {
            return null;
        };

        requestPostJsonArray(url, params, responseFunc, errorFunc,true);
    }

    private void checkIfAlreadyAdded() {

        String url = "https://capstone.api.roopairs.com/v0/service-providers/";

        Function<JSONArray, Void> responseFunc = (jsonArray) -> {
            try {
                Intent incoming_intent = getIntent();
                addedProvidersList = incoming_intent.getStringArrayListExtra("alreadyAddedList");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject restaurant = jsonArray.getJSONObject(0);
                    String addedProvider = (String) restaurant.get("name");

                    if(addedProvidersList.contains(addedProvider)){
                        addedProviderRet = addedProvider;
                    }
                }

                // returning added service provider for error checking
                Intent intent = new Intent(AddPreferredProvidersLogin.this, PreferredProvidersLogin.class);
                intent.putExtra("added", addedProviderRet);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        };

        Function<String, Void> errorFunc = (string) -> {
            Log.i("try", "uhoh");
            return null;
        };

        requestGetJsonArray(url, responseFunc, errorFunc, true);
    }

}