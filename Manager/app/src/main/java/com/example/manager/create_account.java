package com.example.manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class create_account extends AppCompatActivity {


    Button btn_chef;
    Button btn_deliv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        btn_chef = (Button)findViewById(R.id.btn_chefaccount);
        btn_deliv = (Button)findViewById(R.id.btn_deliveryaccount);

        btn_chef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(create_account.this, createChefAccount.class);
                startActivity(intent);
            }
        });

        btn_deliv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(create_account.this, createDeliveryAccount.class);
                startActivity(intent);
            }
        });


    }
}