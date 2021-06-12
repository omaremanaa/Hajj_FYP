package com.example.hajj_fyp;

public class PrayerTimeFormat {
    private String timeformat, prayermethod;

    public PrayerTimeFormat() {
    }

    public String getTimeformat() {
        return timeformat;
    }

    public void setTimeformat(String timeformat) {
        this.timeformat = timeformat;
    }

    public String getPrayermethod() {
        return prayermethod;
    }

    public void setPrayermethod(String prayermethod) {
        this.prayermethod = prayermethod;
    }

    public PrayerTimeFormat(String timeformat, String prayermethod) {
        timeformat = timeformat;
        prayermethod = prayermethod;
    }
}
