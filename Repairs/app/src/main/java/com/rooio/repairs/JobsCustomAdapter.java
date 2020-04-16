package com.rooio.repairs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import java.util.Objects;
import java.util.TimeZone;

class JobsCustomAdapter implements ListAdapter {
    private ArrayList<JSONObject> arrayList;
    private Context context;
    private ArrayList<String> statuses = new ArrayList<>();

    public JobsCustomAdapter(Context context, ArrayList<JSONObject> jobs) {
        this.arrayList = jobs;
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
                int status_enum = data.getInt("status");
                String status_value = null;

                //Display Statuses for each swimlane
                switch(status_enum) {
                    //Declined Swimlane
                    case 1:
                        status_value = "Declined";
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(color.getBackground()),
                                ContextCompat.getColor(context, R.color.lightGray)
                        );
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
                    case 3:
                        status_value = "Archived";

                        DrawableCompat.setTint(
                                DrawableCompat.wrap(color.getBackground()),
                                ContextCompat.getColor(context, R.color.lightGray)
                        );
                        break;
                        //Cancelled Swimlane Status
                    case 4:
                        status_value = "Cancelled";
                        DrawableCompat.setTint(
                                DrawableCompat.wrap(color.getBackground()),
                                ContextCompat.getColor(context, R.color.lightGray)
                        );
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
                                ContextCompat.getColor(context, R.color.Yellow)
                        );

                        break;
                        //Pending Swimlane Status
                    case 0:
                        status_value = "Awaiting Response";

                        DrawableCompat.setTint(
                                DrawableCompat.wrap(color.getBackground()),
                                ContextCompat.getColor(context, R.color.Purple)
                        );
                        break;
                }

                //Sets the Equipment for Job: May be multiple equipment per job
                JSONArray equipmentObjList = data.getJSONArray("equipment");

                JSONObject equipmentObj = equipmentObjList.getJSONObject(0);
                String category = equipmentObj.getString("service_category");

                    switch(category) {
                        case "4":
//                            repairType.setText("General Appliance");
                            //categories.add("General Appliance");
                            repairType.setText(context.getString(R.string.generalAppliance));

                            break;
                        case "1":
//                            repairType.setText("HVAC");
                            repairType.setText(context.getString(R.string.hvac));
                            break;
                        case "2":
//                            repairType.setText("Lighting and Electrical");
                            repairType.setText(context.getString(R.string.lightingAndElectrical));
                            break;
                        case "3":
//                            repairType.setText("Plumbing");
                            repairType.setText(context.getString(R.string.plumbing));
                            break;
                    }


                //Get Service Location
                JSONObject locationObj = data.getJSONObject("service_location");
                address.setText(locationObj.getString("physical_address"));

                //Get Restraunt Name
                JSONObject internal_client = locationObj.getJSONObject("internal_client");
                name.setText(internal_client.getString("name"));


                //Change format of date
                if (!(data.isNull("status_time_value") )){
                    @SuppressLint("SimpleDateFormat") Date date1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(convertToNewFormat(data.getString("status_time_value")));
                    assert date1 != null;
                    timeImage.setText(date1.toString());

                }

                Picasso.with(context)
                        .load(internal_client.getString("logo")
                        )
                        .into(image);



                if(!statuses.contains(status_value)){
                    assert status_value != null;
                    status.setText(status_value.toUpperCase());
                    statuses.add(status_value);
                }
                else
                    status.setVisibility(View.INVISIBLE);

                }


            catch (JSONException e) {
                Log.d("exception", e.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //JobId Holds the JobId. ** Displays on UI as a string for testing purposes**
            jobsButton.setOnClickListener(v -> {
                try {
                    String jobId = data.getString("id");

                    Intent intent = new Intent(context, JobDetails.class);
                    intent.putExtra("id", jobId);

                    context.startActivity(intent);

                }
                catch (JSONException e) {
                    Log.d("exception", e.toString());
                }

            });

        }
        return convertView;
    }

    private static String convertToNewFormat(String dateStr) throws ParseException {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sourceFormat.setTimeZone(utc);
        Date convertedDate = sourceFormat.parse(dateStr);
        assert convertedDate != null;
        return destFormat.format(convertedDate);
    }

    private   String timeConvert(String dateStr) throws ParseException {
        String eta = convertToNewFormat(dateStr);

        //GET NOW DATE + TIME
        String now = now();

        String endOfToday = endOfToday();
        String endOfTomorrow = endOfTomorrow();
        String endofWeek = endOfWeek();
        String endOfNextWeek = endOfNextWeek();
        String endOfThisMonth = endOfThisMonth();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (Objects.requireNonNull(sdf.parse(eta)).before(sdf.parse(now))) {
            return "PAST DUE";
        }
        else if ((Objects.requireNonNull(sdf.parse(eta)).after(sdf.parse(now))) && (Objects.requireNonNull(sdf.parse(eta)).before(sdf.parse(endOfToday))))  {
            return "TODAY";
        }
        else if ((Objects.requireNonNull(sdf.parse(eta)).after(sdf.parse(endOfToday))) && (Objects.requireNonNull(sdf.parse(eta)).before(sdf.parse(endOfTomorrow()))))  {
            return "TOMORROW";
        }
        else if ((Objects.requireNonNull(sdf.parse(eta)).after(sdf.parse(endOfTomorrow))) && (Objects.requireNonNull(sdf.parse(eta)).before(sdf.parse(endofWeek))))  {
            return "THIS WEEK";
        }
        else if ((Objects.requireNonNull(sdf.parse(eta)).after(sdf.parse(endofWeek))) && (Objects.requireNonNull(sdf.parse(eta)).before(sdf.parse(endOfNextWeek))))  {
            return "NEXT WEEK";
        }
        else if ((Objects.requireNonNull(sdf.parse(eta)).after(sdf.parse(endOfNextWeek))) && (Objects.requireNonNull(sdf.parse(eta)).before(sdf.parse(endOfThisMonth))))  {
            return "LATER THIS MONTH";
        }
        else{
            return "FUTURE";
        }

    }

    private String endOfToday(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        return df.format(c.getTime());
    }

    private String endOfTomorrow(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    private String endOfWeek(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            cal.add(Calendar.DATE, 1);
        }
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endOfWeek = cal.getTime();
        return df.format(endOfWeek);
    }

    private String endOfNextWeek(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        Date endofNext = cal.getTime();
        return df.format(endofNext);
    }
    private String endOfThisMonth(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endofThis = cal.getTime();
        return df.format(endofThis);
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
