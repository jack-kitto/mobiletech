package com.example.sampleassignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.sampleassignment1.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    if(currentUser.data.isEmpty()) return;
    UserFirebaseRecord record = currentUser.data.get(currentUser.data.size() - 1);
    Log.d("D", "@@@@@@@@@@@@@@@@@@@@@ PRINTING CURRENT USER (" + currentUser.name + ") RECORD @@@@@@@@@@@@@@@@@@@@@@@@");

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
    Log.d("D", "@@@@@@@@@@@@@@@@@@@@@ END PRINTING CURRENT USER (" + currentUser.name + ") RECORD @@@@@@@@@@@@@@@@@@@@@@@@");

}

public void handleRegisteredUser(){
    if(registeredUser.data.isEmpty()) return;
    Log.d("D", "!!!!!!!!!!!!!!!!!!! START HANDLE REGISTERED USER!!!!!!!!!!!!!!!!!!!!!");
    Log.d("D", "!!!!!!!!!!!!!!!!!!! START REGISTERED USER (" + registeredUser.name + ") LOCATIONS!!!!!!!!!!!!!!!!!!!!!");
    registeredUser.printData();
    Log.d("D", "!!!!!!!!!!!!!!!!!!! END REGISTERED USER (" + registeredUser.name + ") LOCATIONS!!!!!!!!!!!!!!!!!!!!!");
    Log.d("D", "!!!!!!!!!!!!!!!!!!! START CURRENT USER (" + currentUser.name + ") LOCATIONS!!!!!!!!!!!!!!!!!!!!!");
    currentUser.printData();
    Log.d("D", "!!!!!!!!!!!!!!!!!!! END CURRENT USER (" + currentUser.name + ") LOCATIONS!!!!!!!!!!!!!!!!!!!!!");

    Log.d("D", "!!!!!!!!!!!!!!!!!!! END HANDLE REGISTERED USER!!!!!!!!!!!!!!!!!!!!!");

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

        Log.d("D", "Registered user: " + registeredUser.name);
        Log.d("D", "Current user: " + currentUser.name);

        if(currentUser.name.matches(registeredUser.name)) handleCurrentUser();
        else handleRegisteredUser();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        streetViewPanoramaFragment = (SupportStreetViewPanoramaFragment) getSupportFragmentManager().findFragmentById(R.id.streetView);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
        mapFragment.getView().bringToFront();
        myLocationPlaceMap = new MyLocationPlaceMap(getApplicationContext(), MapsActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent myIntent = getIntent();

        registeredUser = (User) myIntent.getSerializableExtra("registeredUser", User.class);
        currentUser = (User) myIntent.getSerializableExtra("currentUser", User.class);

        Log.d("D", "Registered user: " + registeredUser.name);
        Log.d("D", "Current user: " + currentUser.name);
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
        if(currentUser.data.isEmpty()) return;
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
    // https://stackoverflow.com/a/35646210
    private LatLng computeCentroid(ArrayList<LatLng> points) {
        double latitude = 0;
        double longitude = 0;
        int n = points.size();

        for (LatLng point : points) {
            latitude += point.latitude;
            longitude += point.longitude;
        }

        return new LatLng(latitude/n, longitude/n);
    }
    public void handleRegisteredUser(GoogleMap mMap) {
        Log.d("D", "!!!!!!!!!!!!!!!!!!! REGISTERED USER RECORDS: " + registeredUser.data.size() + " !!!!!!!!!!!!!!!!!!!!!");
        Log.d("D", "!!!!!!!!!!!!!!!!!!! CURRENT USER RECORDS: " + currentUser.data.size() + " !!!!!!!!!!!!!!!!!!!!!");
        ArrayList<LatLng> points = new ArrayList<>();
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if(registeredUser.data.isEmpty())return;
                if(currentUser.data.isEmpty())return;
                UserFirebaseRecord latestRegisteredUserRecord = registeredUser.data.get(0);
                UserFirebaseRecord latestCurrentUserRecord = currentUser.data.get(0);

                for(UserFirebaseRecord r : registeredUser.data){
                    LatLng p = new LatLng(r.latitude, r.longitude);
                    if(r.after(latestRegisteredUserRecord)) latestRegisteredUserRecord = r;
                    points.add(p);
                    mMap.addMarker(new MarkerOptions()
                            .title("Show Surroundings")
                            .position(new LatLng(r.latitude, r.longitude))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    );
                }
                for(UserFirebaseRecord r : currentUser.data){
                    LatLng p = new LatLng(r.latitude, r.longitude);
                    if(r.after(latestCurrentUserRecord)) latestCurrentUserRecord = r;

                    points.add(p);
                    mMap.addMarker(new MarkerOptions()
                            .title("Show Surroundings")
                            .position(p)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    );
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(computeCentroid(points), 14));
                drawRoute(
                        new LatLng(latestCurrentUserRecord.latitude, latestCurrentUserRecord.longitude),
                        new LatLng(latestRegisteredUserRecord.latitude, latestRegisteredUserRecord.longitude)
                );
            }
        });

    }

    public void drawRoute(LatLng origin, LatLng destination) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + origin.latitude + "," + origin.longitude + "&destination="
                + destination.latitude + "," + destination.longitude
                + "&mode=driving&key=AIzaSyD6iQRsq-pEX4YqJEjKzdDyQcMnRryrZPM";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse the JSON response and draw the route on the map
                        PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.
                                BLACK);
                        polylineOptions.width(5);
                        JSONArray routes = null;
                        try {
                            routes = response.getJSONArray("routes");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        for (int i = 0; i < routes.length(); i++) {
                            try {
                                JSONObject route = routes.getJSONObject(i);
                                JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                                String points = overviewPolyline.getString("points");
                                List<LatLng> path = PolyUtil.
                                        decode(points);
                                polylineOptions.addAll(path);
//                                String distance = route.getJSONArray("legs")
//                                        .getJSONObject(0)
//                                        .getJSONObject("distance")
//                                        .getString("text");
//                                String duration = route.getJSONArray("legs")
//                                        .getJSONObject(0)
//                                        .getJSONObject("duration")
//                                        .getString("text");
//                                TextView textView = findViewById(R.id.mapsTextView);
//                                textView.setText("Distance: "+distance + " Duration: "+duration);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        mMap.addPolyline(polylineOptions);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                    }
                });
        RequestQueue requestQueue = Volley.
                newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
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