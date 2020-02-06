package com.rooio.repairs;

import org.json.JSONException;
import org.json.JSONObject;

class EquipmentData {

    String name;
    String location;

    public EquipmentData(JSONObject jsonObject){
        try{
            this.name = jsonObject.getString("display_name");
            this.location = jsonObject.getString("location");
        } catch (JSONException e) {
        }

    }

}
