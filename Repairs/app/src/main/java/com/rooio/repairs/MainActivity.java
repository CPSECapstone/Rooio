package com.rooio.repairs;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v3.inventory.InventoryConnector;
import com.clover.sdk.v3.inventory.Item;

import static android.accounts.AccountManager.get;


public class MainActivity extends AppCompatActivity {

    private Account mAccount;
    private InventoryConnector mInventoryConnector;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("myTag", "This is in create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        Log.v("myTag", "This is resume");
        super.onResume();

        mTextView = findViewById(R.id.activity_main_text_view);
        //AccountManager am = get(this);
        //Account[] accounts = am.getAccountsByType("com.google");
        //
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
        new InventoryAsyncTask().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect();
    }

    private void connect() {
        disconnect();
        if (mAccount != null) {
            mInventoryConnector = new InventoryConnector(this, mAccount, null);
            mInventoryConnector.connect();
        }
    }

    private void disconnect() {
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