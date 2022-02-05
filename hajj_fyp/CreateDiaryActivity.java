package com.example.hajj_fyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateDiaryActivity extends AppCompatActivity {
    EditText mcreatetitleofdiary, mcreatecontentofdiary;
    FloatingActionButton msavebutton;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_diary);
        msavebutton=findViewById(R.id.savediary);
        mcreatecontentofdiary=findViewById(R.id.createcontentofdiary);
        mcreatetitleofdiary=findViewById(R.id.createtitleofdiary);
        Toolbar toolbar=findViewById(R.id.toolbarofcreatediary);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        msavebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=mcreatetitleofdiary.getText().toString();
                String content=mcreatecontentofdiary.getText().toString();
                if (title.isEmpty()){
                    mcreatetitleofdiary.setError("Please Enter a Title for your Diary");
                    mcreatetitleofdiary.requestFocus();
                    return;
                }
                 if (content.isEmpty()){
                    mcreatecontentofdiary.setError("Please Enter the content");
                    mcreatecontentofdiary.requestFocus();
                    return;
                }
                else{
                    DocumentReference documentReference = firebaseFirestore.collection("diary").document(firebaseUser.getUid()).collection("myDiary").document();
                    Map<String,Object> diary = new HashMap<>();
                    diary.put("title",title);
                    diary.put("content",content);
                    documentReference.set(diary).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Diary Created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateDiaryActivity.this,NotesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to create diary", Toast.LENGTH_SHORT).show();
//                           startActivity(new Intent(CreateDiaryActivity.this,NotesActivity.class));
                        }
                    });
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}