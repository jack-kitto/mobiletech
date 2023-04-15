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
    int showUserSelector = View.INVISIBLE;
    String selectedUserName = "";
    ArrayList<String> users;
    MyDbHelper myDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        myDbHelper = new MyDbHelper(this, "sampleassignment1", null, 1);
        RadioGroup radioGroup = findViewById(R.id.rg1);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // checkedId is the RadioButton selected
            RadioButton selectedRadioButton = findViewById(checkedId);
            selectedUserName = selectedRadioButton.getText().toString();
        });
        users = myDbHelper.getAllUsers();
        setTitle("Location Tracker");
    }

    @Override
    protected void onResume() {
        super.onResume();
        users = myDbHelper.getAllUsers();
        if(!users.isEmpty())displayUsers();
    }

    public void displayUsers(){
        EditText et1 = findViewById(R.id.et1);
        EditText et2 = findViewById(R.id.et2);
        EditText et3 = findViewById(R.id.et3);
        RadioButton rb1 = findViewById(R.id.rb1);
        RadioButton rb2 = findViewById(R.id.rb2);
        RadioButton rb3 = findViewById(R.id.rb3);

        for (String user : users) {
            Log.d("D", "USER: " + user);
            String name = myDbHelper.getNameFromUser(user);
            boolean isSelected = myDbHelper.getIsSelectedFromUser(user);
            if(isSelected) Log.d("D", "SELECTED");
            else Log.d("D", "NOT SELECTED");
            if(!et2.getText().toString().isEmpty() && et3.getText().toString().isEmpty()){
                et3.setText(name);
                rb3.setText(name);
                if(isSelected){
                    rb3.setChecked(true);
                    rb3.setSelected(true);
                }
            };
            if(!et1.getText().toString().isEmpty() && et2.getText().toString().isEmpty()){
                et2.setText(name);
                rb2.setText(name);
                if(isSelected){
                    rb2.setChecked(true);
                    rb2.setSelected(true);
                }            }
            if(et1.getText().toString().isEmpty()){
                et1.setText(name);
                rb1.setText(name);
                if(isSelected){
                    rb1.setChecked(true);
                    rb1.setSelected(true);
                }
            }
        }
        if(!usernamesAreValid()) setVisibility(View.INVISIBLE);
        else setVisibility(View.VISIBLE);
    }
    public void setVisibility(int visibility){
        TextView textView = findViewById(R.id.tv2);
        RadioGroup radioGroup = findViewById(R.id.rg1);
        Button btn2 = findViewById(R.id.btn2);
        textView.setVisibility(visibility);
        radioGroup.setVisibility(visibility);
        btn2.setVisibility(visibility);
    }
    public boolean usernamesAreValid(){
        EditText et1 = findViewById(R.id.et1);
        EditText et2 = findViewById(R.id.et2);
        EditText et3 = findViewById(R.id.et3);
        String et1Text = et1.getText().toString();
        String et2Text = et2.getText().toString();
        String et3Text = et3.getText().toString();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(et2.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(et3.getWindowToken(), 0);

        if(et1Text.isEmpty() || et2Text.isEmpty() || et3Text.isEmpty()) return false;
        return true;
    }
    public void save(View view) {
        EditText et1 = findViewById(R.id.et1);
        EditText et2 = findViewById(R.id.et2);
        EditText et3 = findViewById(R.id.et3);
        RadioButton rb1 = findViewById(R.id.rb1);
        RadioButton rb2 = findViewById(R.id.rb2);
        RadioButton rb3 = findViewById(R.id.rb3);
        String user1 = et1.getText().toString();
        String user2 = et2.getText().toString();
        String user3 = et3.getText().toString();

        if(usernamesAreValid()) myDbHelper.deleteAllUsers();
        if(showUserSelector == View.INVISIBLE)showUserSelector = View.VISIBLE;
        else showUserSelector = View.INVISIBLE;
        rb1.setText(user1);
        rb2.setText(user2);
        rb3.setText(user3);

        myDbHelper.insertUser(user1, "0");
        myDbHelper.insertUser(user2, "0");
        myDbHelper.insertUser(user3, "0");
        users = myDbHelper.getAllUsers();

        if(!usernamesAreValid()) {
            setVisibility(View.INVISIBLE);
            return;
        }
        setVisibility(View.VISIBLE);
    }
    public void logAllUsers(){
        for (String user: myDbHelper.getAllUsers()) {
            Log.d("USER", user);
        }
    }

    public void start(View view){
        if(selectedUserName.isEmpty() | !usernamesAreValid()) return;

        String newSelectedUser = "";

        for (String user : users) {
            String name = myDbHelper.getNameFromUser(user);
            String id = myDbHelper.getIdFromUser(user);
            if(name.matches(selectedUserName)) newSelectedUser = user;
            else myDbHelper.updateUser(id, name, "0");
        };
        String id = myDbHelper.getIdFromUser(newSelectedUser);
        String name = myDbHelper.getNameFromUser(newSelectedUser);
        myDbHelper.updateUser(id, name, "1");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

