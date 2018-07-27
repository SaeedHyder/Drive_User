package com.ingic.driveuser.entities;

/**
 * Created on 6/22/2017.
 */

public class CarRide {
    private String Title;
    private int drawable;
    private int drawable_big;
    private boolean selected;

    public CarRide(String title, int drawable, int drawable_big, boolean selected) {
        Title = title;
        this.drawable = drawable;
        this.drawable_big = drawable_big;
        this.selected = selected;

    }

    public CarRide(String title, int drawable) {
        Title = title;
        this.drawable = drawable;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getDrawable_big() {

        return drawable_big;
    }

    public void setDrawable_big(int drawable_big) {
        this.drawable_big = drawable_big;
    }

    public String getTitle() {

        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
