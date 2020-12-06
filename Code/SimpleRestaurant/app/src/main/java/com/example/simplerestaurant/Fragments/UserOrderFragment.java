package com.example.simplerestaurant.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.Interfaces.UserOrderFragmentInterface;
import com.example.simplerestaurant.R;

public class UserOrderFragment extends Fragment {
    private RecyclerView orderRecycler;
    private TextView tvEmptyList;
    private String userID, userType;

    private UserOrderFragmentInterface listener;

    public UserOrderFragment(String userID, String userType, UserOrderFragmentInterface listener) {
        super(R.layout.fragment_user_main_orders);
        this.listener = listener;
        this.userID = userID;
        this.userType = userType;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvEmptyList = (TextView) view.findViewById(R.id.tv_main_order_record_empty);
        orderRecycler = (RecyclerView) view.findViewById(R.id.recycler_main_order_list);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(null != listener){
            listener.getOrderListFromServer(userID, userType);
        }
    }

    public void orderListResponse(String res){
        Log.i("order", res);
    }
}
