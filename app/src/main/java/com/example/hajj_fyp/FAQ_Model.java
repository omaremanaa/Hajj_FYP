package com.example.hajj_fyp;

public class FAQ_Model {
    private String title, description;
    private boolean expandable;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public FAQ_Model(String title, String description) {
        this.title = title;
        this.description = description;
        this.expandable = false;
    }

    @Override
    public String toString() {
        return "FAQ_Model{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
