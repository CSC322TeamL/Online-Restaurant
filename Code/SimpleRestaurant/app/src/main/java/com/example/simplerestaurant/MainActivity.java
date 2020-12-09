package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.simplerestaurant.beans.SimpleResponseBean;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private EditText editUserName, editPsw;
    private View background;
    private Button buttonSubmit;
    private TextView textSurfer, textSignUp, textLoginError, textResetPSW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUserName = (EditText) findViewById(R.id.edittext_username);
        editPsw = (EditText) findViewById(R.id.edittext_psw);
        buttonSubmit = (Button) findViewById(R.id.button_sumbit);
        background = (View) findViewById(R.id.view_background);
        textSurfer = (TextView) findViewById(R.id.textview_surfer);
        textSignUp = (TextView) findViewById(R.id.textview_signup);
        textLoginError = (TextView) findViewById(R.id.textview_loginerror);
        textResetPSW = (TextView) findViewById(R.id.textview_reset_psw);

        editUserName.setHint("Username");
        editPsw.setHint("Password");

        editUserName.setText("deli0001");
        editPsw.setText("deli0001");

        buttonSubmit.setOnClickListener(this);
        textSurfer.setOnClickListener(this);
        textSignUp.setOnClickListener(this);
        textResetPSW.setOnClickListener(this);

        editUserName.addTextChangedListener(this);
        editPsw.addTextChangedListener(this);

        hideInputMethod(background);
    }

    private void send2Server(String username, String psw){
        String url = getString(R.string.base_url) + "/user_login";
        // get OkHttp3 client from the base activity
        OkHttpClient client = UnitTools.getOkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", username);
        bodyBuilder.add("userPassword", psw);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = client.newCall(request);
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
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loginResponseHandler(res);
                    }
                });
            }
        });
    }

    private void loginResponseHandler(String res){
        //Log.i("222", "In the UI: " + res);
        Gson gson = UnitTools.getGson();
        SimpleResponseBean result = gson.fromJson(res, SimpleResponseBean.class);
        if(result.getCode() == 1){
            textLoginError.setText(result.getContent());
            textLoginError.setVisibility(View.VISIBLE);
        } else if (result.getCode() == 0){
            if("Customer".equals(result.getContent()) || "VIP".equals(result.getContent())){
                Intent intent = new Intent(this, UserMainPageActivity.class);
                intent.putExtra("userID", editUserName.getText().toString().trim());
                intent.putExtra("userType", result.getContent());
                finish();
                startActivity(intent);
            } else if("active".equals(result.getContent())){
                // user that needs to be activated
                Intent intent = new Intent(this, ResetPSWActivity.class);
                intent.putExtra("activate", 0);
                intent.putExtra("userID", editUserName.getText().toString().trim());
                finish();
                startActivity(intent);
            } else if("delivery".equals(result.getContent())){
                // delivery people
                Intent intent = new Intent(this, DeliveryMainPageActivity.class);
                intent.putExtra("userID", editUserName.getText().toString().trim());
                intent.putExtra("userType", result.getContent());
                finish();
                startActivity(intent);
            }
            else
                toastMessage("Hello " + result.getContent());
        } else {
            textLoginError.setText("Unexpected Error");
            textLoginError.setVisibility(View.VISIBLE);
        }
        /*if (res.equals("1")) {
            textLoginError.setVisibility(View.VISIBLE);
        } else {
            // route the user to the main page
            Intent intent = new Intent(this, UserMainPage.class);
            intent.putExtra("UserType", res);
            finish();
            startActivity(intent);
        }*/
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.button_sumbit){
            String name = editUserName.getText().toString().trim();
            if(name.length() == 0){
                toastMessage("Username can't be empty");
                return;
            }
            String pss = editPsw.getText().toString().trim();
            if(pss.length() == 0){
                toastMessage("Password can't be empty");
            }
            send2Server(name, pss);
        } else if (id == R.id.textview_surfer){
            // just browser the menu
            Intent intent = new Intent(this, UserMainPageActivity.class);
            intent.putExtra("userID", "-1");
            intent.putExtra("userType", "Surfer");
            finish();
            startActivity(intent);
        } else if(id == R.id.textview_signup){
            Intent intent = new Intent(this, UserRegisterRequestActivity.class);
            startActivity(intent);
        } else if(id == R.id.textview_reset_psw){
            Intent intent = new Intent(this, ResetPSWActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        textLoginError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
