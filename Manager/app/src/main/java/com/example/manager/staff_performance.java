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

public class staff_performance extends AppCompatActivity {
    private ArrayList<model_performance>models;
    RecyclerView myrecycleview;
    private Adapter_performance myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_performance);

        myrecycleview = findViewById(R.id.recyclePer);
        myrecycleview.setLayoutManager(new LinearLayoutManager(this));

        models = new ArrayList<>();
        getperfomance();


    }

    public void getperfomance() {

        String url = getString(R.string.base_url) + "/performance";
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
                staff_performance.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONArray jarray = new JSONArray(respond);
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject object1 = jarray.getJSONObject(i);
                                String userid = "User ID: "+object1.getString("userID");
                                String complaints = "Received complaints:  *"+object1.getString("complaints");
                                String compliments = "Received compliments:  *"+object1.getString("compliments");
                                String promoted = "Received Peomotes:  *"+object1.getString("promoted");
                                String demoted = "Received Demotes:  *"+object1.getString("demoted");
                                String backid = object1.getString("userID");
                                models.add(new model_performance(userid,complaints,compliments,demoted,promoted,backid));
                            }
                            myadapter = new Adapter_performance(staff_performance.this,models);
                            myrecycleview.setAdapter(myadapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


}