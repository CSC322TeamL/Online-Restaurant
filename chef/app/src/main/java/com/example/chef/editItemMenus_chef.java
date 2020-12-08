package com.example.chef;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class editItemMenus_chef extends AppCompatActivity {

    EditText editname, editdescribe, editprice, editkey;
    TextView ff123;
    Button _submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_itemto_menus_chef);

        editname = (EditText) findViewById(R.id.edit_name);
        editprice = (EditText)findViewById(R.id.edit_price);
        editdescribe = (EditText)findViewById(R.id.edit_description);
        editkey = (EditText)findViewById(R.id.edit_keyword);
        _submit = (Button)findViewById(R.id.cehf_edit_submit);

        String userid = getuserid.userid;
        Bundle extra = getIntent().getExtras();
        String dishid = extra.getString("dishid");
        String name1 = extra.getString("name");
        String price1 = extra.getString("price");
        String describe1 = extra.getString("describe");
        String key1 = extra.getString("key");



        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editname.getText().toString();
                String price = editprice.getText().toString();
                String describe = editdescribe.getText().toString();
                String key = editkey.getText().toString();
                String fname, fprice,fdescribe,fkey;



                if(name.equals("")){
                    fname = name1;
                }
                else{
                    fname = name;
                }


                if (price.equals("")){
                    fprice = price1;
                }else{fprice = price;
                }


                if(describe.equals("")){fdescribe = describe1;}
                else {fdescribe = describe;}

                if(key.equals("")){fkey = "-1";}
                else {fkey = key;}


                String url = "http://10.0.2.2:5000" + "/update_dish";
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                String role = "chef";

                bodyBuilder.add("_id",dishid);
                bodyBuilder.add("title", fname);
                bodyBuilder.add("keywords", fkey);
                bodyBuilder.add("price",fprice);
                bodyBuilder.add("description",fdescribe);

                Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    }
                });

                Toast.makeText(editItemMenus_chef.this,"The Edit was changed , Restart from Menu Button to see the change.", Toast.LENGTH_LONG).show();
            }
        });



    }


}