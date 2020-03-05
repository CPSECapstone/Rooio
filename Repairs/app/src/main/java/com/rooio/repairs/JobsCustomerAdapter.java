package com.rooio.repairs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

class JobsCustomerAdapter implements ListAdapter {
    private ArrayList<JSONObject> arrayList;
    private Context context;
    String result_categories = "";
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject data = arrayList.get(position);
        if(convertView == null) {
            //Set
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.job_list_row, parent, false);
            TextView repairType = convertView.findViewById(R.id.repairType);
            TextView name = convertView.findViewById(R.id.name);
            TextView address = convertView.findViewById(R.id.address);
            ImageView image = convertView.findViewById(R.id.image);
            TextView timeImage = convertView.findViewById(R.id.timeImage);
            Button jobsButton = convertView.findViewById(R.id.jobsButton);

            TextView status = convertView.findViewById(R.id.status);
            ConstraintLayout color = convertView.findViewById(R.id.color);
            try {
                Integer status_enum = data.getInt("status");
                Integer jobId = data.getInt("id");

                String status_value = null;

                //Display Statuses for each swimlane
                switch(status_enum) {
                    //Declined Swimlane
                    case 1:
                        status_value = "Declined";

                        break;
                        //Scheduled swimlane uses time as status
                    case 2:
                        //Time Based Statuses
                        status_value = timeConvert(data.getString("status_time_value"));
                        
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(color.getBackground()),
                                ContextCompat.getColor(context, R.color.Blue)
                        );
                        break;
                        //Archived Swimlane Status
                    case 3:
                        status_value = "Archived";
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(color.getBackground()),
                                ContextCompat.getColor(context, R.color.lightGray)
                        );
                        break;
                        //Cancelled Swimlane Status
                    case 4:
                        status_value = "Canceled";

                        break;
                    case 5:
                        //In Progress Swimlane Status
                        status_value = "Started";
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(color.getBackground()),
                                ContextCompat.getColor(context, R.color.colorPrimary)
                        );
                        break;
                        //In Progress Swimlane Status
                    case 6:
                        status_value = "Paused";
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(color.getBackground()),
                                ContextCompat.getColor(context, R.color.colorPrimary)
                        );

                        break;
                        //Pending Swimlane Status
                    case 0:
                        status_value = "Pending";
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(color.getBackground()),
                                ContextCompat.getColor(context, R.color.Purple)
                        );
                        break;
                }

                //Sets the Equipment for Job: May be multiple equipment per job
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
                for (int i = 0; i < categories.size(); i++) {
                    result_categories = result_categories + categories.get(i);
                }
                repairType.setText(result_categories);

                //Get Service Location
                JSONObject locationObj = data.getJSONObject("service_location");
                address.setText(locationObj.getString("physical_address"));

                //Get Restraunt Name
                JSONObject internal_client = locationObj.getJSONObject("internal_client");
                name.setText(internal_client.getString("name"));


                //Change format of date
                Date date1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(convertToNewFormat(data.getString("status_time_value")));
                timeImage.setText(date1.toString());

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
                Log.d("exception", e.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //JobId Holds the JobId. ** Displays on UI as a string for testing purposes**
            jobsButton.setOnClickListener(v -> {
                try {
                    Integer jobId = data.getInt("id");
                    CharSequence text = jobId.toString();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
                catch (JSONException e) {
                    Log.d("exception", e.toString());
                }

            });

        }
        return convertView;
    }

    public static String convertToNewFormat(String dateStr) throws ParseException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sourceFormat.setTimeZone(utc);
        Date convertedDate = sourceFormat.parse(dateStr);
        return destFormat.format(convertedDate);
    }

    public  String timeConvert(String dateStr) throws ParseException {
        String eta = convertToNewFormat(dateStr);

        //GET NOW DATE + TIME
        String now = now();

        String endOfToday = endOfToday();
        String endOfTomorrow = endofTomorrow();
        String endofWeek = endofWeek();
        String endofNextWeek = endofNextWeek();
        String endofThisMonth = endofThisMonth();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (sdf.parse(eta).before(sdf.parse(now))) {
            return "PAST DUE";
        }
        else if ((sdf.parse(eta).after(sdf.parse(now))) && (sdf.parse(eta).before(sdf.parse(endOfToday))))  {
            return "TODAY";
        }
        else if ((sdf.parse(eta).after(sdf.parse(endOfToday))) && (sdf.parse(eta).before(sdf.parse(endofTomorrow()))))  {
            return "TOMORROW";
        }
        else if ((sdf.parse(eta).after(sdf.parse(endOfTomorrow))) && (sdf.parse(eta).before(sdf.parse(endofWeek))))  {
            return "THIS WEEK";
        }
        else if ((sdf.parse(eta).after(sdf.parse(endofWeek))) && (sdf.parse(eta).before(sdf.parse(endofNextWeek))))  {
            return "NEXT WEEK";
        }
        else if ((sdf.parse(eta).after(sdf.parse(endofNextWeek))) && (sdf.parse(eta).before(sdf.parse(endofThisMonth))))  {
            return "LATER THIS MONTH";
        }
        else{
            return "FUTURE";
        }

    }

    private String endOfToday(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endOfToday = cal.getTime();
        return df.format(endOfToday);
    }

    private String now(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        return df.format(c.getTime());
    }

    private String endofTomorrow(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);// <--
        Date endOfTomorrow = cal.getTime();
        return df.format(endOfTomorrow);
    }

    private String endofWeek(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            cal.add(Calendar.DATE, 1);
        }
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endOfTomorrow = cal.getTime();
        return df.format(endOfTomorrow);
    }

    private String endofNextWeek(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            cal.add(Calendar.DATE, 1);
        }
        cal.add(Calendar.DAY_OF_YEAR, 7);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endOfTomorrow = cal.getTime();
        return df.format(endOfTomorrow);
    }
    private String endofThisMonth(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endOfTomorrow = cal.getTime();
        return df.format(endOfTomorrow);
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
