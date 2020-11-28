package com.example.simplerestaurant;

import android.util.Patterns;

import okhttp3.OkHttpClient;

public class UnitTools {

    private static OkHttpClient client;

    public static boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static OkHttpClient getOkHttpClient(){
        if(null == client){
            client = new OkHttpClient();
        }
        return client;
    }
}
