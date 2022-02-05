package com.example.hajj_fyp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class DuaActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Dua_Model> dua_modelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dua);
        //Toolbar Identifies it in the layout file
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Sets Title of the toolbar
        getSupportActionBar().setTitle("Dua");
        // Add an a back button that will return to your desired activity based on
        // ParentActivityName in manifest
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recycleViewDua);

        initData();
        setRecyclerview();
    }
    private void setRecyclerview() {
        Dua_Adapter dua_adapter = new Dua_Adapter(dua_modelList);
        recyclerView.setAdapter(dua_adapter);
        recyclerView.setHasFixedSize(true);
    }
    private void initData() {

        dua_modelList = new ArrayList<>();
        //1
        dua_modelList.add(new Dua_Model("When at Mount Safa and Mount Marwah","(1)إِنَّ الصَّفَا وَالمَرْوَةَ مِنْ شَعَائِرِاللَّهِ. أَبْدَأُ بِمَا بَدَأَ اللَّهُ بِهِ(2) اللَّهُ أَكْبَرُ،  اللَّهُ أَكْبَرُ، اللَّهُ أَكْبَرُ(3) لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ، أَنْجَزَ وَعْدَهُ، وَنَصَرَ عَبْدَهُ، وَهَزَمَ الأَحْزَابَ وَحْدَهُ",
                "Whenever the Prophet (SAW) approached Mount Safa, he would recite: \"Surely Safa and Marwah are among the signs of Allah. I begin by that which Allah began\".(1) Then he began (his Sa'y) at Mount Safa climbing it until he could see the House (Kaaba). He then faced the Qiblah repeating the words: \"Allah is the Most Great, Allah is the Most Great, Allah is the Most Great\".(2) Then he said: \"None has the right to be worshiped but Allah alone, Who has no partner, His is the dominion and His is the praise, and He is Able to do all things. None has the right to be worshiped but Allah alone, He fulfilled His Promise, He aided His slave, and He alone defeated Confederates\".(3) Then he would ask Allah for what he liked, repeating the same thing like this Three times. He did at Mount Marwah as he did at Mount Safa."));
        //2
        dua_modelList.add(new Dua_Model("The Talbiya for the one doing Hajj or Umrah.","لَبَّيْكَ اللَّهُمَّ لَبَّيْكَ، لَبَّيْكَ لاَ شَرِيكَ لَكَ لَبَّيْكَ، إِنَّ الْحَمْدَ والنِّعْمَةَ، لَكَ والْمُلْكُ، لَا شَرِيكَ لَكَ",
                "I am here at Your service, O Allah, I am here at Your service. I am here at Your service, You have no partner, I am here at Your service. Surely the praise, and blessings are Yours, and the dominion. You have no partner."));
        //3
        dua_modelList.add(new Dua_Model("The Takbeer passing the black stone","اللَّهُ أَكْبَرُ",
                "The Prophet (SAW) performed Tawaf riding a camel. Every time he passed the corner (containing the Black Stone), he would point to it with something that he was holding and say: Allaahu 'Akbar. (Allah is the Most Great)(The 'something' that was referred to in this Hadith was a cane.)"));
        //4
        dua_modelList.add(new Dua_Model("Between the Yemeni corner and the black stone","رَبَّنَا آتِنَا فِي الْدُّنْيَا حَسَنَةً وَفِي الَأخِرَةِ حَسَنةً وَقِنَا عَذَابَ النَّارِ",
                "Rabbanaa 'aatinaa fid-dunyaa hasanatan wa fil-'aakhirati hasanatan wa qinaa 'athaaban-naar." +
                        "Our Lord, grant us the good things in this world and the good things in the next life and save us from the punishment of the Fire."));
        //5
        dua_modelList.add(new Dua_Model("The Day of 'Arafah","لاَ إِلَهَ إِلاَّ اللّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
                "Laa 'ilaaha 'illallaahu wahdahu laa shareeka lahu, lahul-mulku wa lahul-hamdu wa Huwa 'alaa kulli shay'in Qadeer.\n" +
                        "The best invocation is that of the Day of Arafat, and the best that anyone can say is what I and the Prophets before me have said: \"None has the right to be worshiped but Allah alone, Who has no partner. His is the dominion and His is the praise, and He is Able to do all things\".\n"));
        //6
        dua_modelList.add(new Dua_Model("Remembrance at Muzdalifa","(1)اللَّهُ أَكْبَرُ(2)لاَ إِلَهَ إِلاَّ اللَّهُ(3)اللَّهُ اَحَدٌ",
                "Allaahu 'Akbar.(1)Laa 'ilaaha 'illallaah.(2)Allaahu 'Ahad.(3)\n" +
                        "The Prophet (SAW) rode his camel, Al-Qaswa' (from Arafah), until he reached the sacred area (Al-Mash'aril-Haraam). Then he faced the Qiblah and invoked Allah, and repeatedly said:\"Allah is the Most Great\",(1)\"There is none worthy of worship but Allah\"(2) and\"Allah is One\".(3)He remained stationary until the sky became yellow with the dawn, and then pressed on before sunrise.\n"));
        //7
        dua_modelList.add(new Dua_Model("When throwing each pebble at the Jamarat","اللَّهُ أكْبَرُ",
                "Allaahu 'Akbar.\n" +
                        "The Prophet (SAW) said Allaahu 'Akbar (Allah is the Most Great) with each pebble he threw at the Three pillars. Then he went forward, stood facing the Qiblah and raised his hands and called upon Allah. That was after (stoning) the first and second pillar. As for the third, he stoned it and called out Allaahu 'Akbar with every pebble he threw, but when he was finished he left without standing at it (for supplications)."));
        //8
        dua_modelList.add(new Dua_Model("When slaughtering or offering a sacrifice","بِسْمِ اللَّهِ وَاللَّهُ أَكْبَرُ اللَّهُمَّ مِنْكَ ولَكَ اللَّهُمَّ تَقَبَّلْ مِنِّي",
                "Bismillaahi wallaahu 'Akbar [Allaahumma minka wa laka] Allaahumma taqabbal minnee.\n" +
                        "With the Name of Allah, Allah is the Most Great! (O Allah, from You and to You.) O Allah, accept it from me.\n"));
    }
    }