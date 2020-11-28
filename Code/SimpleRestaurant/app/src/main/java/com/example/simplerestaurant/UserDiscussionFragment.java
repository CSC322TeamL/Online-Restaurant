package com.example.simplerestaurant;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserDiscussionFragment extends Fragment {

    public UserDiscussionFragment(){
        super(R.layout.fragment_user_main_menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView greeting = (TextView) view.findViewById(R.id.greeting);

        greeting.setText("In Discussion");
    }
}
