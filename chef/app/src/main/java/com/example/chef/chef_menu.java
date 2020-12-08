package com.example.chef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

public class chef_menu extends AppCompatActivity {

    Button btn_addItem;
    private ArrayList<modelMenu> models;
    RecyclerView mrecycleview;
    private AdapterMenu myadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_menu);


        mrecycleview = findViewById(R.id.menu_recycle);
        mrecycleview.setLayoutManager(new LinearLayoutManager(this));

        models = new ArrayList<>();
        String userid = getuserid.userid;
        getmenu(userid);

    }

    public void getmenu(String userid) {

        String url = getString(R.string.base_url) + "/get_menu";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID",userid);

        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String respond = response.body().string();
                chef_menu.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jarray1 = new JSONArray(respond);

                            for(int k =0; k<jarray1.length(); k++){

                                JSONObject obj1 = jarray1.getJSONObject(k);
                                JSONArray jarray2 = obj1.getJSONArray("dishes");
                                for (int i = 0; i < jarray2.length(); i++) {
                                    JSONObject object = jarray2.getJSONObject(i);
                                    String dishname = "Dish Name: " + object.getString("title");
                                    String price = "Price: " + object.getString("price");
                                    String description = "Description: " + object.getString("description");
                                    String path1 =  object.getString("image");
                                    String dishid = object.getString("_id");
                                    String name1 = object.getString("title");
                                    String price1 = object.getString("price");
                                    String describe1 = object.getString("description");
                                    String key = object.getString("keywords");
                                    String rating ="Rating of dish: " + object.getString("ratings");
                                    int identifier = getResources().getIdentifier(path1, "drawable", getPackageName());
                                    int path = identifier;


                                    models.add(new modelMenu(dishname, price, description, path,dishid,name1,price1,describe1,key, rating));

                                }
                            }
                            myadapter = new AdapterMenu(chef_menu.this, models);
                            mrecycleview.setAdapter(myadapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }
}