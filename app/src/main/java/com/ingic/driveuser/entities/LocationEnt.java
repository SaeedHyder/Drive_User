package com.ingic.driveuser.entities;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created on 6/29/2017.
 */

public class LocationEnt {
    private String Address;
    private LatLng latlng;
    private String Place;

    public LocationEnt(String address, LatLng latlng, String Place) {
        Address = address;
        this.latlng = latlng;
        this.Place = Place;
    }

    public LocationEnt(String address, LatLng latlng) {
        Address = address;
        this.latlng = latlng;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }
}
