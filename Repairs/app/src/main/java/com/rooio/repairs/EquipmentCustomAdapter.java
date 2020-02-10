package com.rooio.repairs;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;


import java.util.ArrayList;

class EquipmentCustomAdapter implements ListAdapter {
    ArrayList<EquipmentData> arrayList;
    ArrayList<String> locations = new ArrayList<>();
    Context context;

    public EquipmentCustomAdapter(Context context, ArrayList<EquipmentData> equipment) {
        this.arrayList = equipment;
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
        EquipmentData data = arrayList.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.equipment_list_item, parent, false);
            TextView location = convertView.findViewById(R.id.location);
            if(!locations.contains(data.location)){
                location.setText(data.location.toUpperCase());
                locations.add(data.location);
            }
            else
                location.setVisibility(View.GONE);
            TextView equipmentName = convertView.findViewById(R.id.equipmentItem);
            equipmentName.setText(data.name);

            equipmentName.setTag(data);
            equipmentName.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    equipmentName.setBackgroundResource(R.drawable.green_button_border);
                    equipmentName.setTextColor(Color.parseColor("#00CA8F"));

                }
            });


        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }
}
