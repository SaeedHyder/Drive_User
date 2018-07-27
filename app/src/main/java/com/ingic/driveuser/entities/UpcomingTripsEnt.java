package com.ingic.driveuser.entities;

/**
 * Created by saeedhyder on 7/3/2017.
 */

public class UpcomingTripsEnt {

    String RideNo;
    String EstimatedFare;
    String Date;
    String Type;
    String upcomingImg;

    public UpcomingTripsEnt(String RideNo,String EstimatedFare, String Date,String Type, String upcomingImg)
    {

        setRideNo(RideNo);
        setEstimatedFare(EstimatedFare);
        setDate(Date);
        setType(Type);
        setUpcomingImg(upcomingImg);
    }

    public String getRideNo() {
        return RideNo;
    }

    public void setRideNo(String rideNo) {
        RideNo = rideNo;
    }

    public String getEstimatedFare() {
        return EstimatedFare;
    }

    public void setEstimatedFare(String estimatedFare) {
        EstimatedFare = estimatedFare;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getUpcomingImg() {
        return upcomingImg;
    }

    public void setUpcomingImg(String upcomingImg) {
        this.upcomingImg = upcomingImg;
    }
}
