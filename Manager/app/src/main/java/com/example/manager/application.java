package com.example.manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

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

public class application extends AppCompatActivity {

    private ArrayList<modelApplication> models;
    RecyclerView mrecyclerview;
    private AdapterApplication myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        mrecyclerview = findViewById(R.id.application_recycle);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));

        models = new ArrayList<>();

        getapplycation();

    }

    public void getapplycation() {

        String url = getString(R.string.base_url) + "/get_NewCustomerRequest";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();

        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String respond = response.body().string();
                application.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject json = new JSONObject(respond);
                            JSONObject json2 = json.getJSONObject("result");
                            JSONArray jarry1 = json2.getJSONArray("waiting");
                            for (int i = 0; i < jarry1.length(); i++) {
                                JSONObject object1 = jarry1.getJSONObject(i);
                                String email = object1.getString("requesterEmail");
                                String context = object1.getString("context");
                                String caseid = object1.getString("_id");
                                models.add(new modelApplication(email,context,caseid));
                            }
                            myadapter = new AdapterApplication(application.this,models);
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