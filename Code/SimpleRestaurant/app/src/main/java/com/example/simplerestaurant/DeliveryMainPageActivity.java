package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.simplerestaurant.beans.DeliveryAllOrder;
import com.example.simplerestaurant.beans.OrderBean;
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

public class DeliveryMainPageActivity extends DeliveryBaseActivity {
    final private static String SENDING_TAG = "fragment_delivery_sending";
    final private static String WAITING_TAG = "fragment_delivery_waiting";
    final private static String FINISHED_TAG = "fragment_delivery_finished";
    final private static String ACC_TAG = "fragment_user_account";

    private String userID, userType;
    private OkHttpClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_main_page);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userType = intent.getStringExtra("userType");

        client = UnitTools.getOkHttpClient();

        getOrdersFromServer();
        getPreparedOrdersFromServer();
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

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


}
