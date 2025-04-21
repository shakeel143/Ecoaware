package com.example.ecoguardians.Model;

public class EducationItem {
    public String title;
    public String shortText;
    public String fullText;
    public int imageRes;
    public boolean isExpanded;

    public EducationItem(String title, String shortText, String fullText, int imageRes) {
        this.title = title;
        this.shortText = shortText;
        this.fullText = fullText;
        this.imageRes = imageRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}


