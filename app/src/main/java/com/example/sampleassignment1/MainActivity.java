package com.example.sampleassignment1;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    MyLocationPlaceMap myLocationPlaceMap;
    ArrayList<MyLocationPlace> myLocations = new ArrayList<>();
    MyLocationPlace myLocation;
//    ArrayList<User> users;
    MyDbHelper myDbHelper;
    ArrayList<String> db_users;

    User currentUser;

    DatabaseReference user1Ref;
    DatabaseReference user2Ref;
    DatabaseReference user3Ref;
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        users = new Users(this);
//        users.print();
//        myLocationPlaceMap = new MyLocationPlaceMap(getApplicationContext(), MainActivity.this);
//        myLocationPlaceMap.requestPermissions();
//        myLocationPlaceMap.getLatLngAddress(myLocations);
//        myDbHelper = new MyDbHelper(this, "sampleassignment1", null, 1);
//        Button buttonWhereAmI = findViewById(R.id.buttonWhereAmI);
//        Button buttonWhereIsUser2 = findViewById(R.id.buttonWhereIsUser2);
//        Button buttonWhereIsUser3 = findViewById(R.id.buttonWhereIsUser3);
//        db_users = myDbHelper.getAllUsers();
//        users = new ArrayList<>();
//
//        boolean isUser2Set = false;
//        for(int i = 0; i < 3; i++) {
//            User user = new User(
//                myDbHelper.getIdFromUser(db_users.get(i)),
//                myDbHelper.getNameFromUser(db_users.get(i)),
//                myDbHelper.getSelectedFromUser(db_users.get(i))
//            );
//            users.add(user);
////            downloadClassInstanceFromRealtimeDB(user.name);
//            if(user.selected.matches("1")){
//                currentUser = user;
//                buttonWhereAmI.setText("WHERE AM I? (" + user.name + ")");
//            }
//            else if(!isUser2Set){
//                buttonWhereIsUser2.setText("WHERE IS " + user.name);
//                isUser2Set = true;
//            }else buttonWhereIsUser3.setText("WHERE IS " + user.name);
//        }
//        String user1 = users.get(0).name;
//        String user2 = users.get(1).name;
//        String user3 = users.get(2).name;
//        user1Ref = FirebaseDatabase.getInstance().getReference(user1);
//        user2Ref = FirebaseDatabase.getInstance().getReference(user2);
//        user3Ref = FirebaseDatabase.getInstance().getReference(user3);
//        user1Ref.addChildEventListener(childEventListener);
//        user2Ref.addChildEventListener(childEventListener);
//        user3Ref.addChildEventListener(childEventListener);


    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        myLocationPlaceMap.getLatLngAddress(myLocations);
//        String user1 = users.get(0).name;
//        String user2 = users.get(1).name;
//        String user3 = users.get(2).name;
//        user1Ref = FirebaseDatabase.getInstance().getReference(user1);
//        user2Ref = FirebaseDatabase.getInstance().getReference(user2);
//        user3Ref = FirebaseDatabase.getInstance().getReference(user3);
//        user1Ref.addChildEventListener(childEventListener);
//        user2Ref.addChildEventListener(childEventListener);
//        user3Ref.addChildEventListener(childEventListener);
//
//
//    }
//
//    public void whereAmI (View view) throws InterruptedException {
//        Log.d("D", "WHERE AM I");
//        int userIndex = 0;
//        User user = users.get(userIndex);
//        myLocationPlaceMap.getLatLngAddress(myLocations);
//
//        if (myLocations.size() <= 0) return;
//
//        myLocation = myLocations.get(0);
//        myLocations.clear();
//
//        Double lat = myLocation.getLatitude();
//        Double lon = myLocation.getLongitude();
//        String add = myLocation.getAddress();
//        Date date = new Date();
//        UserFirebaseRecord userFirebaseRecord = new UserFirebaseRecord(
//                lat,
//                lon,
//                add,
//                date
//        );
//        user.add(userFirebaseRecord);
//        Intent intent = new Intent(this, MapsActivity.class);
//        intent.putExtra("registeredUser", user);
//        intent.putExtra("currentUser", currentUser);
//
//        intent.putExtra("userIndex", 0);
//        intent.putExtra("lat", myLocation.getLatitude());
//        intent.putExtra("lng", myLocation.getLongitude());
//        intent.putExtra("addr", myLocation.getAddress());
//        startActivity(intent);
//    }
//
//    public void whereIsUser2(View view){
//        int userIndex = 1;
//        User user = users.get(userIndex);
//        Intent intent = new Intent(this, MapsActivity.class);
//        intent.putExtra("registeredUser", user);
//        intent.putExtra("currentUser", currentUser);
//        startActivity(intent);
//    }
//    public void whereIsUser3(View view){
//        int userIndex = 2;
//        User user = users.get(userIndex);
//        Intent intent = new Intent(this, MapsActivity.class);
//        intent.putExtra("registeredUser", user);
//        intent.putExtra("currentUser", currentUser);
//        startActivity(intent);
//    }
//
//    ValueEventListener valueEventListener = new ValueEventListener() {
//    @Override
//    public void onDataChange(@NonNull DataSnapshot snapshot) {
//        for (DataSnapshot userFirebaseRecordSnapshot : snapshot.getChildren()){
//            UserFirebaseRecord userFirebaseRecord = userFirebaseRecordSnapshot.getValue(UserFirebaseRecord.class);
//            String userName = userFirebaseRecordSnapshot.getRef().getParent().getKey();
//            for(User user : users){
//                if(user.name.matches(userName)){
//                    user.add(userFirebaseRecord);
//                    break;
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError error) {
//
//    }
//};
//    ChildEventListener childEventListener = new ChildEventListener() {
//    @Override
//    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
//        UserFirebaseRecord userFirebaseRecord = dataSnapshot.getValue(UserFirebaseRecord.class);
//        String userName = dataSnapshot.getRef().getParent().getKey();
//        for(User user : users){
//            if(user.name.matches(userName)){
//                user.add(userFirebaseRecord);
//                break;
//            }
//        }
//    }
//
//    @Override
//    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//    }
//
//    @Override
//    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//    }
//
//    @Override
//    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//    }
//
//    @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//
//    };
}

