package com.ingic.driveuser.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 7/19/2017.
 */

public class RideDriverEnt {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ride_id")
    @Expose
    private Integer rideId;
    @SerializedName("driver_id")
    @Expose
    private Integer driverId;
    @SerializedName("ride_status")
    @Expose
    private Integer rideStatus;
    @SerializedName("trip_status")
    @Expose
    private Integer tripStatus;
    @SerializedName("driver_detail")
    @Expose
    private DriverEnt driverDetail;
    @SerializedName("ride_detail")
    @Expose
    private RideEnt rideDetail;
    @SerializedName("vehicle_detail")
    @Expose
    private VehicleDetail vehicleDetail;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(Integer rideStatus) {
        this.rideStatus = rideStatus;
    }

    public Integer getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(Integer tripStatus) {
        this.tripStatus = tripStatus;
    }

    public DriverEnt getDriverDetail() {
        return driverDetail;
    }

    public void setDriverDetail(DriverEnt driverDetail) {
        this.driverDetail = driverDetail;
    }

    public RideEnt getRideDetail() {
        return rideDetail;
    }

    public void setRideDetail(RideEnt rideDetail) {
        this.rideDetail = rideDetail;
    }

    public VehicleDetail getVehicleDetail() {
        return vehicleDetail;
    }

    public void setVehicleDetail(VehicleDetail vehicleDetail) {
        this.vehicleDetail = vehicleDetail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
