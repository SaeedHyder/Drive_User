package com.ingic.driveuser.entities;

/**
 * Created by saeedhyder on 7/3/2017.
 */

public class PastTripsEnt {

    String RideNo;
    String Fare;
    String Date;
    String Tip;
    String PastTripImg;

    public PastTripsEnt(String RideNo, String Fare,String Date, String Tip,String PastTripImg)
    {
        setRideNo(RideNo);
        setFare(Fare);
        setDate(Date);
        setTip(Tip);
        setPastTripImg(PastTripImg);
    }

    public String getRideNo() {
        return RideNo;
    }

    public void setRideNo(String rideNo) {
        RideNo = rideNo;
    }

    public String getFare() {
        return Fare;
    }

    public void setFare(String fare) {
        Fare = fare;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTip() {
        return Tip;
    }

    public void setTip(String tip) {
        Tip = tip;
    }

    public String getPastTripImg() {
        return PastTripImg;
    }

    public void setPastTripImg(String pastTripImg) {
        PastTripImg = pastTripImg;
    }
}
