package com.example.chef;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {


    private Button btn_pickedOrder;
    private Button btn_IncOrder;
    private Button btn_complaint;
    private Button btn_account;
    private Button btn_menu;
    private Button btn_logOut;
    private Button btn_compliment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_pickedOrder = (Button)findViewById(R.id.btn_pickedOrder);
        btn_IncOrder = (Button)findViewById(R.id.btn_incOrder);
        btn_complaint = (Button)findViewById(R.id.btn_complaint);
        btn_account = (Button)findViewById(R.id.btn_acc);
        btn_menu = (Button)findViewById(R.id.btn_Menu);
        btn_logOut = (Button)findViewById(R.id.btn_LogOut);
        btn_compliment = (Button)findViewById(R.id.btn_compliment);


        btn_IncOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm1 = getSupportFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();

                ft1.replace(R.id.main_layout1, new frag1_uncomplete());
                ft1.commit();
            }
        });
        btn_pickedOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm2 = getSupportFragmentManager();
                FragmentTransaction ft2 = fm2.beginTransaction();
                ft2.replace(R.id.main_layout1, new frag2_completeord());
                ft2.commit();
            }
        });
        btn_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm3 = getSupportFragmentManager();
                FragmentTransaction ft3 = fm3.beginTransaction();
                ft3.replace(R.id.main_layout1, new frag3_viewcomplaint());
                ft3.commit();
            }
        });
        btn_compliment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm4 = getSupportFragmentManager();
                FragmentTransaction ft4 = fm4.beginTransaction();
                ft4.replace(R.id.main_layout1, new frag4_viewcompliment());
                ft4.commit();
            }
        });


        //Click account to account page
        btn_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, chef_account.class);
                startActivity(intent);
            }
        });

        //click menu to menu page
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, chef_menu.class);
                startActivity(intent);
            }
        });

        //Clcik Log out to log out pop up.
        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), logout.class);
                startActivity(i);
            }
        });



    }


}