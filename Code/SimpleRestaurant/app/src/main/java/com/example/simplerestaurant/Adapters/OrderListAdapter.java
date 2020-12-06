package com.example.simplerestaurant.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.Interfaces.OrderHis2DetailInterface;
import com.example.simplerestaurant.R;
import com.example.simplerestaurant.beans.OrderBean;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> {

    private String userID, userType;
    private ArrayList<OrderBean> orderList;
    private OrderHis2DetailInterface listener;

    public OrderListAdapter(String userID, String userType, ArrayList<OrderBean> orderList, OrderHis2DetailInterface listener) {
        setOrderList(orderList);
        this.userID = userID;
        this.userType = userType;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_list_order_item, parent, false);
        return new OrderListViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListViewHolder holder, final int position) {
        OrderBean currentOrder = orderList.get(position);
        String title = currentOrder.getDishDetail().get(0).getTitle();
        int dishCount = currentOrder.getDishDetail().size();
        String dishPo = " dish";
        if(dishCount > 1){
            title += (", " +currentOrder.getDishDetail().get(1).getTitle());
            dishPo += "es";
        }
        holder.getTvOrderTitle().setText(title);
        holder.getTvOrderDate().setText("Created on" + currentOrder.getCreateDate());
        holder.getTvOrderDishQuantity().setText(dishCount + dishPo);
        holder.getTvOrderCharged().setText("$" + currentOrder.getOrderCharged());
        holder.getTvOrderStatus().setText(currentOrder.getStatus());
        holder.getCard().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != listener){
                    listener.startOrderDetail(orderList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(null == this.orderList){
            return 0;
        }
        return orderList.size();
    }

    public void setOrderList(ArrayList<OrderBean> newList){
        if(null == orderList){
            orderList = new ArrayList<>(newList.size());
        }
        this.orderList.clear();
        this.orderList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class OrderListViewHolder extends RecyclerView.ViewHolder{

        private TextView tvOrderTitle, tvOrderDate, tvOrderDishQuantity
                , tvOrderCharged, tvOrderStatus;
        private View card;

        public OrderListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderTitle = (TextView) itemView.findViewById(R.id.tv_order_list_order_title);
            tvOrderDishQuantity = (TextView) itemView.findViewById(R.id.tv_order_list_dish_quantity);
            tvOrderDate = (TextView) itemView.findViewById(R.id.tv_order_list_order_date);
            tvOrderCharged = (TextView) itemView.findViewById(R.id.tv_order_list_total_charged);
            tvOrderStatus = (TextView) itemView.findViewById(R.id.tv_order_list_order_status);
            card = (View) itemView.findViewById(R.id.view_order_list_order);
        }

        public TextView getTvOrderTitle() {
            return tvOrderTitle;
        }

        public TextView getTvOrderDate() {
            return tvOrderDate;
        }

        public TextView getTvOrderDishQuantity() {
            return tvOrderDishQuantity;
        }

        public TextView getTvOrderCharged() {
            return tvOrderCharged;
        }

        public TextView getTvOrderStatus() {
            return tvOrderStatus;
        }

        public View getCard() {
            return card;
        }
    }
}
