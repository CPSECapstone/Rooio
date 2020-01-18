package com.rooio.repairs;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Button;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import androidx.arch.core.util.Function;

public class AddPreferredProvidersLogin extends RestApi {

    private Button addButton;
    private EditText newProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preferred_providers_login);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        addButton = (Button) findViewById(R.id.add_provider);
        newProvider = (EditText) findViewById(R.id.new_phone);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneInput = newProvider.getText().toString();

                addPreferredServiceProvider(phoneInput);

                Intent intent = new Intent(AddPreferredProvidersLogin.this, PreferredProvidersLogin.class);
                startActivity(intent);
            }
        });
    }

    public void addPreferredServiceProvider(String phoneInput){
        String url = "https://capstone.api.roopairs.com/v0/service-providers/";

        HashMap<String, Object> params = new HashMap<>();
        params.put("phone", phoneInput);

        Function<String, Void> errorFunc = (string) -> {
            //                       test.setText(string);
            return null;
        };

        Function<JSONArray,Void> responseFunc = (jsonArray) -> {
            return null;
        };

        requestPostJsonArray(url, params, responseFunc, errorFunc,true);
    }

}