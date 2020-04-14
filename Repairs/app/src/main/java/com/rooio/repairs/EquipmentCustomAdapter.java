package com.rooio.repairs;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import static android.view.View.GONE;

class EquipmentCustomAdapter implements ListAdapter {
    private ArrayList<EquipmentData> arrayList;
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<Button> buttons = new ArrayList<>();
    private Context context;
    private String equipmentID;

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
            ConstraintLayout editEquipmentDetails = ((Activity)context).findViewById(R.id.editEquipmentConstraint);
            ConstraintLayout equipmentAnalytics = ((Activity)context).findViewById(R.id.analyticsConstraint);

            // displaying locations
            TextView location = convertView.findViewById(R.id.location);
            if(!locations.contains(data.getLocation().toUpperCase())){
                location.setText(data.getLocation().toUpperCase());
                locations.add(data.getLocation().toUpperCase());
            }
            else
                location.setVisibility(GONE);

            // displaying equipment buttons
            Button equipment = convertView.findViewById(R.id.equipmentItem);
            equipment.setText(data.getName());

            //adding button to list of existing buttons
            buttons.add(equipment);
            equipment.setOnClickListener(v -> {
                //reset the page's UI
                ConstraintLayout addEquipmentLayout = ((Activity)context).findViewById(R.id.addEquipmentConstraint);
                addEquipmentLayout.setVisibility(GONE);

                editEquipmentDetails.setVisibility(GONE);

                Button addEquipmentButton = ((Activity)context).findViewById(R.id.addEquipmentButton);
                addEquipmentButton.setTextColor(context.getResources().getColor(R.color.darkGray));
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

                // displaying equipment details in edit equipment details fields
                editEquipmentDetails(data, editEquipmentDetails);

                // displaying equipment details
                equipmentDetails.setVisibility(View.VISIBLE);
                equipmentDetails(data, equipmentDetails);

                //saving the equipmentID
                equipmentID = data.getId();

                // displaying equipment analytics
                equipmentAnalytics.setVisibility(View.VISIBLE);
            });
        }
        return convertView;
    }

    // setting text fields with equipment information
    private void equipmentDetails(EquipmentData equipment, ConstraintLayout constraintLayout){
        TextView displayName = constraintLayout.findViewById(R.id.displayName);
        displayName.setText(equipment.getName());

        TextView serialNumber = constraintLayout.findViewById(R.id.serialNumber);
        if(equipment.getSerialNumber().isEmpty()) serialNumber.setText("--");
        else serialNumber.setText(equipment.getSerialNumber());

        TextView lastServiceDate = constraintLayout.findViewById(R.id.lastServiceDate);
        if(equipment.getLastServiceDate().equals("null")) lastServiceDate.setText("--");
        else lastServiceDate.setText(equipment.getLastServiceDate());

        TextView manufacturer = constraintLayout.findViewById(R.id.manufacturer);
        if(equipment.getManufacturer().isEmpty()) manufacturer.setText("--");
        else manufacturer.setText(equipment.getManufacturer());

        TextView location = constraintLayout.findViewById(R.id.location);
        if(equipment.getLocation().isEmpty()) location.setText("--");
        else location.setText(equipment.getLocation());

        TextView modelNum = constraintLayout.findViewById(R.id.modelNumber);
        if(equipment.getModelNumber().isEmpty()) modelNum.setText("--");
        else modelNum.setText(equipment.getModelNumber());

        TextView lastServiceBy = constraintLayout.findViewById(R.id.lastServiceBy);
        if(equipment.getLastServiceBy().isEmpty()) lastServiceBy.setText("--");
        else lastServiceBy.setText(equipment.getLastServiceBy());

        TextView equipmentType = constraintLayout.findViewById(R.id.equipmentType);
        equipmentType.setText(equipment.getType().toString());
    }

    private void editEquipmentDetails(EquipmentData equipment, ConstraintLayout constraintLayout) {
        TextInputEditText displayName = constraintLayout.findViewById(R.id.editDisplayName);
        displayName.setText(equipment.getName());

        TextInputEditText serialNumber = constraintLayout.findViewById(R.id.editSerialNumber);
        serialNumber.setText(equipment.getSerialNumber());

        TextInputEditText manufacturer = constraintLayout.findViewById(R.id.editManufacturer);
        manufacturer.setText(equipment.getManufacturer());

        TextInputEditText location = constraintLayout.findViewById(R.id.editLocation);
        location.setText(equipment.getLocation());

        TextInputEditText modelNum = constraintLayout.findViewById(R.id.editModelNumber);
        modelNum.setText(equipment.getModelNumber());

        Spinner equipmentType = constraintLayout.findViewById(R.id.editEquipmentType);
        equipmentType.setSelection(equipment.getType().getIntRepr() - 1);
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

    public String getEquipmentId() { return equipmentID; }
}
