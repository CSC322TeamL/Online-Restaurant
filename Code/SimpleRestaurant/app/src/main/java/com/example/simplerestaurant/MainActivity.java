package com.example.simplerestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText editUserName, editPsw;
    private View background;
    private Button buttonSubmit;
    private TextView textSurfer, textSignUp, textLoginError;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {}

        editUserName = (EditText) findViewById(R.id.edittext_username);
        editPsw = (EditText) findViewById(R.id.edittext_psw);
        buttonSubmit = (Button) findViewById(R.id.button_sumbit);
        background = (View) findViewById(R.id.view_background);
        textSurfer = (TextView) findViewById(R.id.textview_surfer);
        textSignUp = (TextView) findViewById(R.id.textview_signup);
        textLoginError = (TextView) findViewById(R.id.textview_loginerror);

        buttonSubmit.setOnClickListener(this);
        textSurfer.setOnClickListener(this);
        textSignUp.setOnClickListener(this);


        editUserName.addTextChangedListener(this);
        editPsw.addTextChangedListener(this);

        background.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(null != MainActivity.this.getCurrentFocus()){
                    InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    return manager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    private void send2Server(String username, String psw){
        String url = "http://10.0.2.2:5000/user";
        OkHttpClient client = new OkHttpClient();
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
            toastMessage("Logined");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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

        } else if(id == R.id.textview_signup){

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
