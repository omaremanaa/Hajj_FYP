package com.example.hajj_fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

public class GuidanceFragment extends Fragment {
    ImageButton hajjGuide, introHajj, historyHajj, ihramGuide;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable  ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_guidance,container,false);
        introHajj = v.findViewById(R.id.hajjintrobtn);
        hajjGuide = v.findViewById(R.id.basichajjbtn);
        historyHajj = v.findViewById(R.id.historybtn);
        ihramGuide = v.findViewById(R.id.ihrambtn);

        introHajj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HajjIntroductionActivity.class));

            }
        });
        hajjGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BasicGuideActivity.class));
            }
        });
        historyHajj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HistoryHajjActivity.class));
            }
        });
        ihramGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), IhramGuideActivity.class));
            }
        });
         return v;

    }
}
