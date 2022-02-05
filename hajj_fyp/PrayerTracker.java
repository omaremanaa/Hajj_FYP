package com.example.hajj_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.chrono.HijrahChronology;
import org.threeten.bp.chrono.HijrahDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PrayerTracker extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseUser user;
    String userID;
    RequestQueue mQueue;
    DatabaseReference TimeZonereference,reference;
    CheckBox prayer1,prayer2,prayer3,prayer4,prayer5;
    Button prayertimebutton;
    PrayerHelper prayer;
    PrayerTimeFormat prayerTimeFormat;
    TextView currentPrayer;
    SwipeRefreshLayout swipeRefreshLayout;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_tracker);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        Date date = new Date(); // Gregorian date
        Calendar cl=Calendar.getInstance();
        cl.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
        String currentDateandTime = sdf.format(new Date());
        TextView gregoriandate = findViewById(R.id.currentDategregorian);
        TextView hijridate = findViewById(R.id.CurrentDatehijri);
        gregoriandate.setText(currentDateandTime);
        HijrahDate islamyDate = HijrahChronology.INSTANCE.date(LocalDate.of(cl.get(Calendar.YEAR),cl.get(Calendar.MONTH)+1, cl.get(Calendar.DATE)));
        String str = ""+islamyDate;
        str = str.replace("Hijrah-umalqura AH","");
        hijridate.setText(str);
        //Toolbar Identifies it in the layout file
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Sets Title of the toolbar
        getSupportActionBar().setTitle("Prayer Tracker");
        // Add an a back button that will return to your desired activity based on
        // ParentActivityName in manifest
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        // The entry point of the Firebase Authentication SDK
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        TimeZonereference = database.getReference("TimeZone");
        reference = database.getInstance().getReference().child("Prayer");
        currentPrayer = findViewById(R.id.currentPrayertime);
        mQueue = Volley.newRequestQueue(this);
        swipeRefreshLayout = findViewById(R.id.refereshlayout1);
        prayer = new PrayerHelper();
        prayer1 = findViewById(R.id.fajrtime);
        prayer2 = findViewById(R.id.dhurtime);
        prayer3 = findViewById(R.id.asrtime);
        prayer4 = findViewById(R.id.maghribtime);
        prayer5 = findViewById(R.id.eishatime);
        TextView locationOnMenu = findViewById(R.id.currentLocationMenu);
        String p1 = "Fajr";
        String p2 = "Dhur";
        String p3 = "Asr";
        String p4 = "Maghrib";
        String p5 = "Eisha";
        jsonParse();
        jsonParse2();
        TimeZonereference.addValueEventListener(new ValueEventListener() {
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
        TimeZonereference.addValueEventListener(new ValueEventListener() {
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
                jsonParse2();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private void jsonParse() {
        Date date = new Date();
        int day =  date.getDate();
        int month = date.getMonth()+1;
        int year = date.getYear()+1900;
        TimeZonereference.addListenerForSingleValueEvent(new ValueEventListener() {
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


                                    } catch (JSONException | ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
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
    private void jsonParse2(){
        Date date = new Date();
        int day =  date.getDate();
        int month = date.getMonth()+1;
        int year = date.getYear()+1900;

        TimeZonereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot!=null){
                    String cityfromdb = String.valueOf(snapshot.child(userID).child("Location").getValue());
                    String methodfromdb = String.valueOf(snapshot.child(userID).child("prayermethod").getValue());
                    String url = "https://api.aladhan.com/v1/calendarByAddress?address="+cityfromdb+"&method="+methodfromdb+"&month="+month+"&year="+year;
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String p1 = "Fajr";
                                        String p2 = "Dhur";
                                        String p3 = "Asr";
                                        String p4 = "Maghrib";
                                        String p5 = "Eisha";

                                        Date c = Calendar.getInstance().getTime();
                                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                                        String formattedDate = df.format(c);
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
                                        try {
                                            if(_24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d))))
                                            {
                                                prayer1.setEnabled(false);
                                            }
                                            else if (_24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d)))){
                                                prayer1.setEnabled(true);
                                            }
                                            if(_24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d1))))
                                            {
                                                prayer2.setEnabled(false);
                                            }
                                            else if (_24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d1)))){
                                                prayer2.setEnabled(true);
                                            }
                                            if(_24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d2))))
                                            {
                                                prayer3.setEnabled(false);
                                            }
                                            else if (_24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d2)))){
                                                prayer3.setEnabled(true);
                                            }
                                            if(_24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d3))))
                                            {
                                                prayer4.setEnabled(false);
                                            }
                                            else if (_24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d3)))){
                                                prayer4.setEnabled(true);
                                            }

                                            if(_24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d4))))
                                            {
                                                prayer5.setEnabled(false);
                                            }else if (_24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d4)))){
                                                prayer5.setEnabled(true);
                                            }



                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                String methodfromdb = String.valueOf(snapshot.child(userID).child("prayermethod").getValue());
                                                String prayerfajr = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).child("prayer1").getValue());
                                                String prayerdhur = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).child("prayer2").getValue());
                                                String prayerasr = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).child("prayer3").getValue());
                                                String prayermaghrib = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).child("prayer4").getValue());
                                                String prayereisha = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).child("prayer5").getValue());


                                                prayer1.setChecked(prayerfajr.equals("Fajr"));
                                                prayer2.setChecked(prayerdhur.equals("Dhur"));
                                                prayer3.setChecked(prayerasr.equals("Asr"));
                                                prayer4.setChecked(prayermaghrib.equals("Maghrib"));
                                                prayer5.setChecked(prayereisha.equals("Eisha"));
                                                String prayerfajr1 = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).child("prayer1").getValue());
                                                String prayerdhur1 = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).child("prayer2").getValue());
                                                String prayerasr1 = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).child("prayer3").getValue());
                                                String prayermaghrib1 = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).child("prayer4").getValue());
                                                String prayereisha1 = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).child("prayer5").getValue());
                                                if (prayerfajr1.equals("Fajr")){
                                                    prayer1.setChecked(true);
                                                    prayer.setPrayer1(p1);
                                                }
                                                if (prayerdhur1.equals("Dhur")){
                                                    prayer2.setChecked(true);
                                                    prayer.setPrayer2(p2);
                                                }
                                                if (prayerasr1.equals("Asr")){
                                                    prayer3.setChecked(true);
                                                    prayer.setPrayer3(p3);
                                                }
                                                if (prayermaghrib1.equals("Maghrib")){
                                                    prayer4.setChecked(true);
                                                    prayer.setPrayer4(p4);
                                                }
                                                if (prayereisha1.equals("Eisha")){
                                                    prayer5.setChecked(true);
                                                    prayer.setPrayer5(p5);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });

//                                       prayer1.setChecked(!=null);
                                        prayer1.setOnClickListener(new View.OnClickListener(){
                                            @Override
                                            public void onClick(View v) {

                                                if (prayer1.isChecked()) {
                                                    prayer.setPrayer1(p1);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).setValue(prayer);
                                                }
                                                else if (!prayer1.isChecked()){
                                                    prayer.setPrayer1(null);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).setValue(prayer);


                                                }

                                            }
                                        });
                                        prayer2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                //Dhur Prayer
                                                if (prayer2.isChecked()){
                                                    prayer.setPrayer2(p2);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).setValue(prayer);
                                                }

//                else if (!prayer2.isChecked()){
//                    reference.child(String.valueOf(formattedDate)).child("prayer2").removeValue();
//                }
                                                else{
                                                    prayer.setPrayer2(null);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).setValue(prayer);
                                                }
                                            }
                                        });
                                        prayer3.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                                //Asr Prayer
                                                if (prayer3.isChecked()){
                                                    prayer.setPrayer3(p3);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).setValue(prayer);
                                                }
//                else if (!prayer3.isChecked()){
//                    reference.child(String.valueOf(formattedDate)).child("prayer3").removeValue();
//                }
                                                else{
                                                    prayer.setPrayer3(null);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).setValue(prayer);
                                                }
                                            }
                                        });
                                        prayer4.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                //Maghrib Prayer
                                                if (prayer4.isChecked()){
                                                    prayer.setPrayer4(p4);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).setValue(prayer);
                                                }
//                else if (!prayer4.isChecked()){
//                    reference.child(String.valueOf(formattedDate)).child("prayer4").removeValue();
//                }
                                                else{
                                                    prayer.setPrayer4(null);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).setValue(prayer);
                                                }
                                            }
                                        });
                                        prayer5.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                //Eisha Prayer
                                                if (prayer5.isChecked()){
                                                    prayer.setPrayer5(p5);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).setValue(prayer);
                                                }
//                else if (!prayer5.isChecked()){
//                    reference.child(String.valueOf(formattedDate)).child("prayer5").removeValue();
//
//                }
                                                else{
                                                    prayer.setPrayer5(null);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(String.valueOf(formattedDate)).setValue(prayer);
                                                }
                                            }
                                        });
                                    } catch (JSONException | ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
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