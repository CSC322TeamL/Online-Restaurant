package com.example.simplerestaurant.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.simplerestaurant.Interfaces.UserDiscussionFragmentInterface;
import com.example.simplerestaurant.R;

public class UserDiscussionFragment extends Fragment {

    private UserDiscussionFragmentInterface listener;
    private String userID, userType;

    public UserDiscussionFragment(UserDiscussionFragmentInterface listener){
        super(R.layout.fragment_user_main_discussion);
        this.listener = listener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle bundle = getArguments();

        userID = bundle.getString("userID");
        userType = bundle.getString("userType");

        if(null != listener){
            listener.getDiscussionFromServer(userID);
        }
    }
}
