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

public class HajjTracker extends AppCompatActivity {
    FirebaseDatabase database;
    FirebaseUser user;
    String userID;
    RequestQueue mQueue;
    DatabaseReference TimeZonereference,reference;
    CheckBox hajj1,hajj2,hajj3,hajj4,hajj5,hajj6,hajj7,hajj8,hajj9;
    HajjHelper HajjHelper;
    SwipeRefreshLayout swipeRefreshLayout;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hajj_tracker);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        Date date = new Date(); // Gregorian date
        Calendar cl=Calendar.getInstance();
        cl.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
        String currentDateandTime = sdf.format(new Date());
        HijrahDate islamyDate = HijrahChronology.INSTANCE.date(LocalDate.of(cl.get(Calendar.YEAR),cl.get(Calendar.MONTH)+1, cl.get(Calendar.DATE)));
        System.out.println(islamyDate.toString().contains("06-02"));
        //Toolbar Identifies it in the layout file
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Sets Title of the toolbar
        getSupportActionBar().setTitle("Hajj Tracker");
        // Add an a back button that will return to your desired activity based on
        // ParentActivityName in manifest
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        // The entry point of the Firebase Authentication SDK
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        TimeZonereference = database.getReference("TimeZone");
        reference = database.getInstance().getReference().child("Hajj");
        mQueue = Volley.newRequestQueue(this);
        swipeRefreshLayout = findViewById(R.id.refereshlayoutHajj);
        HajjHelper = new HajjHelper();
        hajj1 = findViewById(R.id.ihramCheck);
        hajj2 = findViewById(R.id.tawafandSayCheck);
        hajj3 = findViewById(R.id.minaCheck);
        hajj4 = findViewById(R.id.arafatCheck);
        hajj5 = findViewById(R.id.muzdalifahCheck);
        hajj6 = findViewById(R.id.toMinaCheck);
        hajj7 = findViewById(R.id.tawafIfadahCheck);
        hajj8 = findViewById(R.id.fromMakkahCheck);
        hajj9 = findViewById(R.id.tawafCheck);
        jsonParse();
        jsonParse2();
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

                                            }

                                            else if(_24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d4)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d3)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d2)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d1)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d)))){

                                            }

                                            else if(_24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d4)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d3)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d2)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d1)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d)))){

                                            }
                                            else if(_24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d4)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d3)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d2)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d1)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d)))){

                                            }
                                            else if(_24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d4)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d3)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d2)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).before(_24HourSDF.parse(_24HourSDF.format(d1)))&&
                                                    _24HourSDF.parse(_24HourSDF.format(currentdate)).after(_24HourSDF.parse(_24HourSDF.format(d)))){

                                            }
                                            else{
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
                                        String p1 = "Ihram";
                                        String p2 = "Performing the Welcome Tawaf and Sa'y";
                                        String p3 = "Going to Mina from Makkah";
                                        String p4 = "Going to Arafat from Mina";
                                        String p5 = "Going to Muzdalifah from Arafat";
                                        String p6 = "Proceeding to Mina from Muzdalifah";
                                        String p7 = "Tawaf al-Ifadha in the Holy City of Makkah";
                                        String p8 = "Returning to Mina from Makkah";
                                        String p9 = "Farewell Tawaf in the Holy City of Makkah";

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
                                        Calendar hajjCondition=Calendar.getInstance();
                                        hajjCondition.setTime(date);
                                        HijrahDate islamyDateCondition = HijrahChronology.INSTANCE.date(LocalDate.of(hajjCondition.get(Calendar.YEAR),hajjCondition.get(Calendar.MONTH)+1, hajjCondition.get(Calendar.DATE)));
                                        if(islamyDateCondition.toString().contains("12-08")||islamyDateCondition.toString().contains("12-09")
                                        ||islamyDateCondition.toString().contains("12-10")||islamyDateCondition.toString().contains("12-11")
                                        ||islamyDateCondition.toString().contains("12-12")||islamyDateCondition.toString().contains("12-12")
                                        ||islamyDateCondition.toString().contains("12-13")||islamyDateCondition.toString().contains("12-14")
                                                ||islamyDateCondition.toString().contains("12-15")||islamyDateCondition.toString().contains("12-16")
                                                ||islamyDateCondition.toString().contains("12-17"))
                                        {
                                            hajj1.setEnabled(true);
                                            hajj2.setEnabled(true);
                                            hajj3.setEnabled(true);
                                            hajj4.setEnabled(true);
                                            hajj5.setEnabled(true);
                                            hajj6.setEnabled(true);
                                            hajj7.setEnabled(true);
                                            hajj8.setEnabled(true);
                                            hajj9.setEnabled(true);

                                        }
                                        else {
                                            hajj1.setEnabled(false);
                                            hajj2.setEnabled(false);
                                            hajj3.setEnabled(false);
                                            hajj4.setEnabled(false);
                                            hajj5.setEnabled(false);
                                            hajj6.setEnabled(false);
                                            hajj7.setEnabled(false);
                                            hajj8.setEnabled(false);
                                            hajj9.setEnabled(false);
                                        }


                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                String hajjtrack1 = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("hajj1").getValue());
                                                String hajjtrack2 = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("hajj2").getValue());
                                                String hajjtrack3 = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("hajj3").getValue());
                                                String hajjtrack4 = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("hajj4").getValue());
                                                String hajjtrack5 = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("hajj5").getValue());
                                                String hajjtrack6 = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("hajj6").getValue());
                                                String hajjtrack7 = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("hajj7").getValue());
                                                String hajjtrack8 = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("hajj8").getValue());
                                                String hajjtrack9 = String.valueOf(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("hajj9").getValue());

                                                if (hajjtrack1.equals("Ihram")){
                                                    hajj1.setChecked(true);
                                                    HajjHelper.setHajj1(p1);
                                                }
                                                if (hajjtrack2.equals("Performing the Welcome Tawaf and Sa'y")){
                                                    hajj2.setChecked(true);
                                                    HajjHelper.setHajj2(p2);
                                                }
                                                if (hajjtrack3.equals("Going to Mina from Makkah")){
                                                    hajj3.setChecked(true);
                                                    HajjHelper.setHajj3(p3);
                                                }
                                                if (hajjtrack4.equals("Going to Arafat from Mina")){
                                                    hajj4.setChecked(true);
                                                    HajjHelper.setHajj4(p4);
                                                }
                                                if (hajjtrack5.equals("Going to Muzdalifah from Arafat")){
                                                    hajj5.setChecked(true);
                                                    HajjHelper.setHajj5(p5);
                                                }
                                                if (hajjtrack6.equals("Proceeding to Mina from Muzdalifah")){
                                                    hajj6.setChecked(true);
                                                    HajjHelper.setHajj6(p6);
                                                }
                                                if (hajjtrack7.equals("Tawaf al-Ifadha in the Holy City of Makkah")){
                                                    hajj7.setChecked(true);
                                                    HajjHelper.setHajj7(p7);
                                                }
                                                if (hajjtrack8.equals("Returning to Mina from Makkah")){
                                                    hajj8.setChecked(true);
                                                    HajjHelper.setHajj8(p8);
                                                }
                                                if (hajjtrack9.equals("Farewell Tawaf in the Holy City of Makkah")){
                                                    hajj9.setChecked(true);
                                                    HajjHelper.setHajj9(p9);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });

//                                       prayer1.setChecked(!=null);
                                        hajj1.setOnClickListener(new View.OnClickListener(){
                                            @Override
                                            public void onClick(View v) {

                                                if (hajj1.isChecked()) {
                                                    HajjHelper.setHajj1(p1);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }
                                                else if (!hajj1.isChecked()){
                                                    HajjHelper.setHajj1(null);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }

                                            }
                                        });
                                        hajj2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                //Dhur Prayer
                                                if (hajj2.isChecked()){
                                                    HajjHelper.setHajj2(p2);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }

//                else if (!prayer2.isChecked()){
//                    reference.child(String.valueOf(formattedDate)).child("prayer2").removeValue();
//                }
                                                else{
                                                    HajjHelper.setHajj2(null);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }
                                            }
                                        });
                                        hajj3.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                                //Asr Prayer
                                                if (hajj3.isChecked()){
                                                    HajjHelper.setHajj3(p3);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }
//                else if (!prayer3.isChecked()){
//                    reference.child(String.valueOf(formattedDate)).child("prayer3").removeValue();
//                }
                                                else{
                                                    HajjHelper.setHajj3(null);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }
                                            }
                                        });
                                        hajj4.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                //Maghrib Prayer
                                                if (hajj4.isChecked()){
                                                    HajjHelper.setHajj4(p4);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }
//                else if (!prayer4.isChecked()){
//                    reference.child(String.valueOf(formattedDate)).child("prayer4").removeValue();
//                }
                                                else{
                                                    HajjHelper.setHajj4(null);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }
                                            }
                                        });
                                        hajj5.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                //Eisha Prayer
                                                if (hajj5.isChecked()){
                                                    HajjHelper.setHajj5(p5);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }
//                else if (!prayer5.isChecked()){
//                    reference.child(String.valueOf(formattedDate)).child("prayer5").removeValue();
//
//                }
                                                else{
                                                    HajjHelper.setHajj5(null);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }
                                            }
                                        });
                                        hajj6.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                //Eisha Prayer
                                                if (hajj6.isChecked()){
                                                    HajjHelper.setHajj6(p6);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }
//                else if (!prayer5.isChecked()){
//                    reference.child(String.valueOf(formattedDate)).child("prayer5").removeValue();
//
//                }
                                                else{
                                                    HajjHelper.setHajj6(null);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }
                                            }
                                        });
                                        hajj7.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                //Eisha Prayer
                                                if (hajj7.isChecked()){
                                                    HajjHelper.setHajj7(p7);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }
//                else if (!prayer5.isChecked()){
//                    reference.child(String.valueOf(formattedDate)).child("prayer5").removeValue();
//
//                }
                                                else{
                                                    HajjHelper.setHajj7(null);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }
                                            }
                                        });
                                        hajj8.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                //Eisha Prayer
                                                if (hajj8.isChecked()){
                                                    HajjHelper.setHajj8(p8);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }
//                else if (!prayer5.isChecked()){
//                    reference.child(String.valueOf(formattedDate)).child("prayer5").removeValue();
//
//                }
                                                else{
                                                    HajjHelper.setHajj8(null);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }
                                            }
                                        });
                                        hajj9.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                //Eisha Prayer
                                                if (hajj9.isChecked()){
                                                    HajjHelper.setHajj9(p9);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
                                                }
//                else if (!prayer5.isChecked()){
//                    reference.child(String.valueOf(formattedDate)).child("prayer5").removeValue();
//
//                }
                                                else{
                                                    HajjHelper.setHajj9(null);
                                                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(HajjHelper);
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