package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class DeliveryMainPageActivity extends DeliveryBaseActivity {
    final private static String SENDING_TAG = "fragment_delivery_sending";
    final private static String WAITING_TAG = "fragment_delivery_waiting";
    final private static String FINISHED_TAG = "fragment_delivery_finished";
    final private static String ACC_TAG = "fragment_user_account";

    private String userID, userType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_main_page);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userType = intent.getStringExtra("userType");
    }

    private void getOrdersFromServer(){
        String url = getString(R.string.base_url) + "/get_order";
    }


}
