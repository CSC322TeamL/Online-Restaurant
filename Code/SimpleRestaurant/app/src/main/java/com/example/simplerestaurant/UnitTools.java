package com.example.simplerestaurant;

import android.util.Patterns;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class UnitTools {

    private static OkHttpClient client;
    private static Gson gson;

    public final static int REQUEST_USER_ORDER_CART = 11;
    public final static int REQUEST_USER_INFO = 21;

    public final static String TYPE_JSON = "application/json";

    public static boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static OkHttpClient getOkHttpClient(){
        if(null == client){
            client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        }
        return client;
    }

    public static Gson getGson(){
        if(null == gson){
            gson = new Gson();
        }
        return gson;
    }
}
