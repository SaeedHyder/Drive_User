package com.ingic.driveuser.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by saeedhyder on 4/9/2018.
 */

public class RideTrackEnt {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("cancel_id")
    @Expose
    private String cancelId;
    @SerializedName("pickup_latitude")
    @Expose
    private String pickupLatitude;
    @SerializedName("pickup_longitude")
    @Expose
    private String pickupLongitude;
    @SerializedName("pickup_address")
    @Expose
    private String pickupAddress;
    @SerializedName("pickup_place")
    @Expose
    private String pickupPlace;
    @SerializedName("destination_latitude")
    @Expose
    private String destinationLatitude;
    @SerializedName("destination_longitude")
    @Expose
    private String destinationLongitude;
    @SerializedName("destination_address")
    @Expose
    private String destinationAddress;
    @SerializedName("destination_place")
    @Expose
    private String destinationPlace;
    @SerializedName("vehicle_id")
    @Expose
    private String vehicleId;
    @SerializedName("percentage")
    @Expose
    private String percentage;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("peak_factor")
    @Expose
    private String peakFactor;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("time_duration")
    @Expose
    private String timeDuration;
    @SerializedName("estimate_fare")
    @Expose
    private String estimateFare;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("paid")
    @Expose
    private String paid;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("driver_info")
    @Expose
    private DriverEnt driverInfo;
    @SerializedName("vehicle_detail")
    @Expose
    private VehicleDetailRideHistory vehicleDetail;
    @SerializedName("user_info")
    @Expose
    private UserInfo userInfo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCancelId() {
        return cancelId;
    }

    public void setCancelId(String cancelId) {
        this.cancelId = cancelId;
    }

    public String getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(String pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public String getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(String pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getPickupPlace() {
        return pickupPlace;
    }

    public void setPickupPlace(String pickupPlace) {
        this.pickupPlace = pickupPlace;
    }

    public String getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(String destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public String getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(String destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getDestinationPlace() {
        return destinationPlace;
    }

    public void setDestinationPlace(String destinationPlace) {
        this.destinationPlace = destinationPlace;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPeakFactor() {
        return peakFactor;
    }

    public void setPeakFactor(String peakFactor) {
        this.peakFactor = peakFactor;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        this.timeDuration = timeDuration;
    }

    public String getEstimateFare() {
        return estimateFare;
    }

    public void setEstimateFare(String estimateFare) {
        this.estimateFare = estimateFare;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DriverEnt getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverEnt driverInfo) {
        this.driverInfo = driverInfo;
    }

    public VehicleDetailRideHistory getVehicleInfo() {
        return vehicleDetail;
    }

    public void setVehicleInfo(VehicleDetailRideHistory vehicleInfo) {
        this.vehicleDetail = vehicleInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
