package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class DisputeComplaintActivity extends BaseActivity implements View.OnClickListener {

    private String userID, userType, complaintID, complaintText;
    private TextView tvComplaintContext;
    private EditText etDisputeContext;
    private Button btnSubmit;
    private ImageButton imgbtnBack;
    private View vBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispute_complaint);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userType = intent.getStringExtra("userType");
        complaintID = intent.getStringExtra("complaintID");
        complaintText = intent.getStringExtra("complaintContext");

        tvComplaintContext = findViewById(R.id.tv_dispute_complaint_complaint_context);
        etDisputeContext = findViewById(R.id.et_dispute_complaint_context);
        vBackground = findViewById(R.id.view_dispute_background);
        imgbtnBack = findViewById(R.id.imagebtn_backward);
        btnSubmit = findViewById(R.id.btn_dispute_complaint_submit);

        tvComplaintContext.setText(complaintText);
        imgbtnBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        hideInputMethod(vBackground);
    }

    private void serverResponse(String res){
        if(res.equals("0")){
            toastMessage("Dispute Submitted");
            this.finish();
        } else {
            toastMessage("Failed to submit. Try again later.");
        }
    }

    private void submitDispute2Server(String complaintText){
        String url = getString(R.string.base_url) + "/dispute_complaint";
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", userID);
        bodyBuilder.add("role", userType);
        bodyBuilder.add("complaintID", complaintID);
        bodyBuilder.add("context", complaintText);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = UnitTools.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Failed to connect server");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res  = response.body().string().trim();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        serverResponse(res);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imagebtn_backward:
                this.finish();
                break;
            case R.id.btn_dispute_complaint_submit:
                String disputeTest = etDisputeContext.getText().toString().trim();
                if(disputeTest.isEmpty()){
                    toastMessage("Your dispute is empty.");
                    return;
                }
                submitDispute2Server(disputeTest);
                break;
        }
    }
}
