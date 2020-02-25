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

import java.util.ArrayList;

class JobsCustomerAdapter implements ListAdapter {
    private ArrayList<JobsData> arrayList;
    private Context context;
    ArrayList<String> statuses = new ArrayList<>();

    public JobsCustomerAdapter(Context context, ArrayList<JobsData> serviceLocations) {
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
        JobsData data = arrayList.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.job_list_row, parent, false);
            TextView repairType = convertView.findViewById(R.id.repairType);
            TextView name = convertView.findViewById(R.id.name);
            TextView address = convertView.findViewById(R.id.address);
            ImageView image = convertView.findViewById(R.id.image);
            TextView timeImage = convertView.findViewById(R.id.timeImage);
            TextView status = convertView.findViewById(R.id.status);

            if(!statuses.contains(data.status)){
                status.setText(data.status.toUpperCase());
            statuses.add(data.status);

        }
        else
            status.setVisibility(View.GONE);

        repairType.setText(data.repairType);
        name.setText(data.name);
        address.setText(data.address);
        timeImage.setText(data.time);
        Picasso.with(context)
                .load(data.image)
                .into(image);

//            TextView location = convertView.findViewById(R.id.location);
//            if(!statuses.contains(data.status)){
//                location.setText(data.status.toUpperCase());
//                statuses.add(data.status);
//            }
//            else
//                location.setVisibility(View.GONE);
//
//            // displaying equipment buttons
//            Button equipment = convertView.findViewById(R.id.equipmentItem);
//            equipment.setText(data.name);
//            Button equipment2 = convertView.findViewById(R.id.equipmentItem2);
//            equipment2.setText(data.name);
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
