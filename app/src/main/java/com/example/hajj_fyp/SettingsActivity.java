package com.example.hajj_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button savesettings;
    Spinner timeformat, prayermethod;
    ImageButton AutoLocation;
    DatabaseReference databaseReference;
    FusedLocationProviderClient fusedLocationProviderClient;
    String timeitem, prayermethoditem;
    PrayerTimeFormat prayerTimeFormat;
    EditText locationmanu;
    String[] timing = {"12 Hour", "24 Hour"};
    String[] prayertiming = {"Default (Umm Al-Qura University, Makkah)", "Shia Ithna-Ansari", "University of Islamic Sciences, Karachi", "Islamic Society of North America"
            , "Muslim World League", "Egyptian General Authority of Survey", "Institute of Geophysics, University of Tehran",
            "Gulf Region", "Kuwait", "Qatar", "Majlis Ugama Islam Singapura, Singapore", "Union Organization islamic de France", "Diyanet İşleri Başkanlığı, Turkey",
            "Spiritual Administration of Muslims of Russia"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Toolbar Identifies it in the layout file
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Sets Title of the toolbar
        getSupportActionBar().setTitle("Settings");
        // Add an a back button that will return to your desired activity based on
        // ParentActivityName in manifest
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        savesettings = findViewById(R.id.changepassword);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        AutoLocation = findViewById(R.id.btlocation);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("TimeZone");
        timeformat = findViewById(R.id.prayertimeformat);
        timeformat.setOnItemSelectedListener(this);
        prayermethod = findViewById(R.id.prayermethod);
        prayermethod.setOnItemSelectedListener(this);
        locationmanu = findViewById(R.id.locationmanual);
        prayerTimeFormat = new PrayerTimeFormat();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, timing);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timeformat.setAdapter(arrayAdapter);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, prayertiming);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prayermethod.setAdapter(arrayAdapter2);
        AutoLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
        savesettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveValue(timeitem, prayermethoditem);

                locationmanu = findViewById(R.id.locationmanual);
                String located = locationmanu.getText().toString();
                GeoLocation geoLocation = new GeoLocation();
                geoLocation.getAddress(located,getApplicationContext(),new GeoHandler());


                //Validate for Location
                if (located.isEmpty()||located=="") {
                    locationmanu.setError("Please Enter a Location");
                    locationmanu.requestFocus();
                    return;
                }
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Location").setValue(located);
                startActivity(new Intent(SettingsActivity.this, MenuActivity.class));

            }

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        timeitem = timeformat.getSelectedItem().toString();
        prayermethoditem = prayermethod.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    void SaveValue(String timeitem, String prayermethoditem) {

        switch (timeitem) {
            case "12 Hour":
                prayerTimeFormat.setTimeformat("12h");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
            case "24 Hour":
                prayerTimeFormat.setTimeformat("24h");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
            default:
                prayerTimeFormat.setTimeformat("12h");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
        }

        switch (prayermethoditem) {
            case "Default (Umm Al-Qura University, Makkah)":
                prayerTimeFormat.setPrayermethod("4");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
            case "Shia Ithna-Ansari":
                prayerTimeFormat.setPrayermethod("0");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
            case "University of Islamic Sciences, Karachi":
                prayerTimeFormat.setPrayermethod("1");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
            case "Islamic Society of North America":
                prayerTimeFormat.setPrayermethod("2");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
            case "Muslim World League":
                prayerTimeFormat.setPrayermethod("3");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
            case "Egyptian General Authority of Survey":
                prayerTimeFormat.setPrayermethod("5");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
            case "Institute of Geophysics, University of Tehran":
                prayerTimeFormat.setPrayermethod("7");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
            case "Gulf Region":
                prayerTimeFormat.setPrayermethod("8");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
            case "Kuwait":
                prayerTimeFormat.setPrayermethod("9");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
            case "Qatar":
                prayerTimeFormat.setPrayermethod("10");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
            case "Majlis Ugama Islam Singapura, Singapore":
                prayerTimeFormat.setPrayermethod("11");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
            case "Union Organization islamic de France":
                prayerTimeFormat.setPrayermethod("12");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
            case "Diyanet İşleri Başkanlığı, Turkey":
                prayerTimeFormat.setPrayermethod("13");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
            case "Spiritual Administration of Muslims of Russia":
                prayerTimeFormat.setPrayermethod("14");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;

            default:
                prayerTimeFormat.setPrayermethod("4");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(prayerTimeFormat);
                break;
        }
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {

                    try {
                        Geocoder geocoder = new Geocoder(SettingsActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        databaseReference.child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String city = addresses.get(0).getLocality();
                                String lat = String.valueOf( addresses.get(0).getLatitude());
                                String longt = String.valueOf(addresses.get(0).getLongitude());
                                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Location").setValue(city);
                                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LocationLat").setValue(lat);
                                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LocationLong").setValue(longt);
                                locationmanu.setText(city);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private class GeoHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String lat;
            String latitudee;
            String longitudee;
            String [] longandlat;
            switch (msg.what){
                case 1:

                    Bundle bundle = msg.getData();
                    lat =bundle.getString("Location");
                    longandlat = lat.split("&");
                    HashMap hashMap = new HashMap();
                    hashMap.put("LocationLat", longandlat[0]);
                    hashMap.put("LocationLong", longandlat[1]);
                    databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                        }
                    });

                    break;
                default:
                    latitudee = null;
                    longitudee = null;
            }
        }
    }
}