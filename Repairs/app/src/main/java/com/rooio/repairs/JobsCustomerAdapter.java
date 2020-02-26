package com.rooio.repairs;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class JobsCustomerAdapter implements ListAdapter {
    private ArrayList<JSONObject> arrayList;
    private Context context;
    ArrayList<String> statuses = new ArrayList<>();

    public JobsCustomerAdapter(Context context, ArrayList<JSONObject> serviceLocations) {
        this.arrayList = serviceLocations;
        this.context = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject data = arrayList.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.job_list_row, parent, false);
            TextView repairType = convertView.findViewById(R.id.repairType);
            TextView name = convertView.findViewById(R.id.name);
            TextView address = convertView.findViewById(R.id.address);
            ImageView image = convertView.findViewById(R.id.image);
            TextView timeImage = convertView.findViewById(R.id.timeImage);
            TextView status = convertView.findViewById(R.id.status);

            try {
                Integer status_enum = data.getInt("status");
                String status_value = null;
                switch(status_enum) {
                    case 1:
                        status_value = "Pending";
                        break;
                    case 2:
                        status_value = "Declined";
                        break;
                    case 3:
                        status_value = "Completed";
                        break;
                    case 4:
                        status_value = "Canceled";
                        break;
                    case 5:
                        status_value = "Started";
                        break;
                    case 6:
                        status_value = "Paused";
                        break;
                    case 0:
                        status_value = "Pending";
                        break;
                }
                JSONObject equipmentObj = data.getJSONObject("equipment");
                repairType.setText(equipmentObj.getInt("service_category"));

                JSONObject locationObj = data.getJSONObject("service_location");
                name.setText(locationObj.getString("name"));

                address.setText(locationObj.getString("physical_address"));

                timeImage.setText(data.getString("estimated_arrival_time"));

                JSONObject userObject = locationObj.getJSONObject("internal_client");

                Picasso.with(context)
                        .load(userObject.getString("logo")
                        )
                        .into(image);

                if(!statuses.contains(status_value)){
                    status.setText(status_value.toUpperCase());
                    statuses.add(status_value);
                }
                else
                    status.setVisibility(View.GONE);
                }

            catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return 1;
    }
    @Override
    public boolean isEmpty() {
        return false;
    }
}
