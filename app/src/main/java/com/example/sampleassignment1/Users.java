package com.example.sampleassignment1;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Users implements Serializable{
    ArrayList<User> users;
    User selectedUser;
    String SELECTED = "1";
    String NOT_SELECTED = "0";
    MyDbHelper myDbHelper;
    RadioGroup rg;
    ArrayList<EditText> ets;
    ArrayList<RadioButton> rbs;

    public Users(Context context, EditText et1, EditText et2, EditText et3, RadioButton rb1, RadioButton rb2, RadioButton rb3, RadioGroup rg){
        this.users = new ArrayList<>();
        this.selectedUser = null;
        this.rg = rg;
        ets = new ArrayList<>();
        ets.add(et1);
        ets.add(et2);
        ets.add(et3);

        rbs = new ArrayList<>();
        rbs.add(rb1);
        rbs.add(rb2);
        rbs.add(rb3);

        this.myDbHelper = new MyDbHelper(context, "sampleassignment1", null, 1);
    }

    public Users(Context context) {
        this.users = new ArrayList<>();
        this.selectedUser = null;
        this.SELECTED = "1";
        this.myDbHelper = new MyDbHelper(context, "sampleassignment1", null, 1);
        this.rg = null;
        this.ets = null;
        this.rbs = null;
        boolean error = this.load();
    }

    public void setSelectedUser(String selectedUserName){
        for(User user : this.users) {
            if(user.name.matches(selectedUserName)){
                this.selectedUser = user;
                this.selectedUser.selected = SELECTED;
            }else{
                user.selected = NOT_SELECTED;
            }
        }
    }
    public void save(){
        if(!this.valid())return;
        this.myDbHelper.deleteAllUsers();
        this.users.clear();
        for(int i = 0; i < 3; i++){
            User u = new User(
                    String.valueOf(i),
                    this.getName(i),
                    this.NOT_SELECTED);
            this.users.add(u);
            this.myDbHelper.insertUser(u.name, u.selected);
        }
    }

    public boolean load(){
        ArrayList<String> Users = new ArrayList<>();
        try{
            Users = this.myDbHelper.getAllUsers();
        }catch (Exception e){
            return true;
        }
        if(Users.isEmpty()) return true;
        this.users.clear();
        for(String s : Users){
            User u = new User(
                    this.myDbHelper.getIdFromUser(s),
                    this.myDbHelper.getNameFromUser(s),
                    this.myDbHelper.getSelectedFromUser(s)
            );
            this.users.add(u);
            if(this.myDbHelper.getIsSelectedFromUser(s)) this.selectedUser = u;
        }
        return false;

    }

    public void updateRadioButtons(){
        for(int i = 0; i < 3; i++) {
            this.rbs.get(i).setText(this.users.get(i).name);
        }
    }
    public String getName(int user){
        for(int i = 0; i < 3; i++){
            if(user == i) return this.ets.get(i).getText().toString();
        }
        return null;
    }

    public User createUser(String name){
        return new User("", name, NOT_SELECTED);
    }

    public boolean isEmpty() {
        if(users.isEmpty()) return true;
        else return false;
    }

    public void display() {
        Log.d("D", "DISPLAYING USERS");
        this.updateRadioButtons();
        for(int i = 0; i < 3; i++){
            ets.get(i).setText(users.get(i).name);
            if(users.get(i).selected == SELECTED){
                Log.d("D", "#!#!#!#!#!#!#!# SELECTED USER: " + users.get(i).name);
                rbs.get(i).setChecked(true);
                rbs.get(i).setSelected(true);
            }
        }
    }

    public void print(){
        Log.d("D", "################# PRINTING USERS ##############");
        if(this.selectedUser != null){
            Log.d("D", "SELECTED USER: ");
            this.selectedUser.print();
            Log.d("D", "All users:");
        }

        for(User user : this.users){
            user.print();
            Log.d("D", "- - - - - - - - - - -");
        }
        Log.d("D", "################# END PRINTING USERS ###########");

    }

    public boolean valid(){
        for(int i = 0; i < 3; i++){
            if(this.ets.get(i).getText().toString().isEmpty()) return false;
        }
        return true;
    }
}
