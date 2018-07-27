package com.ingic.driveuser.entities;

/**
 * Created on 6/23/2017.
 */

public class NavigationEnt {
   private int ResId;
    private String Title;

    public NavigationEnt(int resId, String title) {
        ResId = resId;
        Title = title;
    }

    public int getResId() {

        return ResId;
    }

    public void setResId(int resId) {
        ResId = resId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
