package com.example.sampleassignment1;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public String id;
    public String name;
    public String selected;
    public ArrayList<UserFirebaseRecord> data;

    public User(String id, String name, String selected) {
        this.id = id;
        this.name = name;
        this.selected = selected;
        this.data = new ArrayList<>();
    }

    public void printData() {
        for (UserFirebaseRecord data : data) data.print();
    }

    public void add(UserFirebaseRecord userFirebaseRecord) {
        if (this.data.contains(userFirebaseRecord)) return;
        for(UserFirebaseRecord record: this.data){
            if(record.longitude.equals(userFirebaseRecord.longitude)) return;
            if(record.latitude.equals(userFirebaseRecord.latitude)) return;
        }
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        String key_ = dbRef.push().getKey(); // to generate a random key
        dbRef.child(this.name).child(key_).setValue(userFirebaseRecord);

        this.data.add(userFirebaseRecord);
    }
}
