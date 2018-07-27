package com.ingic.driveuser.entities;

/**
 * Created by saeedhyder on 7/26/2017.
 */

public class UserHomeEnt {

    private double latitude;
    private double longitude;
    private LocationEnt origin;
    private LocationEnt destination;
    private double distance;
    private PromoCodeEnt promoCodeEnt;
    private SelectCarEnt selectCarEnt;
    private CreateRideEnt rideEnt;
    private DriverEnt driverDetail;
    private boolean isRideinSession;
    private RideDriverEnt rideDriverEnt;
    private LocationEnt originScheduleRide;
    private LocationEnt destinationScheduleRide;
    private boolean IsScheduleRide;
    private String DriverId;

    public boolean isRideinSession() {
        return isRideinSession;
    }
    public String getDriverId() {
        return DriverId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }

    public boolean isScheduleRide() {
        return IsScheduleRide;
    }

    public void setScheduleRide(boolean scheduleRide) {
        IsScheduleRide = scheduleRide;
    }

    public LocationEnt getOriginScheduleRide() {
        return originScheduleRide;
    }

    public void setOriginScheduleRide(LocationEnt originScheduleRide) {
        this.originScheduleRide = originScheduleRide;
    }

    public LocationEnt getDestinationScheduleRide() {
        return destinationScheduleRide;
    }

    public void setDestinationScheduleRide(LocationEnt destinationScheduleRide) {
        this.destinationScheduleRide = destinationScheduleRide;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LocationEnt getOrigin() {
        return origin;
    }

    public void setOrigin(LocationEnt origin) {
        this.origin = origin;
    }

    public LocationEnt getDestination() {
        return destination;
    }

    public void setDestination(LocationEnt destination) {
        this.destination = destination;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


    public PromoCodeEnt getPromoCodeEnt() {
        return promoCodeEnt;
    }

    public void setPromoCodeEnt(PromoCodeEnt promoCodeEnt) {
        this.promoCodeEnt = promoCodeEnt;
    }


    public void setRideinSession(boolean rideinSession) {
        isRideinSession = rideinSession;
    }


    public SelectCarEnt getSelectCarEnt() {
        return selectCarEnt;
    }

    public void setSelectCarEnt(SelectCarEnt selectCarEnt) {
        this.selectCarEnt = selectCarEnt;
    }

    public CreateRideEnt getRideEnt() {
        return rideEnt;
    }

    public void setRideEnt(CreateRideEnt rideEnt) {
        this.rideEnt = rideEnt;
    }

    public DriverEnt getDriverDetail() {
        return driverDetail;
    }

    public void setDriverDetail(DriverEnt driverDetail) {
        this.driverDetail = driverDetail;
    }


    public RideDriverEnt getRideDriverEnt() {
        return rideDriverEnt;
    }

    public void setRideDriverEnt(RideDriverEnt rideDriverEnt) {
        this.rideDriverEnt = rideDriverEnt;
    }


}
