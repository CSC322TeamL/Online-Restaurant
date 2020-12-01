package com.example.simplerestaurant.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.simplerestaurant.Interfaces.UserAccountFragmentInterface;
import com.example.simplerestaurant.R;

public class UserAccountFragment extends Fragment {

    private UserAccountFragmentInterface accListener;
    private String userID, userType;

    public UserAccountFragment(UserAccountFragmentInterface accListener){
        super(R.layout.fragment_user_account);
        this.accListener = accListener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        userID = bundle.getString("userID");
        userType = bundle.getString("userType");

        if(null != accListener){
            accListener.getUserInfoFromServer(userID, userType);
        }
    }

    public void userInfoResponse(String res){
        Log.i("acc", res);
    }
}
