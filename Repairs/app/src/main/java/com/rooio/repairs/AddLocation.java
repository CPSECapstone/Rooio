package com.rooio.repairs;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Button;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;

public class AddLocation extends AppCompatActivity {

    private Button add_address;
    private EditText new_address;
    private ImageView backArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        add_address = (Button) findViewById(R.id.addLocation);
        new_address = findViewById(R.id.newLocation);
        backArrow = findViewById(R.id.backArrow);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setElevation(0);

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = new_address.getText().toString();

                Intent intent = new Intent(AddLocation.this, LocationLogin.class);
                intent.putExtra("result", result);
                startActivity(intent);
            }
        });
        onCancel();
    }

    private void onCancel() {
        backArrow.setOnClickListener(view ->
                startActivity(new Intent(AddLocation.this, LocationLogin.class)));
    }


}
