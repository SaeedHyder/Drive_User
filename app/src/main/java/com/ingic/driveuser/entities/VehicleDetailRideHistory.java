package com.ingic.driveuser.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by saeedhyder on 4/9/2018.
 */

public class VehicleDetailRideHistory {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("driver_id")
    @Expose
    private String driverId;
    @SerializedName("vehicle_id")
    @Expose
    private String vehicleId;
    @SerializedName("vehicle_name")
    @Expose
    private String vehicleName;
    @SerializedName("vehicle_model")
    @Expose
    private String vehicleModel;
    @SerializedName("vehicle_number")
    @Expose
    private String vehicleNumber;
    @SerializedName("vehicle_color")
    @Expose
    private String vehicleColor;
    @SerializedName("vehicle_picture")
    @Expose
    private String vehiclePicture;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("vehicle_image")
    @Expose
    private String vehicleImage;
    @SerializedName("vehicle_type_detail")
    @Expose
    private VehicleTypeDetail vehicleTypeDetail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehiclePicture() {
        return vehiclePicture;
    }

    public void setVehiclePicture(String vehiclePicture) {
        this.vehiclePicture = vehiclePicture;
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

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public VehicleTypeDetail getVehicleTypeDetail() {
        return vehicleTypeDetail;
    }

    public void setVehicleTypeDetail(VehicleTypeDetail vehicleTypeDetail) {
        this.vehicleTypeDetail = vehicleTypeDetail;
    }
}
