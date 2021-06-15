package com.example.hajj_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    TextView forgotPassword;
    Button submitlogin;
    EditText emailLogin, passwordsign;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Toolbar Identifies it in the layout file
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Sets Title of the toolbar
        getSupportActionBar().setTitle("Sign In");
        // Add an a back button that will return to your desired activity based on
        // ParentActivityName in manifest
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        // The entry point of the Firebase Authentication SDK
        firebaseAuth = FirebaseAuth.getInstance();
        // give and for each EditText and Button in LogIn Activity
        emailLogin = (EditText) findViewById(R.id.editTextTextEmailAddress3);
        passwordsign = (EditText) findViewById(R.id.editTextTextPassword3);
        progressBar = findViewById(R.id.progbarlogin);
        submitlogin = (Button) findViewById(R.id.changepassword);
        //HyperLink for Forget Password.
        forgotPassword = findViewById(R.id.clickHerePassword);
        forgotPassword.setMovementMethod(LinkMovementMethod.getInstance());
        forgotPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        // When Button is Clicked what to do
        submitlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // It goes over the method loginUser when user clicks on Log In
                userLogin();
            }
        });
    }

    public void userLogin() {
        String email = emailLogin.getText().toString().trim();
        String password = passwordsign.getText().toString().trim();
        if (email.isEmpty()) {
            emailLogin.setError("Email is Required");
            emailLogin.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLogin.setError("Please enter a valid Email");
            emailLogin.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordsign.setError("Password is Required");
            passwordsign.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                    } else {

                        Toast.makeText(LoginActivity.this, "Failed to LogIn! Please Verify you're Email!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Failed to LogIn! Please Try Again!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        });
    }

}