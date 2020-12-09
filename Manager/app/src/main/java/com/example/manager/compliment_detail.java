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

public class compliment_detail extends AppCompatActivity {

    private ArrayList<model> models;
    RecyclerView mrecyclerview;
    private AdapterComplaint myadapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail);
        mrecyclerview = findViewById(R.id.recycle_complaint);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));

        models = new ArrayList<>();
        getcomplaint();


    }





    public void getcomplaint() {

        String url = getString(R.string.base_url) + "/all_compliments" ;
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
                compliment_detail.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(respond);
                            JSONObject json2 = json.optJSONObject("result");
                            JSONArray jarray = json2.getJSONArray("waiting");
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject object     = jarray.getJSONObject(i);
                                String mcaseid = object.getString("_id");
                                String mfromid = "From ID " +object.getString("fromID");
                                String mtoid = "to ID: " + object.getString("toID");
                                String msubject = "Subject: " + object.getString("subject");
                                String mcontext = "Context: " + object.getString("context");

                                models.add(new model(mfromid,mtoid,msubject,mcontext,mcaseid));

                            }
                            myadapter = new AdapterComplaint(compliment_detail.this, models);
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



