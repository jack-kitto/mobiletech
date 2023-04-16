package com.example.sampleassignment1;

import android.util.Log;

import java.io.Serializable;

public class UserFirebaseRecord implements Serializable {
    public String date;
    public Double latitude;
    public Double longitude;
    public String address;

    public void print() {
        Log.d("D", "-------------------------------------");
        Log.d("D", "DATE: " + this.date);
        Log.d("D", "LATITUDE: " + this.latitude);
        Log.d("D", "LONGITUDE: " + this.longitude);
        Log.d("D", "ADDRESS: " + this.address);
        Log.d("D", "-------------------------------------");
    }

    public UserFirebaseRecord() {
        this.latitude = null;
        this.longitude = null;
        this.address = null;
        this.date = null;
    }

    public UserFirebaseRecord(Double latitude, Double longitude, String address, String date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.date = date;
    }
}
