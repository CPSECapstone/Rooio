package com.rooio.repairs;

import android.accounts.Account;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import java.lang.Object;
import java.util.regex.Pattern;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.inventory.InventoryConnector;
import com.clover.sdk.v3.inventory.Item;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

        private Account mAccount;
        private InventoryConnector mInventoryConnector;
        private TextView mTextView;
        private EditText username;
        private EditText password;
        private Button login;
        private TextView success;
        private TextView unsuccess;




    @Override
        protected void onCreate (Bundle savedInstanceState) {
        Log.v("myTag", "This is in create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login = (Button) findViewById(R.id.login_button);
        username = (EditText) findViewById(R.id.username_field);
        password = (EditText) findViewById(R.id.password_field);
        success = (TextView) findViewById(R.id.success);
        unsuccess = (TextView) findViewById(R.id.unsuccess);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((validate(username.getText().toString(), password.getText().toString())) == true){
                    success.setText("Successful Login");
                    unsuccess.setText("");

                }
                else{
                    unsuccess.setText("Unsuccessful Login");
                    success.setText("");

                }
            }
        });

    }

    public boolean validate(String userName, String userPassword){
        if((isValid(userName, userPassword)) == true){
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean isValid(String username, String passwordhere) {

        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
        boolean flag = true;

        if (username.length() <= 0) {
            flag=false;
            return flag;
        }
        if (passwordhere.length() < 6) {
            flag=false;
            return flag;
        }
        if (!digitCasePatten.matcher(passwordhere).find()) {
            flag=false;
            return flag;
        }
        return flag;
    }

        @Override
        protected void onResume () {
        Log.v("myTag", "This is resume");
        super.onResume();

        mTextView = findViewById(R.id.activity_main_text_view);
        //AccountManager am = get(this);
        //Account[] accounts = am.getAccountsByType("com.google");
        ;//
        // mAccount = accounts[0];
        // Retrieve the Clover account
        if (mAccount == null) {
            mAccount = CloverAccount.getAccount(this);
            if (mAccount == null) {
                return;
            }
        }

        // Connect InventoryConnector
        connect();

        // Get Item
        new Login.InventoryAsyncTask().execute();
    }

        @Override
        protected void onDestroy () {
        super.onDestroy();
        disconnect();
    }

        private void connect () {
        disconnect();
        if (mAccount != null) {
            mInventoryConnector = new InventoryConnector(this, mAccount, null);
            mInventoryConnector.connect();
        }
    }

        private void disconnect () {
        if (mInventoryConnector != null) {
            mInventoryConnector.disconnect();
            mInventoryConnector = null;
        }
    }

        private class InventoryAsyncTask extends AsyncTask<Void, Void, Item> {

            @Override
            protected final Item doInBackground(Void... params) {
                try {
                    //Get inventory item

                    return mInventoryConnector.getItems().get(0);

                } catch (RemoteException | ClientException | ServiceException | BindingException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected final void onPostExecute(Item item) {
                if (item != null) {
                    mTextView.setText(item.getName());
                }
            }
        }
}
