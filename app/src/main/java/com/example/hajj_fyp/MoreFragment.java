package com.example.hajj_fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MoreFragment extends Fragment {
    ImageButton prayetrack, hajjtrack, dua,suna,gallery,faq;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable  ViewGroup container, Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.fragment_more,container,false);
         prayetrack = v.findViewById(R.id.prayertrackerbutton);
         hajjtrack = v.findViewById(R.id.hajjtrackerbutton);
         dua = v.findViewById(R.id.duabutton);
         suna = v.findViewById(R.id.sunabutton);
         gallery = v.findViewById(R.id.gallerbutton);
         faq = v.findViewById(R.id.faqbutton);
         prayetrack.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(getActivity(), PrayerTracker.class));
             }
         });
        hajjtrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HajjTracker.class));
            }
        });
        dua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DuaActivity.class));
            }
        });
        suna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SunaActivity.class));
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GalleryActivity.class));
            }
        });
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FAQActivity.class));
            }
        });
         return v;
}

}
