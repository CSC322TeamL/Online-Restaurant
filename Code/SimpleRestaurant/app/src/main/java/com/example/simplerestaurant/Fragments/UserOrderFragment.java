package com.example.simplerestaurant.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.Adapters.OrderListAdapter;
import com.example.simplerestaurant.Interfaces.OrderHis2DetailInterface;
import com.example.simplerestaurant.Interfaces.UserOrderFragmentInterface;
import com.example.simplerestaurant.R;
import com.example.simplerestaurant.UnitTools;
import com.example.simplerestaurant.UserOrderDetailActivity;
import com.example.simplerestaurant.beans.OrderBean;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserOrderFragment extends Fragment implements View.OnClickListener,
        OrderHis2DetailInterface {
    private RecyclerView orderRecycler;
    private TextView tvEmptyList;
    private String userID, userType;
    private ArrayList<OrderBean> displayOrder;

    private FloatingActionButton btnRefresh;

    private UserOrderFragmentInterface listener;

    private OrderListAdapter orderListAdapter;

    public UserOrderFragment(String userID, String userType, UserOrderFragmentInterface listener) {
        super(R.layout.fragment_user_main_orders);
        this.listener = listener;
        this.userID = userID;
        this.userType = userType;
        displayOrder = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvEmptyList = (TextView) view.findViewById(R.id.tv_main_order_record_empty);
        orderRecycler = (RecyclerView) view.findViewById(R.id.recycler_main_order_list);

        btnRefresh = (FloatingActionButton)  view.findViewById(R.id.btn_main_order_refresh);
        btnRefresh.setOnClickListener(this);

        orderListAdapter = new OrderListAdapter(userID, userType, displayOrder, this);
        orderRecycler.setAdapter(orderListAdapter);
        orderRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(null != listener){
            listener.getOrderListFromServer(userID, userType);
        }
    }

    public void orderListResponse(String res){
        //Log.i("order", res);
        try {
            JSONObject resJson = new JSONObject(res);
            JSONObject resultJson = resJson.getJSONObject("result");
            OrderListResponseBean orderListFromResponse =
                    UnitTools.getGson().fromJson(resultJson.toString().trim(), OrderListResponseBean.class);
            displayOrder.clear();
            displayOrder.addAll(orderListFromResponse.getWaiting());
            displayOrder.addAll(orderListFromResponse.getFinished());
            //Log.i("order", displayOrder.toString());
            if(orderListAdapter != null){
                Log.i("order" , "IM here");
                orderListAdapter.setOrderList(this.displayOrder);
                orderListAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnRefresh.getId()){
            if(null != listener){
                listener.getOrderListFromServer(userID, userType);
            }
        }
    }

    @Override
    public void startOrderDetail(OrderBean selectedOrder) {
        String selectedOrderStr = UnitTools.getGson().toJson(selectedOrder);
        Intent intent = new Intent(getActivity(), UserOrderDetailActivity.class);
        intent.putExtra("order", selectedOrderStr);
        intent.putExtra("userID", this.userID);
        intent.putExtra("userType", this.userType);
        getActivity().startActivity(intent);
    }

    public class OrderListResponseBean{
        ArrayList<OrderBean> waiting;
        ArrayList<OrderBean> finished;

        public ArrayList<OrderBean> getWaiting() {
            return waiting;
        }

        public void setWaiting(ArrayList<OrderBean> waiting) {
            this.waiting = waiting;
        }

        public ArrayList<OrderBean> getFinished() {
            return finished;
        }

        public void setFinished(ArrayList<OrderBean> finished) {
            this.finished = finished;
        }

        @Override
        public String toString() {
            return "OrderListResponseBean{" +
                    "waiting=" + waiting +
                    ", finished=" + finished +
                    '}';
        }
    }
}
