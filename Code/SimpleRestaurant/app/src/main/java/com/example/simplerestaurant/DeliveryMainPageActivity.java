package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.simplerestaurant.Fragments.DeliveryAccountFragment;
import com.example.simplerestaurant.Fragments.DeliveryFinishedFragment;
import com.example.simplerestaurant.Fragments.DeliverySendingFragment;
import com.example.simplerestaurant.Fragments.DeliveryWaitingFragment;
import com.example.simplerestaurant.Interfaces.DeliveryActionInterface;
import com.example.simplerestaurant.beans.DeliveryAllOrder;
import com.example.simplerestaurant.beans.OrderBean;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeliveryMainPageActivity extends DeliveryBaseActivity implements DeliveryActionInterface {
    final private static String SENDING_TAG = "fragment_delivery_sending";
    final private static String WAITING_TAG = "fragment_delivery_waiting";
    final private static String FINISHED_TAG = "fragment_delivery_finished";
    final private static String ACC_TAG = "fragment_user_account";

    private String userID, userType;
    private OkHttpClient client;

    private BottomNavigationView navigationMenu;

    private DeliverySendingFragment sendingFragment;
    private DeliveryWaitingFragment waitingFragment;
    private DeliveryFinishedFragment finishedFragment;
    private DeliveryAccountFragment accountFragment;

    private Fragment activeFragment;
    private FragmentManager fragmentManager;

    private int currentSelection = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_main_page);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userType = intent.getStringExtra("userType");

        client = UnitTools.getOkHttpClient();
        fragmentManager = getSupportFragmentManager();

        navigationMenu = (BottomNavigationView) findViewById(R.id.bottomNavigation_delivery_main);
        navigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setCurrentFragment(item.getItemId());
                return true;
            }
        });

        currentSelection = DeliveryDataStore.TYPE_SENDING;
        sendingFragment = (DeliverySendingFragment) findFragmentByTag(SENDING_TAG);
        activeFragment = sendingFragment;
        setCurrentFragment(R.id.nav_delivery_main_sending);

    }

    private void setCurrentFragment(int navID){
        switch (navID){
            case R.id.nav_delivery_main_sending:
                if(null == sendingFragment){
                    sendingFragment = (DeliverySendingFragment) findFragmentByTag(SENDING_TAG);
                }
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(sendingFragment)
                        .commit();
                activeFragment = sendingFragment;
                break;
            case R.id.nav_delivery_main_waiting:
                if(null == waitingFragment){
                    waitingFragment = (DeliveryWaitingFragment) findFragmentByTag(WAITING_TAG);
                }
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(waitingFragment)
                        .commit();
                activeFragment = waitingFragment;
                break;
            case R.id.nav_delivery_main_finished:
                if(null == finishedFragment){
                    finishedFragment = (DeliveryFinishedFragment) findFragmentByTag(FINISHED_TAG);
                }
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(finishedFragment)
                        .commit();
                activeFragment = finishedFragment;
                break;
            case R.id.nav_delivery_main_me:
                if(null == accountFragment){
                    accountFragment = (DeliveryAccountFragment) findFragmentByTag(ACC_TAG);
                }
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(accountFragment)
                        .commit();
                activeFragment= accountFragment;
                break;
        }
    }

    private Fragment findFragmentByTag(String tag){
        Fragment result = (Fragment) fragmentManager.findFragmentByTag(tag);
        if(null == result){
            switch (tag){
                case SENDING_TAG:
                    if(null == sendingFragment){
                        sendingFragment = new DeliverySendingFragment(this);
                        if(!sendingFragment.isAdded()){
                            fragmentManager.beginTransaction()
                                    .add(R.id.view_delivery_main_for_replace, sendingFragment, tag)
                                    .hide(sendingFragment)
                                    .commit();
                        }
                        return sendingFragment;
                    }
                    break;
                case WAITING_TAG:
                    if(null == waitingFragment){
                        waitingFragment = new DeliveryWaitingFragment(this);
                        if(!waitingFragment.isAdded()){
                            fragmentManager.beginTransaction()
                                    .add(R.id.view_delivery_main_for_replace, waitingFragment)
                                    .hide(waitingFragment)
                                    .commit();
                        }
                        return waitingFragment;
                    }
                    break;
                case FINISHED_TAG:
                    if(null == finishedFragment){
                        finishedFragment = new DeliveryFinishedFragment(this);
                        if(!finishedFragment.isAdded()){
                            fragmentManager.beginTransaction()
                                    .add(R.id.view_delivery_main_for_replace, finishedFragment)
                                    .hide(finishedFragment)
                                    .commit();
                        }
                        return finishedFragment;
                    }
                    break;
                case ACC_TAG:
                    if(null == accountFragment){
                        accountFragment = new DeliveryAccountFragment(userID, userType);
                        if(!accountFragment.isAdded()){
                            fragmentManager.beginTransaction()
                                    .add(R.id.view_delivery_main_for_replace, accountFragment)
                                    .hide(accountFragment)
                                    .commit();
                        }
                        return accountFragment;
                    }
                    break;
            }
        }
        return result;
    }

    private void orderResponse(String res){
        try {
            JSONObject jsonObject = new JSONObject(res);
            DeliveryAllOrder orders = UnitTools.getGson()
                    .fromJson(jsonObject.getString("result").toString(), DeliveryAllOrder.class);
            store.resetOrderSending(orders.getOrderPicked());
            Log.i("order", store.getOrderSending().toString());
            store.resetOrderFinished(orders.getOrderDelivered());
            Log.i("order", store.getOrderFinished().toString());
            if(null != sendingFragment && currentSelection == DeliveryDataStore.TYPE_SENDING){
                sendingFragment.notifyDataChange();
            }
            if(null != finishedFragment && currentSelection == DeliveryDataStore.TYPE_FINISHED){
                finishedFragment.notifyDataChange();
            }
            if(currentSelection == DeliveryDataStore.TYPE_BOTH){
                if(null != sendingFragment){
                    sendingFragment.notifyDataChange();
                }
                if(null != finishedFragment){
                    finishedFragment.notifyDataChange();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void preparedOrderResponse(String res){
        try{
            JSONObject jsonObject = new JSONObject(res);
            JSONArray array = jsonObject.getJSONObject("result").getJSONArray("prepared");
            Type listType = new TypeToken<ArrayList<OrderBean>>(){}.getType();
            ArrayList<OrderBean> preparedList = UnitTools.getGson().fromJson(array.toString(), listType);
            store.resetOrderWaiting(preparedList);
            Log.i("order", store.getOrderWaiting().toString());
            if(null != waitingFragment){
                waitingFragment.notifyDataChange();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void submitDeliveredResponse(String res){
        if(res.equals("0")){
            toastMessage("Order Delivered");
            currentSelection = DeliveryDataStore.TYPE_BOTH;
            getOrdersFromServer();
        }
    }

    private void submitPickUpResponse(String res){
        if(res.equals("0")){
            toastMessage("Order Picked Up");
            currentSelection = DeliveryDataStore.TYPE_SENDING;
            getOrdersFromServer();
            getPreparedOrdersFromServer();
        }
    }

    /**
     * get the order that is ready to be delivered from server
     */
    private void getPreparedOrdersFromServer(){
        String url = getString(R.string.base_url) + "/uncompleted_order";
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", userID);
        bodyBuilder.add("role", userType);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Failed to connect server");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string().trim();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        preparedOrderResponse(res);
                    }
                });
            }
        });
    }

    /**
     * get the order that is delivering and has finished from server
     */
    private void getOrdersFromServer(){
        String url = getString(R.string.base_url) + "/get_order";
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", userID);
        bodyBuilder.add("role", userType);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Failed to connect server");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string().trim();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        orderResponse(res);
                    }
                });
            }
        });
    }


    private void submitDelivered2Server(String userID, String orderID){
        String url = getString(R.string.base_url) + "/order_delivered";
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", userID);
        bodyBuilder.add("orderID", orderID);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Failed to connect server");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string().trim();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        submitDeliveredResponse(res);
                    }
                });
            }
        });
    }

    private void submitPickUp2Server(String userID, String userType, String orderID){
        String url = getString(R.string.base_url) + "/pick_order";
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", userID);
        bodyBuilder.add("orderID", orderID);
        bodyBuilder.add("role", userType);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Failed to connect server");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string().trim();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        submitPickUpResponse(res);
                    }
                });
            }
        });
    }

    @Override
    public void onDeliveredClick(String orderID) {
        submitDelivered2Server(userID, orderID);
    }

    @Override
    public void onPickUpClick(String orderID) {
        submitPickUp2Server(userID, userType, orderID);
    }

    @Override
    public void onComplaintClick(String orderID) {
        OrderBean order = store.getOrderFromFinished(orderID);
        if(null == order){
            return;
        }
        Intent intent = new Intent(this, ComplaintComplimentActivity.class);
        intent.putExtra("isComplaint", "true");
        intent.putExtra("subjectID", order.getCustomerID());
        intent.putExtra("subjectTo", "Customer");
        intent.putExtra("userID", userID);
        intent.putExtra("userType", userType);
        intent.putExtra("orderID", orderID);
        startActivity(intent);
    }

    @Override
    public void onSendingFragmentRefresh() {
        currentSelection = DeliveryDataStore.TYPE_SENDING;
        getOrdersFromServer();
    }

    @Override
    public void onWaitingFragmentRefresh() {
        currentSelection = DeliveryDataStore.TYPE_WAITING;
        getPreparedOrdersFromServer();
    }

    @Override
    public void onFinishedFragmentRefresh() {
        currentSelection = DeliveryDataStore.TYPE_FINISHED;
        getOrdersFromServer();
    }
}
