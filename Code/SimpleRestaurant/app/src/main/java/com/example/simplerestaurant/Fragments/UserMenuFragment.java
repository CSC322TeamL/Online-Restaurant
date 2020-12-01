package com.example.simplerestaurant.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.simplerestaurant.Interfaces.UserMenuFragmentInterface;
import com.example.simplerestaurant.R;
import com.example.simplerestaurant.UnitTools;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserMenuFragment extends Fragment {
    private TextView greeting;
    private OkHttpClient client;
    private String userID, userType;
    private UserMenuFragmentInterface menuListener;

    public UserMenuFragment(UserMenuFragmentInterface menuListener){
        super(R.layout.fragment_user_main_menu);
        this.menuListener = menuListener;
        Log.i("menu", "Fragment created");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userType = requireArguments().getString("userType");
        userID = requireArguments().getString("userID");
        greeting = (TextView) view.findViewById(R.id.greeting_name);
        greeting.setText(userType);
        Log.i("menu", "Fragment called");
        client = UnitTools.getOkHttpClient();
        if(null != menuListener){
            menuListener.getMenuFromServer(userID);
        }
    }

    public void setGetMenuListener(UserMenuFragmentInterface listener){
        menuListener = listener;
    }

    private void getMenuFromServer(){
        String url = getString(R.string.base_url) + "/get_menu";
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", userID);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("menu", e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    final String res = response.body().string();
                    @Override
                    public void run() {
                        menuResponse(res);
                    }
                });
            }
        });
    }

    public void menuResponse(String res){
        Log.i("menu", res);
    }
}
