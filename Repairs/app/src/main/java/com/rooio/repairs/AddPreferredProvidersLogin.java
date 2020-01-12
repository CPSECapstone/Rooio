package com.rooio.repairs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Button;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

public class AddPreferredProvidersLogin extends AppCompatActivity {

    private Button add_provider;
    private EditText new_provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preferred_providers_login);
        add_provider = (Button) findViewById(R.id.add_provider);
        new_provider = (EditText) findViewById(R.id.new_phone);

        add_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = new_provider.getText().toString();

                Intent intent = new Intent(AddPreferredProvidersLogin.this, PreferredProvidersLogin.class);
                intent.putExtra("result2", result);
                startActivity(intent);
            }
        });
    }


}