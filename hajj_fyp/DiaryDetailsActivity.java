package com.example.hajj_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DiaryDetailsActivity extends AppCompatActivity {
    private TextView mtitleofdiarydetail,mcontentofdiarydetail;
    FloatingActionButton mgotoeditdiary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_details);
        mtitleofdiarydetail=findViewById(R.id.titleofdiarydetail);
        mcontentofdiarydetail=findViewById(R.id.contentofdiarydetail);
        mgotoeditdiary=findViewById(R.id.gotoeditdiary);
        Toolbar toolbar=findViewById(R.id.toolbarofdiarydetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data=getIntent();

        mgotoeditdiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),EditDiaryActivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("diaryId",data.getStringExtra("diaryId"));
                v.getContext().startActivity(intent);
            }
        });

        mcontentofdiarydetail.setText(data.getStringExtra("content"));
        mtitleofdiarydetail.setText(data.getStringExtra("title"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}