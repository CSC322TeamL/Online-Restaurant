package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.simplerestaurant.beans.CCBean;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ComplaintComplimentActivity extends BaseActivity implements View.OnClickListener {

    private String isComplaint, subjectTo, subjectID, userID, userType, orderID;
    private TextView tvIsComplaint, tvSubjectTo;
    private EditText etTitle, etContext;
    private Button btnSubmit;
    private ImageButton imgbtnBack;
    private CCBean ccBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_compliment);

        Intent intent = getIntent();
        isComplaint = intent.getStringExtra("isComplaint");
        subjectID = intent.getStringExtra("subjectID");
        subjectTo = intent.getStringExtra("subjectTo");
        userID = intent.getStringExtra("userID");
        userType = intent.getStringExtra("userType");
        orderID = intent.getStringExtra("orderID");

        tvIsComplaint = (TextView) findViewById(R.id.tv_cc_complaint_or_compliment);
        tvSubjectTo = (TextView) findViewById(R.id.tv_cc_to_subject_type);
        etContext = (EditText) findViewById(R.id.et_cc_context);
        etTitle = (EditText)findViewById(R.id.et_cc_title);
        btnSubmit = (Button) findViewById(R.id.btn_cc_submit);
        imgbtnBack = (ImageButton) findViewById(R.id.imagebtn_backward);

        if(isComplaint.equals("true")){
            tvIsComplaint.setText("Complaint");
        } else {
            tvIsComplaint.setText("Compliment");
        }

        tvSubjectTo.setText(subjectTo);
        imgbtnBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    private void submitResponse(String res){
        if(res.equals("0")){
            toastMessage("Submitted");
            this.finish();
        } else {
            toastMessage("Something wrong");
        }
    }

    private void upload2server(String res){
        String url = getString(R.string.base_url) + "/fileComplaintAndCompliment";
        RequestBody body = RequestBody.create(
                res, MediaType.parse(UnitTools.TYPE_JSON)
        );
        Request request = new Request.Builder().url(url).post(body).build();
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
                final String res = response.body().string().trim();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        submitResponse(res);
                    }
                });
            }
        });
    }

    private String getCCJson(){
        String title = etTitle.getText().toString().trim();
        if(title.equals("") || title.isEmpty()){
            toastMessage("Title cannot be empty");
            return  null;
        }
        String context = etContext.getText().toString().trim();
        if(context.isEmpty() || context.equals("")){
            toastMessage("Context cannot be empty");
            return null;
        }

        ccBean = new CCBean();
        ccBean.setFromID(userID);
        ccBean.setIsComplaint(isComplaint);
        ccBean.setToID(subjectID);
        ccBean.setSubject(title);
        ccBean.setContext(context);
        ccBean.setOrderID(orderID);
        try {
            String ccTemp = UnitTools.getGson().toJson(ccBean);
            JSONObject jsonObject = new JSONObject(ccTemp);
            jsonObject.remove("_id");
            jsonObject.remove("createDate");
            jsonObject.remove("finalizedDate");
            jsonObject.remove("reviewBy");
            return jsonObject.toString().trim();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        toastMessage("Error when formatting data");
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imagebtn_backward:
                this.finish();
                break;
            case R.id.btn_cc_submit:
                String res = getCCJson();
                if(null == res){
                    return;
                }
                upload2server(res);
                break;
        }
    }
}
