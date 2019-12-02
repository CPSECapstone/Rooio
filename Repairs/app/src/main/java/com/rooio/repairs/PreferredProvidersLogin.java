package com.rooio.repairs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PreferredProvidersLogin extends AppCompatActivity {

protected void onCreate(Bundle savedInstanceState){
        Button button;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_providers_login);

        button = findViewById(R.id.add_another_provider);
        button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                        openAddPreferredProvidersLogin();
                }
        });
}
        public void openAddPreferredProvidersLogin(){
                Intent intent = new Intent(this, AddPreferredProvidersLogin.class);
                startActivity(intent);
        }

}
//    EditText et;
//    Button bt;
//            ListView lv;
//            ArrayList<String> provider_list;
//            ArrayAdapter<String> adapter;
//
//            String incoming_name = null;


//@Override
//protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_preferred_providers_login);
//
//        bt = (Button)findViewById(R.id.add_provider);
        //lv = (ListView)findViewById(R.id.Service2);


        //Array to hold all the providers
        //provider_list = new ArrayList<String>();
        //provider_list.add("(310)245-4343");
        //adapter = new ArrayAdapter<String>(PreferredProvidersLogin.this, android.R.layout.simple_list_item_1, provider_list);
        //lv.setAdapter(adapter);

        //Check for new providers added from AddPreferredProviderLogin Page
        //Intent incoming_intent = getIntent();
        //incoming_name = incoming_intent.getStringExtra("result");
        //if(incoming_name!=null){
        //provider_list.add(incoming_name);
        //}
        //adapter.notifyDataSetChanged();

        //Launch listener for "Add New Location"
//        onBtnClick();
//
//        }

//public void onBtnClick(){
//        bt.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        Intent intent1 = new Intent(PreferredProvidersLogin.this, AddPreferredProvidersLogin.class);
//        startActivity(intent1);
//        }
//        });
//}
//        }

