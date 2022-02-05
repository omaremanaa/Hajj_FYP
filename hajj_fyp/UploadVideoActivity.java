package com.example.hajj_fyp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Locale;

public class UploadVideoActivity extends AppCompatActivity {
    public static final int PICK_VIDEO = 1;
    VideoView videoView;
    Button button, button2;
    ProgressBar progressBar;
    EditText editText;
    private Uri videoUri;
    MediaController mediaController;
    ActivityResultLauncher<Intent> mGetContent;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    CommuntiyHelper member;
    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        videoView = findViewById(R.id.videoview_main);
        //Toolbar with Up enable and Title of Activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upload Video");
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        member = new CommuntiyHelper();
        storageReference = FirebaseStorage.getInstance().getReference("CommunityVideo");
        databaseReference = FirebaseDatabase.getInstance().getReference("CommunityVideo");
        button2 = findViewById(R.id.abs_button);
        button = findViewById(R.id.button_upload_main);
        progressBar = findViewById(R.id.progressbar_main);
        editText = findViewById(R.id.et_video_name);
        mediaController= new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();
        ActivityResultLauncher<Intent> galleryActivityResultyLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            videoUri = data.getData();
                            videoView.setVideoURI(videoUri);
                        }
                        else{
                            Toast.makeText(UploadVideoActivity.this,"Canceled...",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                galleryActivityResultyLauncher.launch(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadVideo();
            }
        });


    }
    public void ShowVideo(View view) {
    }
    private void UploadVideo() {
        String videoName = editText.getText().toString();
        String search = editText.getText().toString().toLowerCase();
        if (videoUri != null || !TextUtils.isEmpty(videoName)){
            progressBar.setVisibility(View.VISIBLE);
            String strUri = videoUri.toString();
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + strUri);
            uploadTask = reference.putFile(videoUri);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                Uri downloadUri = task.getResult();
                                progressBar.setVisibility(View.VISIBLE);
                                Toast.makeText(UploadVideoActivity.this,"Data Saved",Toast.LENGTH_SHORT).show();
                                member.setName(videoName);
                                member.setMediaurl(downloadUri.toString());
                                member.setSearch(search);
                                String i = databaseReference.push().getKey();
                                databaseReference.child(i).setValue(member);
                                startActivity(new Intent(UploadVideoActivity.this, CommunityGallery.class));

                            }
                            else{
                                Toast.makeText(UploadVideoActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
        else{
            Toast.makeText(UploadVideoActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();
        }
    }
}