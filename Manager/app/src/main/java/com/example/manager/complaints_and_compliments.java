package com.example.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class complaints_and_compliments extends AppCompatActivity {
    private Button btn_cpmplaint;
    private Button btn_compliments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints_and_compliments);

        btn_cpmplaint = (Button)findViewById(R.id.btn_complaint);
        btn_compliments = (Button)findViewById(R.id.btn_Compliments);

        btn_cpmplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(complaints_and_compliments.this, complaint_detail.class);
                startActivity(i);
            }
        });

        btn_compliments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(complaints_and_compliments.this, compliment_detail.class);
                startActivity(i);
            }
        });

    }



}