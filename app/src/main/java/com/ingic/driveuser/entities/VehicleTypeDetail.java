package com.ingic.driveuser.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by saeedhyder on 4/9/2018.
 */

public class VehicleTypeDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("vehicle_charges")
    @Expose
    private String vehicleCharges;
    @SerializedName("kilometer_charges")
    @Expose
    private String kilometerCharges;
    @SerializedName("time_charges")
    @Expose
    private String timeCharges;
    @SerializedName("capacity")
    @Expose
    private String capacity;
    @SerializedName("vehicle_picture_one")
    @Expose
    private String vehiclePictureOne;
    @SerializedName("vehicle_picture_two")
    @Expose
    private String vehiclePictureTwo;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("vehicle_image_one")
    @Expose
    private String vehicleImageOne;
    @SerializedName("vehicle_image_two")
    @Expose
    private String vehicleImageTwo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVehicleCharges() {
        return vehicleCharges;
    }

    public void setVehicleCharges(String vehicleCharges) {
        this.vehicleCharges = vehicleCharges;
    }

    public String getKilometerCharges() {
        return kilometerCharges;
    }

    public void setKilometerCharges(String kilometerCharges) {
        this.kilometerCharges = kilometerCharges;
    }

    public String getTimeCharges() {
        return timeCharges;
    }

    public void setTimeCharges(String timeCharges) {
        this.timeCharges = timeCharges;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getVehiclePictureOne() {
        return vehiclePictureOne;
    }

    public void setVehiclePictureOne(String vehiclePictureOne) {
        this.vehiclePictureOne = vehiclePictureOne;
    }

    public String getVehiclePictureTwo() {
        return vehiclePictureTwo;
    }

    public void setVehiclePictureTwo(String vehiclePictureTwo) {
        this.vehiclePictureTwo = vehiclePictureTwo;
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

    public String getVehicleImageOne() {
        return vehicleImageOne;
    }

    public void setVehicleImageOne(String vehicleImageOne) {
        this.vehicleImageOne = vehicleImageOne;
    }

    public String getVehicleImageTwo() {
        return vehicleImageTwo;
    }

    public void setVehicleImageTwo(String vehicleImageTwo) {
        this.vehicleImageTwo = vehicleImageTwo;
    }

}
