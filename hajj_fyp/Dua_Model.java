package com.example.hajj_fyp;

public class Dua_Model {
    private String title, arabicDescription, englishDescription;
    private boolean expandable;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArabicDescription() {
        return arabicDescription;
    }

    public void setArabicDescription(String arabicDescription) {
        this.arabicDescription = arabicDescription;
    }

    public String getEnglishDescription() {
        return englishDescription;
    }

    public void setEnglishDescription(String englishDescription) {
        this.englishDescription = englishDescription;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public Dua_Model(String title, String arabicDescription, String englishDescription) {
        this.title = title;
        this.arabicDescription = arabicDescription;
        this.englishDescription = englishDescription;
        this.expandable = false;
    }

    @Override
    public String toString() {
        return "Sunah_Model{" +
                "title='" + title + '\'' +
                ", arabicDescription='" + arabicDescription + '\'' +
                ", englishDescription='" + englishDescription + '\'' +
                '}';
    }
}

