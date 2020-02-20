package com.rooio.repairs;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.ArrayList;

class EquipmentCustomAdapter implements ListAdapter {
    private ArrayList<EquipmentData> arrayList;
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<Button> buttons = new ArrayList<>();
    private Context context;

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

            TextView text = ((Activity)context).findViewById(R.id.equipmentPageNoSelectionText);
            ConstraintLayout equipmentDetails = ((Activity)context).findViewById(R.id.equipmentDetailsConstraint);
            ConstraintLayout equipmentAnalytics = ((Activity)context).findViewById(R.id.analyticsConstraint);

            // displaying locations
            TextView location = convertView.findViewById(R.id.location);
            if(!locations.contains(data.location.toUpperCase())){
                location.setText(data.location.toUpperCase());
                locations.add(data.location.toUpperCase());
            }
            else
                location.setVisibility(View.GONE);

            // displaying equipment buttons
            Button equipment = convertView.findViewById(R.id.equipmentItem);
            equipment.setText(data.name);

            //adding button to list of existing buttons
            buttons.add(equipment);
            equipment.setOnClickListener(v -> {
                //reset the page's UI
                ConstraintLayout addEquipmentLayout = ((Activity)context).findViewById(R.id.addEquipmentConstraint);
                addEquipmentLayout.setVisibility(View.GONE);

                Button addEquipmentButton = ((Activity)context).findViewById(R.id.addEquipmentButton);
                addEquipmentButton.setTextColor(context.getResources().getColor(R.color.GrayText));
                addEquipmentButton.setBackgroundResource(R.drawable.gray_button_border);

                // changing all other buttons back to gray
                for(Button b : buttons){
                    b.setBackgroundResource(R.drawable.dark_gray_button_border);
                    b.setTextColor(Color.parseColor("#747479"));
                }

                // set the color of the selected button to green
                equipment.setBackgroundResource(R.drawable.green_button_border);
                equipment.setTextColor(Color.parseColor("#00CA8F"));

                // getting rid of "select an equipment" text
                text.setVisibility(View.GONE);

                // displaying equipment details
                equipmentDetails.setVisibility(View.VISIBLE);
                equipmentDetails(data, equipmentDetails);

                // displaying equipment analytics
                equipmentAnalytics.setVisibility(View.VISIBLE);
            });
        }
        return convertView;
    }

    // setting text fields with equipment information
    private void equipmentDetails(EquipmentData equipment, ConstraintLayout constraintLayout){
        TextView displayName = constraintLayout.findViewById(R.id.displayName);
        if(equipment.name.isEmpty())
            equipment.name = "--";
        displayName.setText(equipment.name);

        TextView serialNumber = constraintLayout.findViewById(R.id.serialNumber);
        if(equipment.serialNumber.isEmpty())
            equipment.serialNumber = "--";
        serialNumber.setText(equipment.serialNumber);

        TextView lastServiceDate = constraintLayout.findViewById(R.id.lastServiceDate);
        Log.i("try", "ser " + equipment.lastServiceDate);
        if(equipment.lastServiceDate.equals("null"))
            equipment.lastServiceDate = "--";
        lastServiceDate.setText(equipment.lastServiceDate);

        TextView manufacturer = constraintLayout.findViewById(R.id.manufacturer);
        if(equipment.manufacturer.isEmpty())
            equipment.manufacturer = "--";
        manufacturer.setText(equipment.manufacturer);

        TextView location = constraintLayout.findViewById(R.id.location);
        if(equipment.location.isEmpty())
            equipment.location = "--";
        location.setText(equipment.location);

        TextView modelNum = constraintLayout.findViewById(R.id.modelNumber);
        if(equipment.modelNumber.isEmpty())
            equipment.modelNumber = "--";
        modelNum.setText(equipment.modelNumber);

        TextView lastServiceBy = constraintLayout.findViewById(R.id.lastServiceBy);
        if(equipment.lastServiceBy.isEmpty())
            equipment.lastServiceBy = "--";
        lastServiceBy.setText(equipment.lastServiceBy);
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

    public ArrayList<Button> getButtons() {
        return buttons;
    }
}
