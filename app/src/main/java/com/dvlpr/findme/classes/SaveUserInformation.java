package com.dvlpr.findme.classes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SaveUserInformation {

    Context context;

    public SaveUserInformation(Context context) {
        this.context = context;
    }

    public void SaveUserID(int UserID){
        SharedPreferences prefs = context.getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("UserID", UserID);
        editor.commit();
    }

    public void SaveUserImage(String ImageURL){
        SharedPreferences prefs = context.getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ImageURL", ImageURL);
        editor.commit();
    }

    public void SaveUserLastLocation(String LastLocation){
        SharedPreferences prefs = context.getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("UserLastLocation", LastLocation);
        editor.commit();
    }

}
