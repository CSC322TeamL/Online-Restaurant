package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.simplerestaurant.beans.SimpleResponseBean;
import com.example.simplerestaurant.beans.UserBasicInfoBean;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import kotlin.Unit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResetPSWActivity extends BaseActivity implements View.OnClickListener{

    private EditText etUserID, etNewPSW, etConfirmPSW;
    private String userID, newPSW, confirmPSW, userType;
    private Button btnSubmit;

    private int isActivation = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_psw);

        Intent intent = getIntent();
        isActivation = intent.getIntExtra("activate", 1);
        if(isActivation == 0){
            userID = intent.getStringExtra("userID");
        }

        etUserID = (EditText) findViewById(R.id.edittext_email);
        etNewPSW = (EditText) findViewById(R.id.edittext_new_psw);
        etConfirmPSW = (EditText) findViewById(R.id.edittext_confirm_psw);
        btnSubmit = (Button) findViewById(R.id.button_reset_psw_submit);

        if(isActivation == 0){
            etUserID.setText(userID);
        } else {
            etUserID.setHint("User ID");
        }
        etNewPSW.setHint("New Password");
        etConfirmPSW.setHint("Confirm New Password");
        btnSubmit.setOnClickListener(this);
    }

    /**
     * handle psw update response
     * @param res
     */
    private void serverResponse(String res){
        try {
            JSONObject jsonObject = new JSONObject(res);
            if(isActivation == 0){
                userType = jsonObject.getString("role");
                getUserInfo(userID, userType);
            } else {
                this.finish();
            }
        } catch (Exception e){
            toastMessage("Something wrong");
        }
    }

    /**
     * start jumping to user info page
     * @param res
     */
    private void userInfoResponse(String res){
        try {
            UserBasicInfoBean info = UnitTools.getGson().fromJson(res, UserBasicInfoBean.class);
            Intent intent = new Intent(this, UserPersonalInfoActivity.class);
            intent.putExtra("userInfo", res);
            intent.putExtra("activate", 0);
            this.finish();
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
            toastMessage("Please Login manually");
        }
    }


    /**
     * jump to the user info page
     * @param userID
     * @param userType
     */
    private void getUserInfo(String userID, String userType){
        String url = getString(R.string.base_url) + "/get_info";
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("role", userType);
        bodyBuilder.add("userID", userID);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = UnitTools.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                toastMessage("Cannot connect to server");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    final String res = response.body().string();
                    @Override
                    public void run() {
                        userInfoResponse(res);
                    }
                });
            }
        });
    }

    /**
     * update user psw
     * @param userID
     * @param newPSW
     */
    private void sent2server(String userID, String newPSW){
        String url = getString(R.string.base_url) + "/change_password";
        // get OkHttp3 client from the base activity
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", userID);
        bodyBuilder.add("password", newPSW);
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
                final String res = response.body().string().trim();
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
        if(v.getId() == R.id.button_reset_psw_submit){
            userID = etUserID.getText().toString().trim();
            if(userID.isEmpty() || userID.equals("")){
                toastMessage("User ID empty");
                return;
            }
            newPSW = etNewPSW.getText().toString().trim();
            confirmPSW = etConfirmPSW.getText().toString().trim();
            if(newPSW.isEmpty() || newPSW.equals("")){
                toastMessage("Please input a new password");
                return;
            }
            if(confirmPSW.isEmpty() || newPSW.equals("") || !confirmPSW.equals(newPSW)){
                toastMessage("Confirm password not matching your new password");
                return;
            }
            sent2server(userID, newPSW);
        }
    }
}
