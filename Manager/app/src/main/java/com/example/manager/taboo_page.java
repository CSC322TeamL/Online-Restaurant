package com.example.manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class taboo_page extends AppCompatActivity {
    TextView taboos;
    Button btn_add;
    Button btn_delete;
    EditText addword;
    EditText deleteword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taboo_page);

        addword = (EditText)findViewById(R.id.edit_taboo_add);
        deleteword=(EditText)findViewById(R.id.delete_taboo);
        btn_add=(Button)findViewById(R.id.btn_addtaboo);
        btn_delete=(Button)findViewById(R.id.btn_deletetaboo);
        taboos = (TextView)findViewById(R.id.taboolist_tv);
        gettaboo();
        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String word = addword.getText().toString();
                if(word.equals("") || word.equals(" ")){
                    Toast.makeText(taboo_page.this, "You haven't type anything yet.", Toast.LENGTH_LONG).show();
                    gettaboo();
                }
                else{
                    addtaboo(word);
                    Toast.makeText(taboo_page.this, "Done", Toast.LENGTH_LONG).show();
                    gettaboo();
                }


            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String word = deleteword.getText().toString();
                if(word.equals("") || word.equals(" ")){
                    Toast.makeText(taboo_page.this, "You haven't type anything yet.", Toast.LENGTH_LONG).show();
                    gettaboo();
                }
                else{
                    deletetaboo(word);
                    Toast.makeText(taboo_page.this, "Done", Toast.LENGTH_LONG).show();
                    gettaboo();
                }


            }
        });



    }


    public void gettaboo() {

        String url = getString(R.string.base_url) + "/all_taboos";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();

        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
                Toast.makeText(taboo_page.this, "fail to connect server", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string();
                taboo_page.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jarray = new JSONArray(res);
                            String s = "";
                            for(int i=0;i<jarray.length();i++){

                                s=s+jarray.getString(i) + " , ";
                            }
                            String tabo = "Taboo words: " +s;
                            taboos.setText(tabo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

            }
        });
    }

    public void addtaboo(String words){

        String url = getString(R.string.base_url) + "/add_taboo";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("words",words);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }

    public void deletetaboo(String words){

        String url = getString(R.string.base_url) + "/delete_taboo";
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("words",words);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });
    }


}