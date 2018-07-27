package com.ingic.driveuser.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 7/18/2017.
 */

public class PromoCodeEnt {
    @SerializedName("percentage")
    @Expose
    private String percentage;

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
