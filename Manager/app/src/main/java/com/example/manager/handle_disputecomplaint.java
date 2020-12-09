package com.example.manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

public class handle_disputecomplaint extends AppCompatActivity {

    private ArrayList<model_handledispute> models;
    RecyclerView mrecyclerview;
    private Adapter_handledispute myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_disputecomplaint);

        mrecyclerview = findViewById(R.id.recycle_handledispute);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));

        models = new ArrayList<>();
        getapplycation();

    }

    public void getapplycation() {

        String url = getString(R.string.base_url) + "/get_dispute_complaint";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID","-1");
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String respond = response.body().string();
                handle_disputecomplaint.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONArray jarry = new JSONArray(respond);
                            for (int i = 0; i < jarry.length(); i++) {
                                JSONObject object1 = jarry.getJSONObject(i);
                                String backid = object1.getString("_id");
                                String userid = "Dispute By: " + object1.getString("userID");
                                String complaint = "User complaint: "+object1.getString("complaintContext");
                                String dispute = "Dispute Context: "+object1.getString("context");
                                models.add(new model_handledispute(userid,backid,complaint,dispute));
                            }
                            myadapter = new Adapter_handledispute(handle_disputecomplaint.this,models);
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