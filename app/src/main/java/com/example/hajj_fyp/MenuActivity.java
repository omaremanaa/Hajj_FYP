package com.example.hajj_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    DatabaseReference databaseReference, locationreference;
    FusedLocationProviderClient fusedLocationProviderClient;
    String locationlat = "0";
    String locationlongt = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Piligrim's Mate");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("TimeZone");
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationreference = database.getInstance().getReference("TimeZone").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        locationreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    if (ActivityCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        getLocation();
                        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("timeformat").setValue("12h");
                        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("prayermethod").setValue("4");
                    } else {
                        ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                    }
                } else {
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        locationreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                locationlat = String.valueOf(snapshot.child("LocationLat").getValue());
                locationlongt = String.valueOf(snapshot.child("LocationLong").getValue());
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        System.out.println("This is the lat and long " + locationlat + " & "+  locationlongt);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        BottomNavigationView bottomnav = findViewById(R.id.bottom_nav);
        bottomnav.setItemIconTintList(null);
        bottomnav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        bottomnav.setSelectedItemId(R.id.nav_home);



    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    //Tool Bar for profile and faq as navigation bar cant handle more than 5 items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_emergencylocation:
                Intent intent = new Intent(this, EmergencyLocationActivity.class);
                this.startActivity(intent);
                break;
            case R.id.nav_reminder:
                Intent intent2 = new Intent(this, ReminderActivity.class);
                this.startActivity(intent2);
                break;
            case R.id.nav_signout:
                FirebaseAuth.getInstance().signOut();
                Intent intent3 = new Intent(this, MainActivity.class);
                Toast.makeText(getBaseContext(), "You have been logged out", Toast.LENGTH_LONG).show();
                this.startActivity(intent3);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    // BottomNavigationView is to change from home fragment(default) to any other fragment given in the nav bar
        private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_quran:
                        selectedFragment = new QuranFragment();
                        break;
                    case R.id.nav_kaaba:
                        selectedFragment = new GuidanceFragment();
                        break;
                    case R.id.nav_more:
                        selectedFragment = new MoreFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                return true;

            }
        };
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_setting:
                Intent settingintent = new Intent(this,SettingsActivity.class);
                startActivity(settingintent);
                break;
            case R.id.nav_editprofile:
                Intent profileintent = new Intent(this,ProfileActivity.class);
                startActivity(profileintent);
                break;
            case R.id.nav_emergencynumber:
                Intent emergencyintent = new Intent(this,EmergencyActivity.class);
                startActivity(emergencyintent);
                break;
            case R.id.nav_contactus:
                Intent contactusintent = new Intent(this, ContactUsActivity.class);
                startActivity(contactusintent);
                break;
            case R.id.nav_firstlocation:
                        String uri = "http://maps.google.com/maps?saddr="+locationlat+","+locationlongt+"&daddr=21.386740,39.901112";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                break;
            case R.id.nav_secondlocation:

                        String uri2 = "http://maps.google.com/maps?saddr="+locationlat+","+locationlongt+"&daddr=21.389050,39.937050";
                        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(uri2));
                        startActivity(intent2);
                break;
            case R.id.nav_thirdlocation:
                        String uri3 = "http://maps.google.com/maps?saddr="+locationlat+","+locationlongt+"&daddr=21.384170,39.876020";
                        Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(uri3));
                        startActivity(intent3);

                break;
            case R.id.nav_fourthlocation:
                        String uri4 = "http://maps.google.com/maps?saddr="+locationlat+","+locationlongt+"&daddr=21.359170,39.972120";
                        Intent intent4 = new Intent(Intent.ACTION_VIEW, Uri.parse(uri4));
                        startActivity(intent4);
                break;
            case R.id.nav_fifthlocation:
                        String uri5 = "http://maps.google.com/maps?saddr="+locationlat+","+locationlongt+"&daddr=21.421490,39.857680";
                        Intent intent5 = new Intent(Intent.ACTION_VIEW, Uri.parse(uri5));
                        startActivity(intent5);

                break;
            case R.id.nav_sixthlocation:
                        String uri6 = "http://maps.google.com/maps?saddr="+locationlat+","+locationlongt+"&daddr=21.42583,39.86538";
                        Intent intent6 = new Intent(Intent.ACTION_VIEW, Uri.parse(uri6));
                        startActivity(intent6);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
        super.onBackPressed();
    }}
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
                        Geocoder geocoder = new Geocoder(MenuActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        databaseReference.child("Location").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String city = addresses.get(0).getLocality();
                                String lat = String.valueOf( addresses.get(0).getLatitude());
                                String longt = String.valueOf(addresses.get(0).getLongitude());
                                String timezone = String.valueOf(addresses.get(0).getLongitude());
                                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Location").setValue(city);
                                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LocationLat").setValue(lat);
                                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LocationLong").setValue(longt);
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

}