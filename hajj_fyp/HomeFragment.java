package com.example.hajj_fyp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hajj_fyp.databinding.ActivityMainBinding;
import com.example.hajj_fyp.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.chrono.HijrahChronology;
import org.threeten.bp.chrono.HijrahDate;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class HomeFragment extends Fragment {
    FirebaseDatabase database;
    TextView fajrtime, dhurtime, asrtime, maghribtime, eishatime;
    FirebaseUser user;
    String userID;
    RequestQueue mQueue;
    DatabaseReference reference;
    PrayerTimeFormat prayerTimeFormat;
    TextView currentPrayer;
    SwipeRefreshLayout swipeRefreshLayout;
    int i = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable  ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
        String currentDateandTime = sdf.format(new Date());
        fajrtime = v.findViewById(R.id.fajrtiminig);
        dhurtime = v.findViewById(R.id.dhurtiminig);
        asrtime = v.findViewById(R.id.asrtiminig);
        maghribtime = v.findViewById(R.id.maghribtiminig);
        eishatime = v.findViewById(R.id.eishatiminig);
        TextView gregoriandate = v.findViewById(R.id.currentDategregorian);
        TextView hijridate = v.findViewById(R.id.CurrentDatehijri);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        Date date = new Date(); // Gregorian date
        Calendar cl=Calendar.getInstance();
        cl.setTime(date);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("TimeZone");
        HijrahDate islamyDate = HijrahChronology.INSTANCE.date(LocalDate.of(cl.get(Calendar.YEAR),cl.get(Calendar.MONTH)+1, cl.get(Calendar.DATE)));
        String str = ""+islamyDate;
        str = str.replace("Hijrah-umalqura AH","");
        gregoriandate.setText(currentDateandTime);
        hijridate.setText(str);
        prayerTimeFormat = new PrayerTimeFormat();
        PrayerHelper prayer = new PrayerHelper();
        currentPrayer = v.findViewById(R.id.currentPrayertime);
        TextView locationOnMenu = v.findViewById(R.id.currentLocationMenu);
        mQueue = Volley.newRequestQueue(getActivity());
        swipeRefreshLayout = v.findViewById(R.id.refereshlayout1);
        jsonParse();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String location = String.valueOf(snapshot.child(userID).child("Location").getValue());
                locationOnMenu.setText(location);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    i = (int) snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jsonParse();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        return v;
}
    private void jsonParse(){
        Date date = new Date();
        int day =  date.getDate();
        int month = date.getMonth()+1;
        int year = date.getYear()+1900;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    String cityfromdb = String.valueOf(snapshot.child(userID).child("Location").getValue());
                    String methodfromdb = String.valueOf(snapshot.child(userID).child("prayermethod").getValue());;
                    String url = "https://api.aladhan.com/v1/calendarByAddress?address=" + cityfromdb + "&method=" + methodfromdb + "&month=" + month + "&year=" + year;
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                                        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");


                                        String Fajr = response.getJSONArray("data").getJSONObject(day - 1).getJSONObject("timings").get("Fajr").toString();
                                        Fajr = Fajr.substring(0, Fajr.length() - 6);
                                        Date d = _24HourSDF.parse(Fajr);
                                        String Dhuhr = response.getJSONArray("data").getJSONObject(day - 1).getJSONObject("timings").get("Dhuhr").toString();
                                        Dhuhr = Dhuhr.substring(0, Dhuhr.length() - 6);
                                        Date d1 = _24HourSDF.parse(Dhuhr);
                                        String Asr = response.getJSONArray("data").getJSONObject(day - 1).getJSONObject("timings").get("Asr").toString();
                                        Asr = Asr.substring(0, Asr.length() - 6);
                                        Date d2 = _24HourSDF.parse(Asr);
                                        String Maghrib = response.getJSONArray("data").getJSONObject(day - 1).getJSONObject("timings").get("Maghrib").toString();
                                        Maghrib = Maghrib.substring(0, Maghrib.length() - 6);
                                        Date d3 = _24HourSDF.parse(Maghrib);
                                        String Isha = response.getJSONArray("data").getJSONObject(day - 1).getJSONObject("timings").get("Isha").toString();
                                        Isha = Isha.substring(0, Isha.length() - 6);
                                        Date d4 = _24HourSDF.parse(Isha);
                                        Date currentdate = new Date();
                                        String timming = (_24HourSDF.format(currentdate));
                                        try {
                                            if(_24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d4)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d3)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d2)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d1)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d)))){
                                            currentPrayer.setText("EISHA");
                                            }

                                        else if(_24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d4)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d3)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d2)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d1)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d)))){
                                            currentPrayer.setText("MAGHRIB");
                                        }

                                        else if(_24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d4)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d3)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d2)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d1)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d)))){
                                            currentPrayer.setText("ASR");
                                        }
                                        else if(_24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d4)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d3)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d2)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d1)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d)))){
                                            currentPrayer.setText("DHUR");
                                        }
                                        else if(_24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d4)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d3)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d2)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d1)))&&
                                                _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d)))){
                                            currentPrayer.setText("FAJR");
                                        }
                                        else{
                                                currentPrayer.setText("EISHA");
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        String AMPMfromDB = String.valueOf(snapshot.child(userID).child("timeformat").getValue());
                                        if (AMPMfromDB.equals("12h")){
                                            String fajr12Hour = _12HourSDF.format(d);
                                            String dhur12Hour = _12HourSDF.format(d1);
                                            String asr12hour = _12HourSDF.format(d2);
                                            String maghrib12Hour = _12HourSDF.format(d3);
                                            String isha12Hour = _12HourSDF.format(d4);
                                            fajrtime.setText(fajr12Hour);
                                            dhurtime.setText(dhur12Hour);
                                            asrtime.setText(asr12hour);
                                            maghribtime.setText(maghrib12Hour);
                                            eishatime.setText(isha12Hour);
                                        }
                                        else {
                                            String fajr12Hour = _12HourSDF.format(d);
                                            String dhur12Hour = _12HourSDF.format(d1);
                                            String asr12hour = _12HourSDF.format(d2);
                                            String maghrib12Hour = _12HourSDF.format(d3);
                                            String isha12Hour = _12HourSDF.format(d4);
                                            fajrtime.setText(Fajr);
                                            dhurtime.setText(Dhuhr);
                                            asrtime.setText(Asr);
                                            maghribtime.setText(Maghrib);
                                            eishatime.setText(Isha);
                                        }


                                    } catch (JSONException | ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            fajrtime.setText("Error");
                            dhurtime.setText("Error");
                            asrtime.setText("Error");
                            maghribtime.setText("Error");
                            eishatime.setText("Error");
                            Toast toast=Toast.makeText(getContext(),"Have you tried automatic location",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                    mQueue.add(request);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
