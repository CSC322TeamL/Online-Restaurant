package com.example.simplerestaurant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private TextView textSurfer, textSignUp, textLoginError;
    private Context context = this;

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

        editUserName.setHint("Username");
        editPsw.setHint("Password");

        buttonSubmit.setOnClickListener(this);
        textSurfer.setOnClickListener(this);
        textSignUp.setOnClickListener(this);


        editUserName.addTextChangedListener(this);
        editPsw.addTextChangedListener(this);

        hideInputMethod(background);
    }

    private void send2Server(String username, String psw){
        String url = getString(R.string.base_url) + "/user";
        // get OkHttp3 client from the base activity
        OkHttpClient client = getClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("username", username);
        bodyBuilder.add("password", psw);
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
                        Log.i("222", "In the run: " + res);
                        loginResponseHandler(res);
                    }
                });
            }
        });
    }

    private void loginResponseHandler(String res){
        Log.i("222", "In the UI: " + res);
        if (res.equals("1")) {
            textLoginError.setVisibility(View.VISIBLE);
        } else {
            // route the user to the main page
            Intent intent = new Intent(this, UserMainPage.class);
            intent.putExtra("UserType", res);
            finish();
            startActivity(intent);
        }
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
            loginResponseHandler("Surfer");
        } else if(id == R.id.textview_signup){
            Intent intent = new Intent(this, UserRegisterRequestActivity.class);
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
