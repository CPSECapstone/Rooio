package com.rooio.repairs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class JobsCustomerAdapter implements ListAdapter {
    private ArrayList<JSONObject> arrayList;
    private Context context;
    ArrayList<String> statuses = new ArrayList<>();
    ArrayList<String> categories = new ArrayList<>();

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

    @SuppressLint("ResourceAsColor")
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
            TextView error = convertView.findViewById(R.id.error);

            TextView status = convertView.findViewById(R.id.status);
            ConstraintLayout color = convertView.findViewById(R.id.color);
            try {
                Integer status_enum = data.getInt("status");
                String status_value = null;
                switch(status_enum) {
                    case 1:
                        status_value = "Pending";
                        color.setBackgroundColor(Color.parseColor("#9A50BA"));

                        break;
                    case 2:
                        status_value = "Accepted";
                        color.setBackgroundColor(Color.parseColor("#96B8F8"));

                        break;
                    case 3:
                        status_value = "Completed";
                        color.setBackgroundColor(Color.parseColor("#A6A9AC"));

                        break;
                    case 4:
                        status_value = "Canceled";
                        break;
                    case 5:
                        status_value = "Started";
                        color.setBackgroundColor(Color.parseColor("#00CA8F"));
                        break;
                    case 6:
                        status_value = "Paused";
                        color.setBackgroundColor(Color.parseColor("#F6E15C"));

                        break;
                    case 0:
                        status_value = "Pending";
                        color.setBackgroundColor(Color.parseColor("#9A50BA"));

                        break;
                }


                JSONArray equipmentObjList = data.getJSONArray("equipment");
                for (int i = 0; i < equipmentObjList.length(); i++) {
                    JSONObject equipmentObj = equipmentObjList.getJSONObject(i);
                    String category = equipmentObj.getString("service_category");

                    switch(category) {
                        case "0":
//                            repairType.setText("General Appliance");
                            categories.add("General Appliance");
                            break;
                        case "1":
//                            repairType.setText("HVAC");
                            categories.add("HVAC");
                            break;
                        case "2":
//                            repairType.setText("Lighting and Electrical");
                            categories.add("Lighting and Electrical");
                            break;
                        case "3":
//                            repairType.setText("Plumbing");
                            categories.add("Plumbing");
                            break;
                    }
                }

                JSONObject locationObj = data.getJSONObject("service_location");
                JSONObject internal_client = locationObj.getJSONObject("internal_client");

                name.setText(internal_client.getString("name"));

                address.setText(locationObj.getString("physical_address"));

                timeImage.setText(data.getString("status_time_value"));


                Picasso.with(context)
                        .load(internal_client.getString("logo")
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
                error.setText(e.toString());
                Log.d("exception", e.toString());
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
