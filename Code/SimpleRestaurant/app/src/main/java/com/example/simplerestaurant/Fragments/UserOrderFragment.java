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
import com.example.simplerestaurant.UnitTools;
import com.example.simplerestaurant.beans.OrderBean;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserOrderFragment extends Fragment {
    private RecyclerView orderRecycler;
    private TextView tvEmptyList;
    private String userID, userType;
    private ArrayList<OrderBean> displayOrder;

    private UserOrderFragmentInterface listener;

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
            Log.i("order", displayOrder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
