package com.example.chef;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class logout extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        Button btn_chef_logout_btn;
        btn_chef_logout_btn = (Button)findViewById(R.id.chef_logout_btn);
        btn_chef_logout_btn.setOnClickListener((v) -> {
            finish();
            System.exit(0);
        });

    }
}