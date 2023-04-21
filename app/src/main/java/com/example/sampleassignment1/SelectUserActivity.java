package com.example.sampleassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectUserActivity extends AppCompatActivity {
    String selectedUserName = "";
    Users users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        createUsers();


        setTitle("Location Tracker");
    }

    public void createUsers(){
        Log.d("D", "CREATING USERS @#$@!#$@#$");
        EditText et1 = findViewById(R.id.et1);
        EditText et2 = findViewById(R.id.et2);
        EditText et3 = findViewById(R.id.et3);
        RadioButton rb1 = findViewById(R.id.rb1);
        RadioButton rb2 = findViewById(R.id.rb2);
        RadioButton rb3 = findViewById(R.id.rb3);
        RadioGroup rg = findViewById(R.id.rg1);
        users = new Users(this, et1, et2, et3, rb1, rb2, rb3, rg);
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = findViewById(checkedId);
            selectedUserName = selectedRadioButton.getText().toString();
            users.setSelectedUser(selectedUserName);
        });
        Boolean error = users.load();
        if(error){
            Log.d("D", "Error loading users!!!!!!!!!!!!");
            return;
        };
        users.print();
        this.users.display();
        setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createUsers();
    }

    public void setVisibility(int visibility){
        TextView textView = findViewById(R.id.tv2);
        RadioGroup radioGroup = findViewById(R.id.rg1);
        Button btn2 = findViewById(R.id.btn2);
        textView.setVisibility(visibility);
        radioGroup.setVisibility(visibility);
        btn2.setVisibility(visibility);
    }

    public void save(View view) {
        users.save();
        users.updateRadioButtons();
        if(!users.valid()) {
            setVisibility(View.INVISIBLE);
            return;
        }
        setVisibility(View.VISIBLE);
    }

    public void start(View view){
        if(selectedUserName.isEmpty() | !users.valid()) return;
        users.print();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

