package com.example.sampleassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

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
        myLocationPlaceMap.getLatLngAddress(myLocations);

        if (myLocations.size() > 0) {
            myLocation = myLocations.get(0);
            myLocations.clear();

            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("lat", myLocation.getLatitude());
            intent.putExtra("lng", myLocation.getLongitude());
            intent.putExtra("addr", myLocation.getAddress());
            startActivity(intent);
        }
    }

}