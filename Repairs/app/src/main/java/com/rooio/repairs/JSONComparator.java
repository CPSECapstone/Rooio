package com.rooio.repairs;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Comparator;

class JSONComparator implements Comparator<JSONObject> {

    @Override
    public int compare(JSONObject o1, JSONObject o2) {

        try {

            if (o1.isNull("status_time_value") && o2.isNull("status_time_value")){
                return -1;
            }
            else if(o1.isNull("status_time_value")){
                return -1;
            }
            else if(o2.isNull("status_time_value")){
                return -1;
            }
            else{
                String v1 =  o1.getString("status_time_value");
                String v3 = o2.getString("status_time_value");
                return v1.compareTo(v3);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}