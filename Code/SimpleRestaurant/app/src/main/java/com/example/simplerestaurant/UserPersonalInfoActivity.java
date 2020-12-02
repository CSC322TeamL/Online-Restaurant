package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.example.simplerestaurant.beans.UserBasicInfoBean;
import com.google.android.material.textfield.TextInputLayout;

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

public class UserPersonalInfoActivity extends BaseActivity implements View.OnClickListener{

    private UserBasicInfoBean userInfo;
    private TextInputLayout tiFirstName, tiLastName, tiDisplayName
            , tiEmail, tiPhone, tiStreet, tiCity, tiState, tiZipCode;
    private String firstName, lastName, displayName, email, phone
            , street, city, state, zipCode;

    private ImageButton backward;
    private Button submit;

    private String userID, userType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_personal_info);

        tiFirstName = (TextInputLayout)findViewById(R.id.textinput_user_firstname);
        tiLastName = (TextInputLayout)findViewById(R.id.textinput_user_lastname);
        tiDisplayName = (TextInputLayout)findViewById(R.id.textinput_user_displayname);
        tiEmail = (TextInputLayout)findViewById(R.id.textinput_user_email);
        tiPhone = (TextInputLayout)findViewById(R.id.textinput_user_phone);
        tiStreet = (TextInputLayout)findViewById(R.id.textinput_user_street);
        tiCity = (TextInputLayout)findViewById(R.id.textinput_user_city);
        tiState = (TextInputLayout)findViewById(R.id.textinput_user_state);
        tiZipCode = (TextInputLayout)findViewById(R.id.textinput_user_zipcode);

        backward = (ImageButton) findViewById(R.id.imagebtn_backward);
        submit = (Button) findViewById(R.id.button_user_info_submit);

        backward.setOnClickListener(this);
        submit.setOnClickListener(this);

        Intent intent = getIntent();
        userInfo = UnitTools.getGson().fromJson(intent.getStringExtra("userInfo"), UserBasicInfoBean.class);
        Log.i("acc", userInfo.toString());

        userID = userInfo.getUserID();
        userType = userInfo.getUserRole();
        setUpFields(userInfo);
    }

    private void setUpFields(UserBasicInfoBean userinfo){
        if(null == tiFirstName){
            return;
        }
        tiFirstName.getEditText().setText(userinfo.getBasicInfo().getFistName());
        tiLastName.getEditText().setText(userinfo.getBasicInfo().getLastName());
        tiEmail.getEditText().setText(userinfo.getContact().getEmail());
        tiDisplayName.getEditText().setText(userinfo.getDisplayName());
        tiPhone.getEditText().setText(userinfo.getContact().getPhone());
        tiStreet.getEditText().setText(userinfo.getContact().getAddress().getStreet());
        tiCity.getEditText().setText(userinfo.getContact().getAddress().getCity());
        tiState.getEditText().setText(userinfo.getContact().getAddress().getState());
        tiZipCode.getEditText().setText(userinfo.getContact().getAddress().getZipCode());
    }

    private UserBasicInfoBean getInfoFromfields(){
        firstName = tiFirstName.getEditText().getText().toString().trim();
        lastName = tiLastName.getEditText().getText().toString().trim();
        displayName = tiDisplayName.getEditText().getText().toString().trim();
        phone = tiPhone.getEditText().getText().toString().trim();
        street = tiStreet.getEditText().getText().toString().trim();
        city = tiCity.getEditText().getText().toString().trim();
        state = tiState.getEditText().getText().toString().trim();
        zipCode = tiZipCode.getEditText().getText().toString().trim();
        if(null == firstName){
            tiFirstName.getEditText().setError("First Name empty");
            return null;
        }
        if(null == lastName){
            tiLastName.getEditText().setError("First Name empty");
            return null;
        }
        if(null == displayName){
            tiDisplayName.getEditText().setError("First Name empty");
            return null;
        }
        if(null == phone){
            tiPhone.getEditText().setError("First Name empty");
            return null;
        }
        if(null == street){
            tiStreet.getEditText().setError("First Name empty");
            return null;
        }
        if(null == city){
            tiCity.getEditText().setError("First Name empty");
            return null;
        }
        if(null == state){
            tiState.getEditText().setError("First Name empty");
            return null;
        }
        if(null == zipCode){
            tiZipCode.getEditText().setError("First Name empty");
            return null;
        }
        userInfo.getBasicInfo().setFistName(firstName);
        userInfo.getBasicInfo().setLastName(lastName);
        userInfo.setDisplayName(displayName);
        userInfo.getContact().setPhone(phone);
        userInfo.getContact().getAddress().setState(state);
        userInfo.getContact().getAddress().setCity(city);
        userInfo.getContact().getAddress().setStreet(street);
        userInfo.getContact().getAddress().setZipCode(zipCode);

        return userInfo;
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
        } else {
            toastMessage("Something wrong");
        }
        backward.setClickable(true);
        submit.setClickable(true);
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
        switch (v.getId()){
            case R.id.button_user_info_submit:
                UserBasicInfoBean toUpdate = getInfoFromfields();
                if(toUpdate == null){
                    toastMessage("Some input fields are not good");
                    return;
                }
                String updateString = userInfo2Json(toUpdate);
                submitChange2Server(updateString);
                break;
            case R.id.imagebtn_backward:
                finish();
                break;
        }
    }
}
