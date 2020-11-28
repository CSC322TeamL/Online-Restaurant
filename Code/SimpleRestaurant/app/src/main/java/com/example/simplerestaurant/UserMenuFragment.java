package com.example.simplerestaurant;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserMenuFragment extends Fragment {
    private String userName;
    private TextView greeting;

    public UserMenuFragment(){
        super(R.layout.fragment_user_main_menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userName = requireArguments().getString("username");
        greeting = (TextView) view.findViewById(R.id.greeting_name);
        greeting.setText(userName);
    }
}
