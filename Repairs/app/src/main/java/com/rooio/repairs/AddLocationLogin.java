package com.rooio.repairs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.arch.core.util.Function;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class AddLocationLogin extends RestApi {

    private Button add_address;
    private Button backButton;
    private TextInputEditText new_address;
    private TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        add_address = (Button) findViewById(R.id.add_location);
        backButton = (Button) findViewById(R.id.cancel);
        new_address = (TextInputEditText) findViewById(R.id.new_location);
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
        
        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = new_address.getText().toString();

                if (!result.equals("")){
                    //     -- Example params initiations
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("physical_address", result);

                    JsonRequest request = new JsonRequest(false, url, params, responseFunc1, errorFunc, true);
                    requestPostJsonObj(request);

                    Intent intent = new Intent(AddLocationLogin.this, LocationLogin.class);
                    intent.putExtra("result", result);
                    startActivity(intent);

                } else {
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

    public Function<Object,Void> responseFunc1 = (jsonObj) -> null;

    public Function<String,Void> errorFunc = (string) -> {
        errorMessage.setText("Invalid Address");
        return null;
    };


}
