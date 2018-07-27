package com.ingic.driveuser.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by saeedhyder on 1/10/2018.
 */

public class AllDriversHomeEnt {

    @SerializedName("driverList")
    @Expose
    private ArrayList<DriverEnt> driverList = null;
    @SerializedName("peakFactor")
    @Expose
    private String peakFactor;

    public ArrayList<DriverEnt> getDriverList() {
        return driverList;
    }

    public void setDriverList(ArrayList<DriverEnt> driverList) {
        this.driverList = driverList;
    }

    public String getPeakFactor() {
        return peakFactor;
    }

    public void setPeakFactor(String peakFactor) {
        this.peakFactor = peakFactor;
    }
}
