package com.example.simplerestaurant;

import android.util.Patterns;

public class UnitTools {

    public static boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
