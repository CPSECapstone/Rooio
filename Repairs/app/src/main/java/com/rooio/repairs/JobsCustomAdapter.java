package com.rooio.repairs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Build;
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
    private ArrayList<JobData> arrayList;
    private Context context;
    private ArrayList<String> statuses = new ArrayList<>();
    private TextView  timeText;

    public JobsCustomAdapter(Context context, ArrayList<JobData> jobs) {
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
        JobData data = arrayList.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.job_list_row, parent, false);
            TextView repairType = convertView.findViewById(R.id.repairType);
            TextView name = convertView.findViewById(R.id.name);
            TextView address = convertView.findViewById(R.id.address);
            ImageView image = convertView.findViewById(R.id.image);
            timeText = convertView.findViewById(R.id.timeText);
            Button jobsButton = convertView.findViewById(R.id.jobsButton);
            TextView status = convertView.findViewById(R.id.status);
            ConstraintLayout color = convertView.findViewById(R.id.color);

            try {
                JobType jobStatus = data.getStatus();
                String jobStatusText = jobStatus.toString();

                // set color for each job widget
                DrawableCompat.setTint(
                        DrawableCompat.wrap(color.getBackground()),
                        ContextCompat.getColor(context, jobStatus.getColor())
                );

                //Display text for each swim lane
                switch (jobStatus) {
                    case PENDING:
                        jobStatusText = "Awaiting Response";
                        break;

                    case SCHEDULED:
                        jobStatusText = timeConvert(data.getStatusTimeValue());
                        break;
                }


                if (!statuses.contains(jobStatusText)) {
                    status.setText(jobStatusText.toUpperCase());
                    statuses.add(jobStatusText);
                } else status.setVisibility(View.GONE);

                if (jobStatus == JobType.DECLINED || jobStatus == JobType.CANCELLED || jobStatus == JobType.COMPLETED) {
                    status.setVisibility(View.GONE);
                }

                //Sets the Equipment for Job: May be multiple equipment per job\
                //Only handling one equipment per job right now
                ArrayList<EquipmentData> equipmentObjList = data.getEquipmentList();
                if (equipmentObjList.size() != 0) {
                    EquipmentData equipmentObj = equipmentObjList.get(0);
                    String str1 = equipmentObj.getName() + " " + data.getServiceType().toString();
                    repairType.setText(str1);
                }
                //For a general equipment job request
                else {
                    String str = "General " + data.getStrRepr();
                    repairType.setText(str);
                }

                //Get Service Location
                JSONObject locationObj = data.getServiceLocation();
                address.setText(locationObj.getString("physical_address"));

                //Get Restaurant Name
                JSONObject internal_client = locationObj.getJSONObject("internal_client");
                name.setText(internal_client.getString("name"));

                if((data.getStatusTimeValue().isEmpty()) ){
                    timeText.setText((R.string.no_time));
                }
                else if(jobStatus == JobType.STARTED || jobStatus == JobType.PAUSED){
                    if (!(data.getEstimatedArrivalTime().isEmpty())){
                        @SuppressLint("SimpleDateFormat") Date date1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(convertToNewFormat(data.getEstimatedArrivalTime()));
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM d, y hh:mm a zzz");


                        assert date1 != null;
                        timeText.setText(dateFormatter.format(date1));
                    }
                }
                else if (jobStatus == JobType.DECLINED || jobStatus == JobType.COMPLETED || jobStatus == JobType.CANCELLED) {
                    if (!(data.getStatusTimeValue().isEmpty())){
                        @SuppressLint("SimpleDateFormat") Date date1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(convertToNewFormat(data.getStatusTimeValue()));
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM d, y hh:mm a zzz");


                        assert date1 != null;
                        timeText.setText(dateFormatter.format(date1));

                    }
                }
                else{
                    if (!(data.getStatusTimeValue().isEmpty())){
                        @SuppressLint("SimpleDateFormat") Date date1 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(convertToNewFormat(data.getStatusTimeValue()));
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM d, y hh:mm a zzz");


                        assert date1 != null;
                        timeText.setText(dateFormatter.format(date1));

                    }
                }

                //Checks if Image
                //If no Image then Move Text Over
                String imageVal = internal_client.getString("logo");
                if (imageVal.isEmpty() || imageVal.equals("null")) {
                    name.setTranslationX(-80f);
                    repairType.setTranslationX(-80f);
                    address.setTranslationX(-80f);
                    image.setVisibility(View.GONE);
                } else {
                    Picasso.with(context)
                            .load(internal_client.getString("logo")
                            )
                            .into(image);
                }




            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //JobId Holds the JobId. ** Displays on UI as a string for testing purposes**
            jobsButton.setOnClickListener(v -> {
                String jobId = data.getId();
                Intent intent = new Intent(context, JobDetails.class);
                intent.putExtra("id", jobId);
                context.startActivity(intent);
            });

        }
        return convertView;
    }

    private static String convertToNewFormat(String dateStr) throws ParseException {
        try{
            TimeZone utc = TimeZone.getTimeZone("UTC");
            SimpleDateFormat sourceFormat;
            sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sourceFormat.setTimeZone(utc);
            Date convertedDate = sourceFormat.parse(dateStr);
            assert convertedDate != null;
            return destFormat.format(convertedDate);


        }
        catch(ParseException e){
            TimeZone utc = TimeZone.getTimeZone("UTC");
            SimpleDateFormat sourceFormat;

            sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sourceFormat.setTimeZone(utc);
            Date convertedDate = sourceFormat.parse(dateStr);
            assert convertedDate != null;
            return destFormat.format(convertedDate);

        }
    }

    private String timeConvert(String dateStr) throws ParseException {
        String eta = convertToNewFormat(dateStr);

        //GET NOW DATE + TIME
        String now = now();

        String endOfToday = endOfToday();
        String endOfTomorrow = endOfTomorrow();
        String endOfWeek = endOfWeek();
        String endOfNextWeek = endOfNextWeek();
        String endOfThisMonth = endOfThisMonth();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (Objects.requireNonNull(sdf.parse(eta)).before(sdf.parse(now))) {
            timeText.setTextColor(Color.RED);

            return "PAST DUE";
        } else if ((Objects.requireNonNull(sdf.parse(eta)).after(sdf.parse(now))) && (Objects.requireNonNull(sdf.parse(eta)).before(sdf.parse(endOfToday)))) {
            return "TODAY";
        } else if ((Objects.requireNonNull(sdf.parse(eta)).after(sdf.parse(endOfToday))) && (Objects.requireNonNull(sdf.parse(eta)).before(sdf.parse(endOfTomorrow())))) {
            return "TOMORROW";
        } else if ((Objects.requireNonNull(sdf.parse(eta)).after(sdf.parse(endOfTomorrow))) && (Objects.requireNonNull(sdf.parse(eta)).before(sdf.parse(endOfWeek)))) {
            return "THIS WEEK";
        } else if ((Objects.requireNonNull(sdf.parse(eta)).after(sdf.parse(endOfWeek))) && (Objects.requireNonNull(sdf.parse(eta)).before(sdf.parse(endOfNextWeek)))) {
            return "NEXT WEEK";
        } else if ((Objects.requireNonNull(sdf.parse(eta)).after(sdf.parse(endOfNextWeek))) && (Objects.requireNonNull(sdf.parse(eta)).before(sdf.parse(endOfThisMonth)))) {
            return "LATER THIS MONTH";
        } else {
            return "FUTURE";
        }

    }

    private String endOfToday() {
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

    private String now() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        return df.format(c.getTime());
    }

    private String endOfTomorrow() {
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

    private String endOfWeek() {
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

    private String endOfNextWeek() {
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
        Date endOfNext = cal.getTime();
        return df.format(endOfNext);
    }

    private String endOfThisMonth() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endOfThis = cal.getTime();
        return df.format(endOfThis);
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
