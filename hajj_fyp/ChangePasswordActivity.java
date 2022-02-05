package com.example.hajj_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    Button newPassButton;
    EditText oldpass, newpass, confirmpass;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        oldpass = findViewById(R.id.OldPass);
        newpass = findViewById(R.id.NewPass);
        confirmpass = findViewById(R.id.NewConfirm);
        newPassButton = findViewById(R.id.SubmitNewPass);
        progressBar = findViewById(R.id.progbarchangepass);
        mAuth = FirebaseAuth.getInstance();
        newPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String soldpass, snewpass, sconfirmpass;
                soldpass = oldpass.getText().toString().trim();
                snewpass = newpass.getText().toString().trim();
                sconfirmpass = confirmpass.getText().toString().trim();
                String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
                if (soldpass.isEmpty()) {
                    oldpass.setError("Please fill in Password.");
                    oldpass.requestFocus();
                    return;
                }
                if (!soldpass.matches(regex)) {
                    oldpass.setError("Password has to contain minimum 8 letters.");
                    oldpass.requestFocus();
                    return;
                }
                if (snewpass.isEmpty()) {
                    newpass.setError("Please fill in Password.");
                    newpass.requestFocus();
                    return;
                }
                if (!snewpass.matches(regex)) {
                    newpass.setError("Password has to contain minimum 8 letters.");
                    newpass.requestFocus();
                    return;
                }
                if (!sconfirmpass.equals(snewpass)) {
                    confirmpass.setError("Has to be same as Password.");
                    confirmpass.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), soldpass);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(snewpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ChangePasswordActivity.this, "Password Change Succesfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ChangePasswordActivity.this, MenuActivity.class));
                                    } else{
                                        Toast.makeText(ChangePasswordActivity.this, "Error 404", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ChangePasswordActivity.this, MenuActivity.class));
                                    }
                                }
                            });

                        }
                        else{
                            Toast.makeText(ChangePasswordActivity.this, "Error 404", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ChangePasswordActivity.this, MenuActivity.class));
                        }

                    }
                });


            }



        });

//            private void updatePassword(String soldpass, String snewpass) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), soldpass);
//                user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        user.updatePassword(snewpass).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(ChangePasswordActivity.this, "Password Change Succesfully", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(ChangePasswordActivity.this, MenuActivity.class));
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(ChangePasswordActivity.this, "Error 404", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
//                            }
//                        });
//                    }
//                });
//            }
//        });
    }
}