package com.example.sampleassignment1;

import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

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

    public UserFirebaseRecord(Double latitude, Double longitude, String address, Date date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        Locale locale = new Locale("en", "AU");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        this.date = dateFormat.format(date);
    }

    public boolean after(UserFirebaseRecord r){
        Locale locale = new Locale("en", "AU");
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        Date r_d;
        Date this_d;
        try {
            r_d = dateFormat.parse(r.date);
            this_d = dateFormat.parse(this.date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if(r_d.after(this_d)) return false;
        else return true;
    }
}
