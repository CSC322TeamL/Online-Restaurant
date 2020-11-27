package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserMainPage extends BaseActivity {

    private TextView textUserName;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_page);

        Intent intent = getIntent();
        userType = intent.getStringExtra("UserType");

        textUserName = (TextView) findViewById(R.id.textview_user);

        textUserName.setText("Welcome!\n" + userType);
    }

}
