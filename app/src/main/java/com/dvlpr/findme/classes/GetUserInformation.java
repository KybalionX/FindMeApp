package com.dvlpr.findme.classes;

import android.content.Context;
import android.content.SharedPreferences;

public class GetUserInformation {

    Context context;

    public GetUserInformation(Context context) {
        this.context = context;
    }

    public int getUserID(){
        SharedPreferences shared = context.getSharedPreferences("preferences", context.MODE_PRIVATE);
        int channel = (shared.getInt("UserID", 0));
        return channel;
    }

    public String getImageURL(){
        SharedPreferences shared = context.getSharedPreferences("preferences", context.MODE_PRIVATE);
        String imagen = shared.getString("ImageURL", "");
        return imagen;
    }

}
