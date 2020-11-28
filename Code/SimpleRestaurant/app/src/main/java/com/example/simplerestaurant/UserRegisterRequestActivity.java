package com.example.simplerestaurant;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserRegisterRequestActivity extends BaseActivity implements ViewStub.OnClickListener, TextWatcher {

    private EditText editEmail;
    private TextView textSuccess, textError;
    private Button buttonSubmit;
    private View toolbar, background;
    private ImageButton imgbtnBackward;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_request);

        background = (View) findViewById(R.id.view_user_register);
        editEmail = (EditText) findViewById(R.id.edittext_email);
        textSuccess = (TextView) findViewById(R.id.textview_submitted_messsage);
        textError = (TextView) findViewById(R.id.textview_error_message);
        buttonSubmit = (Button) findViewById(R.id.button_submit);
        toolbar = (View) findViewById(R.id.view_navigation_bar);
        imgbtnBackward = (ImageButton) toolbar.findViewById(R.id.imagebtn_backward);

        editEmail.setHint("Email Address");
        hideInputMethod(background);

        buttonSubmit.setOnClickListener(this);
        imgbtnBackward.setOnClickListener(this);
    }

    private void sen2Server(String email){
        String url = getString(R.string.base_url) + "/register_request";
        OkHttpClient client= UnitTools.getOkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("useremail", email);
        // build the request
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Failed to connect to server");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleResponse(res);
                    }
                });
            }
        });
    }

    private void handleResponse(String res){
        Log.i("222", res);
        // 1 is not matching
        if(res.equals("1")){
            // show the error message
            textError.setText("This email has registered");
            textError.setVisibility(View.VISIBLE);
        } else {
            // successfully submitted
            editEmail.setVisibility(View.INVISIBLE);
            textSuccess.setVisibility(View.VISIBLE);
            buttonSubmit.setClickable(false);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.button_submit){
            // clear the error message if any
            textError.setVisibility(View.INVISIBLE);

            String userEmail = editEmail.getText().toString().trim();
            //check is the text file empty or not
            if(userEmail.length() == 0){
                toastMessage("Email cannot be empty");
                return;
            }
            // email validation
            if(!UnitTools.isValidEmail(userEmail)){
                textError.setText("Email is not Valid");
                textError.setVisibility(View.VISIBLE);
                return;
            }
            // otherwise, send the request to server
            sen2Server(userEmail);
        } else if(id == R.id.imagebtn_backward){
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        textError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
