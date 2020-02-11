package com.rooio.repairs;

import org.json.JSONException;
import org.json.JSONObject;

class EquipmentData {

    String name;
    String location;
    String manufacturer;
    String modelNumber;
    String serialNumber;
    String lastServiceDate;
    String lastServiceBy;
    String notes;
    String serviceLocation;
    Integer type;
    String serviceCategory;

    public EquipmentData(JSONObject jsonObject){
        try{
            this.name = jsonObject.getString("display_name");
            this.location = jsonObject.getString("location");
            this.manufacturer = jsonObject.getString("manufacturer");
            this.modelNumber = jsonObject.getString("model_number");
            this.serialNumber = jsonObject.getString("serial_number");
            this.lastServiceDate = jsonObject.getString("last_service_date");
            this.lastServiceBy = jsonObject.getString("last_service_by");
            this.notes = jsonObject.getString("notes");
            this.serviceLocation = jsonObject.getString("service_location");
            this.type = jsonObject.getInt("type");
            this.serviceCategory = jsonObject.getString("service_category");
        } catch (JSONException e) {
        }

    }

}
