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

public class EditDiaryActivity extends AppCompatActivity {
    Intent data;
    EditText medittitleofdiary,meditcontentofdiary;
    FloatingActionButton msaveeditdiary;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);
        medittitleofdiary=findViewById(R.id.edittitleofdiary);
        meditcontentofdiary=findViewById(R.id.editcontentofdiary);
        msaveeditdiary=findViewById(R.id.saveeditdiary);

        data=getIntent();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();


        Toolbar toolbar=findViewById(R.id.toolbarofeditiary);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        msaveeditdiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"savebuton click",Toast.LENGTH_SHORT).show();

                String newtitle=medittitleofdiary.getText().toString();
                String newcontent=meditcontentofdiary.getText().toString();

                if (newtitle.isEmpty()){
                    medittitleofdiary.setError("Please Enter a Title for your Diary");
                    medittitleofdiary.requestFocus();
                    return;
                }
                if (newcontent.isEmpty()){
                    meditcontentofdiary.setError("Please Enter the content");
                    meditcontentofdiary.requestFocus();
                    return;
                }
                else
                {
                    DocumentReference documentReference=firebaseFirestore.collection("diary").document(firebaseUser.getUid()).collection("myDiary").document(data.getStringExtra("diaryId"));
                    Map<String,Object> note=new HashMap<>();
                    note.put("title",newtitle);
                    note.put("content",newcontent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Diary is updated",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditDiaryActivity.this,NotesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed To update",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


        String notetitle=data.getStringExtra("title");
        String notecontent=data.getStringExtra("content");
        meditcontentofdiary.setText(notecontent);
        medittitleofdiary.setText(notetitle);
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