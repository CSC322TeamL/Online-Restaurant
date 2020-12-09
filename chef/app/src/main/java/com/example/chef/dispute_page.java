package com.example.chef;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class dispute_page extends AppCompatActivity {
    EditText editcontext;
    Button _submit;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispute_page);

        editcontext = (EditText)findViewById(R.id.dispute_reason);
        _submit = (Button)findViewById(R.id.dispute_submit);

        userid = getuserid.userid;
        Bundle ext = getIntent().getExtras();
        String complaintid = ext.getString("complaintid");

        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String context = editcontext.getText().toString();
                Toast.makeText(v.getContext(),"The action done, re-enter page to see change",Toast.LENGTH_LONG).show();
                String url = "http://10.0.2.2:5000" + "/dispute_complaint";
                OkHttpClient client = new OkHttpClient();
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                String role = "chef";
                bodyBuilder.add("role", role);
                bodyBuilder.add("userID", userid);
                bodyBuilder.add("complaintID", complaintid);
                bodyBuilder.add("context", context);

                Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    }
                });
                Toast.makeText(dispute_page.this,"The dispute send.", Toast.LENGTH_LONG).show();


            }
        });

    }
}