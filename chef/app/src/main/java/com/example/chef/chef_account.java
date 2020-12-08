package com.example.chef;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class chef_account extends AppCompatActivity {

    String userid = "chef0001";
    String role = "chef";
    TextView chef_gender, chef_name, chef_phone, chef_email, chef_address;
    private String _gender, _name, _id, _phone, _email;
    private Object String;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_account);


        chef_name = (TextView) findViewById(R.id.chef_username_tv);
        chef_gender = (TextView) findViewById(R.id.chef_gender_textview);
        chef_phone = (TextView) findViewById(R.id.chef_phonetextview);
        chef_email = (TextView) findViewById(R.id.chef_emailtextview);
        chef_address = (TextView)findViewById(R.id.tv_chef_address);
        getcomplaint();



    }

    public void getcomplaint() {

        String url = getString(R.string.base_url) + "/get_info";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID",userid);
        bodyBuilder.add("role", role);

        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String respond = response.body().string();
                chef_account.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject(respond);
                            JSONObject jperson = json.getJSONObject("personalInfo");
                            JSONObject jconteact = json.getJSONObject("contactInfo");
                            JSONObject jaddress = json.getJSONObject("address");
                            String name = jperson.getString("firstName") +" "+ jperson.getString("lastName");
                            String gender = jperson.getString("gender");
                            String email = jconteact.getString("email");
                            String phone = jconteact.getString("phone");
                            String address = jaddress.getString("street") +" "+jaddress.getString("city")+ " "+jaddress.getString("state")+ " "+ jaddress.getString("zipCode");


                            chef_name.setText(name);
                            chef_gender.setText(gender);
                            chef_phone.setText(phone);
                            chef_address.setText(address);
                            chef_email.setText(email);





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}