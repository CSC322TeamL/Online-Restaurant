package com.example.simplerestaurant;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import okhttp3.OkHttpClient;

public class BaseActivity extends AppCompatActivity {

    protected OkHttpClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {}
    }

    protected void hideInputMethod(View mainBackground){
        mainBackground.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(null != BaseActivity.this.getCurrentFocus()){
                    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    return manager.hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    protected OkHttpClient getClient(){
        if(null == client){
            client = new OkHttpClient();
        }
        return client;
    }

    protected void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
