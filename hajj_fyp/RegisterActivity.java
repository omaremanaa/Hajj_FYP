package com.example.hajj_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    EditText regName, regEmail, regPassword, regConfirmPassword;
    CheckBox terms;
    DatePicker regAge;
    Button regbtn;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    TextView termss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // The entry point of the Firebase Authentication SDK
        firebaseAuth = FirebaseAuth.getInstance();
        //Toolbar Identifies it in the layout file
        Toolbar toolbar = findViewById(R.id.toolbar);
        termss = findViewById(R.id.terms);
        setSupportActionBar(toolbar);
        // Sets Title of the toolbar
        getSupportActionBar().setTitle("Sign Up");
        // Add an a back button that will return to your desired activity based on
        // ParentActivityName in manifest
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        // Identifying all TextViews to recieve the input
        regName = findViewById(R.id.editTextTextPersonName);
        regEmail = findViewById(R.id.editTextTextEmailAddress);
        regPassword = findViewById(R.id.editTextTextPassword);
        regConfirmPassword = findViewById(R.id.editTextTextPassword2);
        regAge = findViewById(R.id.AgeofUser);
        terms = findViewById(R.id.checkBox);
        progressBar = findViewById(R.id.progbarr);



        regbtn = findViewById(R.id.register3);
        // reference will point at your project's parent and then it will
        // go at your desired child based on what is written
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regUser();

            }
        });
        termss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogue();
            }

            private void openDialogue() {
                TermsDialogue termsDialogue = new TermsDialogue();
                termsDialogue.show(getSupportFragmentManager(),"Terms & Conditions Dialogue");
            }
        });
    }
    public boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int ageYear = regAge.getYear();
        int usersAge = currentYear - ageYear;
        //Validate DOB
        if (usersAge < 2 || usersAge > 100) {
            Toast.makeText(this, "Sorry. You are not eligble to Apply.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }
    public void regUser() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int ageYear = regAge.getYear();
        int usersAge = currentYear - ageYear;
        // Recieve TextView Inputs and convert them to string
        // so that we can see their validation.
        String email = regEmail.getText().toString().trim();
        String name = regName.getText().toString().trim();
        String age = String.valueOf(usersAge);
        String password = regPassword.getText().toString().trim();
        String confirmpassword = regConfirmPassword.getText().toString().trim();

        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";


        //Validate for Name
        if (name.isEmpty()) {
            regName.setError("Please Enter Your Name");
            regName.requestFocus();
            return;
        }
        //Validate Age
        if (!validateAge()){
            return;
        }
        //Validate Email
        if (email.isEmpty()) {
            regEmail.setError("E-mail is required");
            regEmail.requestFocus();
            return;
        }
        //Validate Email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            regEmail.setError("Please provide a valid email");
            regEmail.requestFocus();
            return;
        }

        //Validate Password
        if (password.isEmpty()) {
            regPassword.setError("Please fill in Password.");
            regPassword.requestFocus();
            return;
        }
        if (!password.matches(regex)) {
            regPassword.setError("Password has to contain minimum 8 letters.");
            regPassword.requestFocus();
            return;
        }
        //Validate Confirm Password
        if (!confirmpassword.equals(password)) {
            regConfirmPassword.setError("Has to be same as Password.");
            regConfirmPassword.requestFocus();
            return;
        }
        //Validate Terms checkbox
        if (!terms.isChecked()){
            terms.setError("Accept Terms");
            terms.requestFocus();
            return;
        }
        // Create a unique Password Id that is pushed inside the users password data so the admin
        // wouldnt know his end users passwords
        String uniquePasswordID = UUID.randomUUID().toString();

        progressBar.setVisibility(View.VISIBLE);
        // In this line of code firebase would take the email and based on that
        // it would send an Authentication so the user wouldn't be creating duplicate accounts
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Userhelper is a class that recieves the data based on what is recieved from the User and
                    // sends it to the firebase database under a unique Identification (Uid)
                    UserHelper userHelper = new UserHelper(email, name, uniquePasswordID, age);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(userHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.sendEmailVerification();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                Toast.makeText(RegisterActivity.this, "User Has been registered. Please verify by Email.", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);

                            }

                        }
                    });
                } else {
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    Toast.makeText(RegisterActivity.this, "Failed To Register!Try Again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

}