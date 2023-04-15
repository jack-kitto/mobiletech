package com.example.sampleassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    MyLocationPlaceMap myLocationPlaceMap;
    ArrayList<MyLocationPlace> myLocations = new ArrayList<>();
    MyLocationPlace myLocation;
    ArrayList<String> users;
    MyDbHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDbHelper = new MyDbHelper(this, "sampleassignment1", null, 1);
        users = myDbHelper.getAllUsers();
        myLocationPlaceMap = new MyLocationPlaceMap(getApplicationContext(), MainActivity.this);
        myLocationPlaceMap.requestPermissions();
        myLocationPlaceMap.getLatLngAddress(myLocations);
        Button buttonWhereAmI = findViewById(R.id.buttonWhereAmI);
        Button buttonWhereIsUser2 = findViewById(R.id.buttonWhereIsUser2);
        Button buttonWhereIsUser3 = findViewById(R.id.buttonWhereIsUser3);
        boolean isUser2Set = false;
        for (String user : users) {
            Log.d("D", "USER: " + user);
            String name = user.split(",")[0];
            String isSelected = user.split(",")[1];
            if(isSelected.matches("1"))buttonWhereAmI.setText("WHERE AM I? (" + name + ")");
            else if(!isUser2Set){
                buttonWhereIsUser2.setText("WHERE IS " + name);
                isUser2Set = true;
            }else buttonWhereIsUser3.setText("WHERE IS " + name);
        }

    }

    public void whereAmI (View view) {
        if (myLocations.size() <= 0) return;
        myLocationPlaceMap.getLatLngAddress(myLocations);
        myLocation = myLocations.get(0);
        myLocations.clear();

        Double lat = myLocation.getLatitude();
        Double lon = myLocation.getLongitude();
        String add = myLocation.getAddress();
        Date date = new Date();
        updateUserInFirebase(
                myDbHelper.getNameFromUser(users.get(0)),
                lat,
                lon,
                add,
                myDbHelper.getIdFromUser(users.get(0)),
                myDbHelper.getSelectedFromUser(users.get(0)),
                date.toString()
        );

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("lat", myLocation.getLatitude());
        intent.putExtra("lng", myLocation.getLongitude());
        intent.putExtra("addr", myLocation.getAddress());
        startActivity(intent);
    }

    public void whereIsUser2(View view){
        if (myLocations.size() <= 0) return;
        myLocationPlaceMap.getLatLngAddress(myLocations);
        myLocation = myLocations.get(0);
        myLocations.clear();

        Double lat = myLocation.getLatitude();
        Double lon = myLocation.getLongitude();
        String add = myLocation.getAddress();
        Date date = new Date();
        updateUserInFirebase(
                myDbHelper.getNameFromUser(users.get(1)),
                lat,
                lon,
                add,
                myDbHelper.getIdFromUser(users.get(1)),
                myDbHelper.getSelectedFromUser(users.get(1)),
                date.toString()
        );

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("lat", myLocation.getLatitude());
        intent.putExtra("lng", myLocation.getLongitude());
        intent.putExtra("addr", myLocation.getAddress());
        startActivity(intent);
    }
    public void whereIsUser3(View view){
        if (myLocations.size() <= 0) return;
        myLocationPlaceMap.getLatLngAddress(myLocations);
        myLocation = myLocations.get(0);
        myLocations.clear();

        Double lat = myLocation.getLatitude();
        Double lon = myLocation.getLongitude();
        String add = myLocation.getAddress();
        Date date = new Date();
        updateUserInFirebase(
                myDbHelper.getNameFromUser(users.get(2)),
                lat,
                lon,
                add,
                myDbHelper.getIdFromUser(users.get(2)),
                myDbHelper.getSelectedFromUser(users.get(2)),
                date.toString()
        );

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("lat", myLocation.getLatitude());
        intent.putExtra("lng", myLocation.getLongitude());
        intent.putExtra("addr", myLocation.getAddress());
        startActivity(intent);
    }

    public void updateUserInFirebase(
            String userName,
            Double latitude,
            Double longitude,
            String address,
            String id,
            String selected,
            String date
    ){
        Log.d("D","LAT: " + String.valueOf(latitude));
        Log.d("D","LON: " + String.valueOf(longitude));
        Log.d("D","ADD: " + address);
        Log.d("D","ID: " + id);
        Log.d("D","userName: " + userName);
        Log.d("D","selected: " + selected);
        Log.d("D","date: " + date);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        String key_ = dbRef.push().getKey(); // to generate a random key
        dbRef.child(userName).child(key_).child("latitude").setValue(latitude);
        dbRef.child(userName).child(key_).child("longitude").setValue(longitude);
        dbRef.child(userName).child(key_).child("address").setValue(address);
        dbRef.child(userName).child(key_).child("date").setValue(date);
    }
}