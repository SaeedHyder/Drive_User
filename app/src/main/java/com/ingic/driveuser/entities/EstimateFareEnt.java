package com.ingic.driveuser.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 7/18/2017.
 */

public class EstimateFareEnt {
    @SerializedName("estimate_fare")
    @Expose
    private String EstimateFare;

    public String getEstimateFare() {
        return EstimateFare;
    }

    public void setEstimateFare(String estimateFare) {
        this.EstimateFare = estimateFare;
    }
}
