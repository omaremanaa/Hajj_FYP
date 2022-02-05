package com.example.hajj_fyp;

public class PrayerHelper {
    public String prayer1;
    public String prayer2;
    public String prayer3;
    public String prayer4;
    public String prayer5;

    public String getPrayer1() {
        return prayer1;
    }

    public void setPrayer1(String prayer1) {
        this.prayer1 = prayer1;
    }

    public String getPrayer2() {
        return prayer2;
    }

    public void setPrayer2(String prayer2) {
        this.prayer2 = prayer2;
    }

    public String getPrayer3() {
        return prayer3;
    }

    public void setPrayer3(String prayer3) {
        this.prayer3 = prayer3;
    }

    public String getPrayer4() {
        return prayer4;
    }

    public void setPrayer4(String prayer4) {
        this.prayer4 = prayer4;
    }

    public String getPrayer5() {
        return prayer5;
    }

    public void setPrayer5(String prayer5) {
        this.prayer5 = prayer5;
    }

    public PrayerHelper() {
    }

    public PrayerHelper(String prayer1, String prayer2, String prayer3, String prayer4, String prayer5) {
        this.prayer1 = prayer1;
        this.prayer2 = prayer2;
        this.prayer3 = prayer3;
        this.prayer4 = prayer4;
        this.prayer5 = prayer5;
    }
}
