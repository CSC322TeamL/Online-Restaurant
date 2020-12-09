package com.example.manager;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.security.auth.login.LoginException;


public class createChefAccount extends AppCompatActivity {
    EditText firstname, lastname,gender,email,phone,street,city,state,zipcode,hourlyrate,userid;
    Button _submit;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chef_account);

        tv = (TextView)findViewById(R.id.textView5);
        firstname = (EditText) findViewById(R.id.ip_fn_chef);
        lastname= (EditText) findViewById(R.id.ip_ln_chef);
        gender= (EditText) findViewById(R.id.ip_chef_gender);
        email= (EditText) findViewById(R.id.ip_chef_email);
        phone= (EditText) findViewById(R.id.in_chef_phone);
        street= (EditText) findViewById(R.id.ip_chef_street);
        city= (EditText) findViewById(R.id.ip_chef_city);
        state= (EditText) findViewById(R.id.ip_chef_state);
        zipcode= (EditText) findViewById(R.id.ip_chef_zip);
        hourlyrate= (EditText) findViewById(R.id.ip_chef_hourlyRate);
        userid= (EditText) findViewById(R.id.ip_chef_userID);
        _submit= (Button) findViewById(R.id.ip_chef_submite);

        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String role = "chef";
                String password = "000000";
                String cf_firstname = firstname.getText().toString();
                String cf_lastname = lastname.getText().toString();
                String cf_gender = gender.getText().toString();
                String cf_email = email.getText().toString();
                String cf_phone = phone.getText().toString();
                String cf_street = street.getText().toString();
                String cf_city = city.getText().toString();
                String cf_state = state.getText().toString();
                String cf_zip = zipcode.getText().toString();
                String cf_hourlyrate = hourlyrate.getText().toString();
                String cf_userid = userid.getText().toString();

                if(cf_firstname.equals("")){
                    Toast.makeText(createChefAccount.this,"The first name cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(cf_lastname.equals("")){
                    Toast.makeText(createChefAccount.this,"The Last name cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(cf_gender.equals("")){
                    Toast.makeText(createChefAccount.this,"The gender cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(cf_email.equals("")){
                    Toast.makeText(createChefAccount.this,"The email cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(cf_phone.equals("")){
                    Toast.makeText(createChefAccount.this,"The phone number cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(cf_street.equals("")){
                    Toast.makeText(createChefAccount.this,"The street cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(cf_city.equals("")){
                    Toast.makeText(createChefAccount.this,"The city cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(cf_state.equals("")){
                    Toast.makeText(createChefAccount.this,"The state cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(cf_zip.equals("")){
                    Toast.makeText(createChefAccount.this,"The zip code cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(cf_hourlyrate.equals("")){
                    Toast.makeText(createChefAccount.this,"The hourlyrate cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(cf_userid.equals("")){
                    Toast.makeText(createChefAccount.this,"The useer ID cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else {

                    JSONObject staff = new JSONObject();
                    try {

                        JSONObject personalInfo = new JSONObject();
                        personalInfo.put("firstName", cf_firstname);
                        personalInfo.put("lastName", cf_lastname);
                        personalInfo.put("gender", cf_gender);

                        JSONObject address = new JSONObject();
                        address.put("street", cf_street);
                        address.put("city", cf_city);
                        address.put("state", cf_state);
                        address.put("zipCode", cf_zip);

                        JSONObject contact = new JSONObject();
                        contact.put("email", cf_email);
                        contact.put("phone", cf_phone);
                        contact.put("address", address);

                        staff.put("userID", cf_userid);
                        staff.put("staffType", "chef");
                        staff.put("personalInfo", personalInfo);
                        staff.put("contactInfo", contact);
                        staff.put("address", address);
                        staff.put("hourlyRate", cf_hourlyrate);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = "http://10.0.2.2:5000" + "/new_staff";
                    OkHttpClient client = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(staff.toString(),JSON);
                    String s =staff.toString();

                    Request request = new Request.Builder().url(url).post(body).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            final String res = response.body().string();
                            createChefAccount.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject json = new JSONObject(res);
                                        String code = json.getString("code");
                                        String context = json.getString("content");
                                        if(code.equals("0")){
                                            Toast.makeText(createChefAccount.this,context,Toast.LENGTH_SHORT).show();
                                            firstname.setText(null);
                                            lastname.setText(null);
                                            gender.setText(null);
                                            email.setText(null);
                                            phone.setText(null);
                                            street.setText(null);
                                            city.setText(null);
                                            state.setText(null);
                                            zipcode.setText(null);
                                            hourlyrate.setText(null);
                                            userid.setText(null);

                                        }
                                        else if(code.equals("1")){
                                            Toast.makeText(createChefAccount.this,context,Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                    });
                }


            }
        });


    }
}