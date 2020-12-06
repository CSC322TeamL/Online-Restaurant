package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.Adapters.OrderHistoryDIshListAdapter;
import com.example.simplerestaurant.beans.OrderBean;

public class UserOrderDetailActivity extends BaseActivity {

    private OrderBean currentOrder;
    private TextView tvOrderID, tvOrderCreated, tvOrderCharged, tvDishQuantity, tvOrderStatus;
    private RecyclerView dishRecycler;
    private OrderHistoryDIshListAdapter adapter;
    private ImageButton imgbtnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_detail);
        Intent intent = getIntent();
        String orderStr = intent.getStringExtra("order");
        String userID = intent.getStringExtra("userID");
        String userType = intent.getStringExtra("userType");
        this.currentOrder = UnitTools.getGson().fromJson(orderStr, OrderBean.class);

        tvOrderID = (TextView) findViewById(R.id.tv_user_order_detail_id);
        tvOrderCreated = (TextView) findViewById(R.id.tv_user_order_detail_date);
        tvOrderCharged = (TextView) findViewById(R.id.tv_user_order_detail_charged);
        tvDishQuantity = (TextView) findViewById(R.id.tv_user_order_detail_quantity);
        tvOrderStatus = (TextView) findViewById(R.id.tv_user_order_detail_status);

        tvOrderID.setText(currentOrder.get_id());
        tvOrderCreated.setText(currentOrder.getCreateDate());
        tvOrderCharged.setText("$" + currentOrder.getOrderCharged());
        tvDishQuantity.setText(String.valueOf(currentOrder.getDishDetail().size()));
        tvOrderStatus.setText(currentOrder.getStatus());

        dishRecycler = (RecyclerView) findViewById(R.id.recycler_user_order_detail);
        adapter = new OrderHistoryDIshListAdapter(userID, userType, currentOrder);
        dishRecycler.setAdapter(adapter);
        dishRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
}
