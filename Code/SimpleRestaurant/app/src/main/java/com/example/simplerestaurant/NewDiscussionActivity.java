package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.example.simplerestaurant.beans.DiscussionBean;
import com.example.simplerestaurant.beans.DiscussionDetailBean;
import com.example.simplerestaurant.beans.SimpleResponseBean;

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

public class NewDiscussionActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private ImageButton imgbtnBack;
    private EditText etTitle, etContext;
    private RadioGroup discussionGroup;
    private RadioButton rbtnFood, rbtnCook, rbtnDelivery;
    private Button btnSubmit;

    private String discOnStr = "Food";
    private DiscussionDetailBean discussionDetail;
    private DiscussionBean discussionBean;

    private String userID, userType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_discussion);

        Intent intent = getIntent();
        this.userID = intent.getStringExtra("userID");
        this.userType = intent.getStringExtra("userType");

        imgbtnBack = (ImageButton) findViewById(R.id.imagebtn_backward);
        imgbtnBack.setOnClickListener(this);

        discussionGroup = (RadioGroup) findViewById(R.id.radiogroup_new_discussion_on);
        rbtnFood = (RadioButton) findViewById(R.id.radiobtn_new_discussion_food);
        rbtnCook = (RadioButton) findViewById(R.id.radiobtn_new_discussion_cook);
        rbtnDelivery = (RadioButton) findViewById(R.id.radiobtn_new_discussion_delivery);

        discussionGroup.setOnCheckedChangeListener(this);
        discussionGroup.check(rbtnFood.getId());

        etTitle = (EditText) findViewById(R.id.et_new_discussion_title);
        etContext = (EditText) findViewById(R.id.et_new_discussion_context);

        btnSubmit = (Button) findViewById(R.id.btn_new_discussion_submit);
        btnSubmit.setOnClickListener(this);

        discussionDetail = new DiscussionDetailBean();
        discussionBean = new DiscussionBean();
    }

    private String getDiscussionJson(){
        String title = etTitle.getText().toString().trim();
        if(title.isEmpty() || title.equals("")){
            toastMessage("Title cannot be empty");
            return null;
        }
        String context = etContext.getText().toString().trim();
        if(context.isEmpty() || context.equals("")){
            toastMessage("Context cannot be empty");
            return null;
        }
        discussionDetail.setContext(context);
        discussionBean.setDetail(discussionDetail);
        discussionBean.setTitle(title);
        discussionBean.setDiscussionOn(discOnStr);
        discussionBean.setUserID(userID);
        String jsonStr = UnitTools.getGson().toJson(discussionBean);
        // remove the _id
        try {
            JSONObject res = new JSONObject(jsonStr);
            res.remove("_id");
            return res.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        toastMessage("Error when formatting data");
        return null;
    }

    private void handleServerResponse(String res){
        SimpleResponseBean response = UnitTools.getGson().fromJson(res, SimpleResponseBean.class);
        if(response.getCode() == 0){
            toastMessage("Submitted!");
        } else if(response.getCode() == 1){
            toastMessage("Your discussion has some taboo words.\nA waring received.");
        } else if(response.getCode() == -1){
            toastMessage("Your discussion has more than 3 taboo words.\nThe discussion had been blocked.");
        }
        this.finish();
    }

    private void submit2server(){
        String jsonStr = getDiscussionJson();
        if(null == jsonStr){
            return;
        }
        String url = getString(R.string.base_url) + "/new_discussion";
        RequestBody body = RequestBody.create(jsonStr, MediaType.parse(UnitTools.TYPE_JSON));
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = UnitTools.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Failed to connect server.");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string().trim();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleServerResponse(res);
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
            case R.id.btn_new_discussion_submit:
                if(null != discussionBean){
                    //Log.i("diss", discussionBean.toString());
                    submit2server();
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radiobtn_new_discussion_food:
                discOnStr = "Food";
                break;
            case R.id.radiobtn_new_discussion_cook:
                discOnStr = "Cook";
                break;
            case R.id.radiobtn_new_discussion_delivery:
                discOnStr = "Delivery";
                break;
        }
    }
}
