package com.rooio.repairs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.arch.core.util.Function;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddLocationLogin extends RestApi {

    private Button addAddress;
    private ImageView backButton;
    private EditText newAddress;
    private TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location_login);
        addAddress = (Button) findViewById(R.id.addLocation);
        backButton = (ImageView) findViewById(R.id.backArrow);
        newAddress = (EditText) findViewById(R.id.newLocationLogin);
        errorMessage = (TextView) findViewById(R.id.errorMessage);
        errorMessage.setText("");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setElevation(0);

        onAddClick();
        onBackClick();
    }

    private void onAddClick() {
        String url = "https://capstone.api.roopairs.com/v0/service-locations/";
        
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorMessage.setTextColor(Color.parseColor("#A6A9AC"));
                errorMessage.setText("Loading");

                String inputted_address = newAddress.getText().toString();
                if (!inputted_address.equals("")){
                    //     -- Example params initiations
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("physical_address", inputted_address);

                    JsonRequest request = new JsonRequest(false, url, params, responseFunc1, errorFunc, true);
                    requestPostJsonObj(request);

                } else {
                    errorMessage.setTextColor(Color.parseColor("#E4E40B0B"));
                    errorMessage.setText("Invalid Address");
                }
            }
        });
        onCancel();
    }

    private void onCancel() {
        backButton.setOnClickListener(view ->
                startActivity(new Intent(AddLocationLogin.this, LocationLogin.class)));
    }

    private void onBackClick() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddLocationLogin.this, LocationLogin.class));
            }
        });
    }

    public Function<Object,Void> responseFunc1 = (jsonObj) -> {
        JSONObject responseObj = (JSONObject) jsonObj;
        String address = null;
        String id = null;

        try {
            address = responseObj.getString("physical_address");
            id = responseObj.getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(AddLocationLogin.this, LocationLogin.class);
        intent.putExtra("address", address);
        intent.putExtra("id", id);
        startActivity(intent);
        return null;
    };

    public Function<String,Void> errorFunc = (string) -> {
        errorMessage.setTextColor(Color.parseColor("#E4E40B0B"));
        errorMessage.setText("Invalid Address");
        return null;
    };


}
