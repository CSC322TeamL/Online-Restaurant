package com.example.manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.JsonArray;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import java.util.ArrayList;

public class de_register extends AppCompatActivity {

    private ArrayList<model_deregister> models;
    RecyclerView mrecyclerview;
    private Adapter_deregister myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_de_register);

        mrecyclerview = findViewById(R.id.recycle_de_register);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));

        models = new ArrayList<>();
        getapplycation();

    }


    public void getapplycation() {

        String url = getString(R.string.base_url) + "/de-registration";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();

        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String respond = response.body().string();
                de_register.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jarray = new JSONArray(respond);
                            for(int i=0;i<jarray.length();i++){
                                JSONObject object = jarray.getJSONObject(i);
                                String userid = "User ID: "+object.getString("userID");
                                String spent = "Spent total: "+object.getString("spent");
                                String warning ="Received warning: *"+ object.getString("warnings");
                                String balance = "Account Balance: "+object.getString("balance");
                                String backid = object.getString("userID");
                                models.add(new model_deregister(userid,spent,balance,warning,backid));
                            }
                            myadapter = new Adapter_deregister(de_register.this,models);
                            mrecyclerview.setAdapter(myadapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}