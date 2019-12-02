package com.rooio.repairs;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class AddPreferredProvidersLogin extends AppCompatActivity {

//    private Button add_provider;
//    private EditText new_provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button button;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preferred_providers_login);

        button = findViewById(R.id.add_provider);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openPreferredProvidersLogin();
            }
        });
    }
    public void openPreferredProvidersLogin(){
        Intent intent = new Intent(this, PreferredProvidersLogin.class);
        startActivity(intent);
    }

}
//        add_provider = (Button) findViewById(R.id.add_provider);
//        new_provider = (EditText) findViewById(R.id.new_phone);
//
//                add_provider.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
                        //String result = new_provider.getText().toString();

//                        Intent intent = new Intent(AddPreferredProvidersLogin.this, PreferredProvidersLogin.class);
                        //intent.putExtra("result", result);
//                startActivity(intent);
//            }
//        });
//    }
//}
