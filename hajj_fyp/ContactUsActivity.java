package com.example.hajj_fyp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        //Toolbar Identifies it in the layout file
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Sets Title of the toolbar
        getSupportActionBar().setTitle("Contact Us");
        // Add an a back button that will return to your desired activity based on
        // ParentActivityName in manifest
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        TextView mailfeedback = (TextView) findViewById(R.id.gmailHyperLink);
        mailfeedback.setMovementMethod(LinkMovementMethod.getInstance());

        TextView instaAccount = (TextView) findViewById(R.id.instaHyperLink);
        instaAccount.setMovementMethod(LinkMovementMethod.getInstance());

        TextView facebookPage = (TextView) findViewById(R.id.faceHyperLink);
        facebookPage.setMovementMethod(LinkMovementMethod.getInstance());

        TextView twitterAccount = (TextView) findViewById(R.id.twitterHyperLink);
        twitterAccount.setMovementMethod(LinkMovementMethod.getInstance());
    }
}