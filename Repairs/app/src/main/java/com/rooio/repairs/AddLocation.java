package com.rooio.repairs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Button;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

public class AddLocation extends AppCompatActivity {

    private Button add_address;
    private TextInputEditText new_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        add_address = (Button) findViewById(R.id.add_location);
        new_address = (TextInputEditText) findViewById(R.id.new_location);

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = new_address.getText().toString();

                Intent intent = new Intent(AddLocation.this, LocationLogin.class);
                intent.putExtra("result", result);
                startActivity(intent);
            }
        });
    }


}
