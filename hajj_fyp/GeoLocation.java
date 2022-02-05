package com.example.hajj_fyp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoLocation {
    public static void getAddress(String locationAddress, Context context, Handler handler){
        Thread thread = new Thread(){
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List addresslist = geocoder.getFromLocationName(locationAddress,1);
                    if (addresslist != null && addresslist.size()>0){
                        Address address = (Address) addresslist.get(0);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(address.getLatitude()).append("&");
                        stringBuilder.append(address.getLongitude());
                        result= stringBuilder.toString();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result!=null){
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("Location",result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}
