package com.example.hajj_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {
    EditText NameofUser;
    DatePicker AgeOfUser;
    TextView EmailofUser;
    Button submit;
    DatabaseReference reference;
    FirebaseUser user;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Information");
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        NameofUser = findViewById(R.id.nameUpdate);
        AgeOfUser = findViewById(R.id.AgeUpdate);
        EmailofUser = findViewById(R.id.emailDisplay);
        submit = findViewById(R.id.ButtonSubmit);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                int ageYear = AgeOfUser.getYear();
                int usersAge = currentYear - ageYear;
                String agehash = String.valueOf(usersAge);
                String namehash = NameofUser.getText().toString().trim();
                //Validate for Name
                if (namehash.isEmpty()) {
                    NameofUser.setError("Please Enter Your Name");
                    NameofUser.requestFocus();
                    return;
                }
                //Validate Age
                if (!validateAge()){
                    return;
                }

                HashMap hashMap = new HashMap();
                hashMap.put("age", agehash);
                hashMap.put("name", namehash);
                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(EditProfileActivity.this, "User's Data is Updated.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelper userHelper = snapshot.getValue(UserHelper.class);
                String email = userHelper.email;
                EmailofUser.setText("Email: " + email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Something wrong happened", Toast.LENGTH_LONG).show();
                return;
            }
        });
    }
    public boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int ageYear = AgeOfUser.getYear();
        int usersAge = currentYear - ageYear;
        //Validate DOB
        if (usersAge < 2 || usersAge > 100) {
            Toast.makeText(this, "Sorry. You are not eligble to Apply.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }
}
