package com.rooio.repairs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LocationLogin extends AppCompatActivity implements AdapterView.OnItemClickListener{

    EditText et;
    Button bt;
    ListView lv;
    ArrayList<String> address_list;
    ArrayAdapter<String> adapter;

    String incoming_name = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_login);

        bt = (Button)findViewById(R.id.add_location);
        lv = (ListView)findViewById(R.id.Service);


        //Array to hold all the addresses
        address_list = new ArrayList<String>();
        address_list.add("1 Grand Ave San Luis Obispo, CA 93405");
        adapter = new ArrayAdapter<String>(LocationLogin.this, android.R.layout.simple_list_item_1, address_list);
        lv.setAdapter(adapter);

        //Check for new addresses added from AddLocation Page
        Intent incoming_intent = getIntent();
        incoming_name = incoming_intent.getStringExtra("result");
        if(incoming_name!=null){
            address_list.add(incoming_name);
        }
        adapter.notifyDataSetChanged();

        //Launch listener for "Add New Location"
        onBtnClick();

        lv.setOnItemClickListener(this);

    }

    public void onBtnClick(){
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LocationLogin.this, AddLocation.class);
                startActivity(intent1);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = (TextView)view;
        Toast.makeText(this, "You chose"+tv.getText()+position, Toast.LENGTH_SHORT).show();

        Intent intent1 = new Intent(LocationLogin.this, PreferredProvidersLogin.class);
        startActivity(intent1);

    }
}
