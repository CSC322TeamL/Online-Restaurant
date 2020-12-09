package com.example.manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    String userid = getuserid.userid;

    private Button btn_handle;
    private Button btn_logout;
    private Button btn_comp_comp;
    private Button btn_application;
    private Button btn_create_account;
    private Button btn_taboolist;
    private Button btn_perfomance;
    private Button btn_degegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_create_account = (Button)findViewById(R.id.btn_createAccount);
        btn_logout = (Button)findViewById(R.id.log_out);
        btn_comp_comp = (Button)findViewById(R.id.btn_complaints_Compliments);
        btn_application = (Button)findViewById(R.id.btn_application);
        btn_taboolist = (Button)findViewById(R.id.Taboolist);
        btn_perfomance = (Button)findViewById(R.id.Performance);
        btn_degegister = (Button)findViewById(R.id.de_register);
        btn_handle = (Button)findViewById(R.id.btn_handlddispute);


        btn_taboolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this, taboo_page.class);
                startActivity(i);
            }
        });

        btn_degegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this, de_register.class);
                startActivity(i);
            }
        });

        btn_handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this, handle_disputecomplaint.class);
                startActivity(i);
            }
        });


        btn_perfomance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this, staff_performance.class);
                startActivity(i);
            }
        });



        btn_comp_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this, complaints_and_compliments.class);
                startActivity(i);
            }
        });

        btn_application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this, application.class);
                startActivity(i);
            }
        });

        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this, create_account.class);
                startActivity(i);
            }
        });



    }
}