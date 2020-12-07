package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.simplerestaurant.beans.UserBasicInfoBean;

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

public class UserRefillBalanceActivity extends BaseActivity implements View.OnClickListener{

    private EditText etAmount;
    private Button btnSubmit;

    private String userID, userType;
    private UserBasicInfoBean userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill_balance);

        Intent intent = getIntent();
        userInfo = UnitTools.getGson().fromJson(intent.getStringExtra("userInfo"), UserBasicInfoBean.class);
        userType = userInfo.getUserRole();

        etAmount = (EditText) findViewById(R.id.et_refill_balance_amount);
        btnSubmit = (Button) findViewById(R.id.btn_refill_balance_submit);
        btnSubmit.setOnClickListener(this);
    }

    private String userInfo2Json(UserBasicInfoBean userinfo){
        String tempStr =  UnitTools.getGson().toJson(userinfo);
        try {
            JSONObject jsonObject = new JSONObject(tempStr);
            jsonObject.put("role", userType);
            tempStr = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tempStr;
    }

    private void handleServerResponse(String res){
        if(res.equals("0")){
            toastMessage("Updated");
            this.finish();
        } else {
            toastMessage("Something wrong");
        }
    }

    private void submitChange2Server(String update){
        String url = getString(R.string.base_url) + "/update_info";
        RequestBody body = RequestBody.create(
                update, MediaType.parse(UnitTools.TYPE_JSON)
        );
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = UnitTools.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Failed to connect to server.");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
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
        if(v.getId() == R.id.btn_refill_balance_submit){
            if(null == userInfo){
                return;
            }
            String amountStr = etAmount.getText().toString().trim();
            if(amountStr.isEmpty() || amountStr.equals("")){
                toastMessage("Input empty");
                return;
            }
            float fillUp = Float.valueOf(amountStr);
            if(fillUp <= 0){
                toastMessage("Please input a correct value");
                return;
            }
            userInfo.setBalance(userInfo.getBalance() + fillUp);
            submitChange2Server(userInfo2Json(userInfo));
        }
    }
}
