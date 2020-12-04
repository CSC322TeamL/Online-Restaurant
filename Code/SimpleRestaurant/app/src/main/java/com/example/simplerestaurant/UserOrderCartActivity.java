package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.beans.OrderBean;

public class UserOrderCartActivity extends BaseActivity implements View.OnClickListener{

    private RecyclerView dishRecycler;
    private TextView tvOrderTotal;
    private View navigationBar;
    private ImageButton imgbtnBack, imgbtnEmpty;
    private Button btnPlace;

    private OrderBean currentOrder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_cart);

        dishRecycler = (RecyclerView) findViewById(R.id.recycler_order_cart);
        tvOrderTotal = (TextView) findViewById(R.id.tv_order_cart_total);
        btnPlace = (Button) findViewById(R.id.button_order_cart_place);

        navigationBar = (View) findViewById(R.id.view_order_cart_navigation);
        imgbtnBack = (ImageButton) navigationBar.findViewById(R.id.imagebtn_backward);
        imgbtnEmpty = (ImageButton) navigationBar.findViewById(R.id.imagebtn_cart_empty);

        btnPlace.setOnClickListener(this);
        imgbtnBack.setOnClickListener(this);
        imgbtnEmpty.setOnClickListener(this);

        Intent intent = getIntent();
        String order = intent.getStringExtra("order");

        this.currentOrder = UnitTools.getGson().fromJson(order, OrderBean.class);

        tvOrderTotal.setText("$" + currentOrder.getOrderTotal());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_order_cart_place:
                break;
            case R.id.imagebtn_backward:
                break;
            case R.id.imagebtn_cart_empty:
                break;
        }
    }
}
