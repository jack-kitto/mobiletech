package com.example.sampleassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.sampleassignment1.databinding.ActivityMapsBinding;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    SupportStreetViewPanoramaFragment streetViewPanoramaFragment;
    StreetViewPanorama mStreetViewPanorama;
    User registeredUser;
    User currentUser;
    double latitude;
    double longitude;
    LatLng latLngRed, latLngBlue;
    String address;
    TextView textViewAddress;
    TextView textViewLatitude;
    TextView textViewLongitude;
    Button btnMapStreetView;
    PlacesClient placesClient;
    Marker redMarker, blueMarker;
    boolean showMap = true;
    MyLocationPlaceMap myLocationPlaceMap;
    ArrayList<MyLocationPlace> myLocations = new ArrayList<>();
    MyLocationPlace myLocation;
public void handleCurrentUser(){
    UserFirebaseRecord record = currentUser.data.get(currentUser.data.size() - 1);
    Log.d("D", "PRINTING CURRENT USER RECORD");
    record.print();
    latitude = record.latitude;
    longitude = record.longitude;
    latLngRed = new LatLng(latitude, longitude);
    address = record.address;

    textViewAddress = findViewById(R.id.textViewStreetAddress);
    textViewAddress.setText("Address: " + address);
    textViewLatitude = findViewById(R.id.textViewLatitude);
    textViewLatitude.setText("Latitude: " + latitude);
    textViewLongitude = findViewById(R.id.textViewLongitude);
    textViewLongitude.setText("Longitude: " + longitude);
}

public void handleRegisteredUser(){

}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        binding = ActivityMapsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        setTitle("My Location Details");
        btnMapStreetView = findViewById(R.id.buttonMapStreetView);

        Bundle extras = getIntent().getExtras();
        Intent myIntent = getIntent();

        registeredUser = (User) myIntent.getSerializableExtra("registeredUser", User.class);
        currentUser = (User) myIntent.getSerializableExtra("currentUser", User.class);
        Log.d("D", "START PRINTING REGISTERED USERS DATA");
        registeredUser.printData();
        Log.d("D", "PRINTING REGISTERED USERS DATA");

        if(currentUser.name.matches(registeredUser.name)) handleCurrentUser();
        else handleRegisteredUser();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        streetViewPanoramaFragment = (SupportStreetViewPanoramaFragment) getSupportFragmentManager().findFragmentById(R.id.streetView);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
        mapFragment.getView().bringToFront();
        myLocationPlaceMap = new MyLocationPlaceMap(getApplicationContext(), MapsActivity.this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        if(currentUser.name.matches(registeredUser.name)) handleCurrentUser(mMap);
        else handleRegisteredUser(mMap);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @SuppressLint("PotentialBehaviorOverride")
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (!marker.equals(redMarker)) return false;
                streetViewPanoramaFragment.getView().bringToFront();
                btnMapStreetView.setText("Show map");
                showMap = false;
                return true;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                if (marker.getId().equals(redMarker.getId())) return;
                streetViewPanoramaFragment.getView().bringToFront();
                latLngBlue = marker.getPosition();
                mStreetViewPanorama.setPosition(latLngBlue);
                }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }
            @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                TextView title = (TextView) infoWindow.findViewById(R.id.textViewTitle);
                TextView snippet = (TextView) infoWindow.findViewById(R.id.textViewSnippet);
                ImageView image = (ImageView) infoWindow.findViewById(R.id.imageView);

                if (marker.getTitle() != null && marker.getSnippet() != null) {
                    title.setText(marker.getTitle());
                    snippet.setText(marker.getSnippet());
                } else {
                    title.setText("No info available");
                    snippet.setText("No location available");
                }
                image.setImageDrawable(getResources()
                        .getDrawable(R.drawable.blue_marker, getTheme()));
                return infoWindow;
            }
        });

    }

    @SuppressLint("PotentialBehaviorOverride")
    private void handleCurrentUser(GoogleMap mMap) {
        UserFirebaseRecord record = currentUser.data.get(currentUser.data.size() - 1);
        LatLng latLng = new LatLng(record.latitude, record.longitude);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                redMarker = mMap.addMarker(new MarkerOptions()
                        .title("Show Surroundings")
                        .snippet("Latitude: " + record.latitude + ", Longitude: " + record.longitude +
                                "\nAddress: " + record.address)
                        .position(latLng)
                );
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            }
        });


    }
    private void handleRegisteredUser(GoogleMap mMap) {
    }

    @Override
    public void onStreetViewPanoramaReady(@NonNull StreetViewPanorama streetViewPanorama) {
        mStreetViewPanorama = streetViewPanorama;
        mStreetViewPanorama.setPosition(latLngRed);
    }

    public void showNearby(View view) {
        mapFragment.getView().bringToFront();
        myLocationPlaceMap.getNearbyPlaces(mMap, "YOUR_API_KEY");
    }

    public void showMapStreetView(View view) {
        showMap = !showMap;
        if (showMap) {
            mapFragment.getMapAsync(this);
            mapFragment.getView().bringToFront();
            btnMapStreetView.setText("Show Street View");
        } else {
            streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
            streetViewPanoramaFragment.getView().bringToFront();
            btnMapStreetView.setText("Show Map");
        }
    }

}