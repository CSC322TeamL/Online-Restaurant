package com.example.simplerestaurant.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.DeliveryDataStore;
import com.example.simplerestaurant.Interfaces.DeliveryActionInterface;
import com.example.simplerestaurant.R;
import com.example.simplerestaurant.beans.AddressBean;
import com.example.simplerestaurant.beans.DishInCart;
import com.example.simplerestaurant.beans.OrderBean;

import java.util.ArrayList;

public class DeliveryOrderListAdapter extends RecyclerView.Adapter<DeliveryOrderListAdapter.DeliveryOrderViewHolder> {
    private ArrayList<OrderBean> orderList;
    private int adapterType;
    private DeliveryActionInterface actionListener;

    public DeliveryOrderListAdapter(int adapterType, ArrayList<OrderBean> orderList) {
        this.adapterType = adapterType;
        setOrderList(orderList);
    }

    public void setActionListener(DeliveryActionInterface actionListener){
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public DeliveryOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_delivery_general_order_item, parent, false);
        return new DeliveryOrderViewHolder(root);
    }

    private String getOrderAddress(int position){
        StringBuilder builder = new StringBuilder();
        AddressBean add = orderList.get(position).getContact().getAddress();
        builder.append(add.getStreet())
                .append(", ")
                .append(add.getCity())
                .append(", ")
                .append(add.getState())
                .append(" ")
                .append(add.getZipCode());
        return builder.toString();
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryOrderViewHolder holder, final int position) {
        int dishes = 0;
        StringBuilder builder = new StringBuilder();
        // concat the dishes titles into one string for displaying
        for (DishInCart dish :
                orderList.get(position).getDishDetail()) {
            int q = dish.getQuantity();
            dishes += q;
            builder.append(dish.getTitle());
            if(q > 1){
                builder.append(" * ");
                builder.append(q);
            }
            builder.append("\n");
        }
        // remove the last newline
        builder.setLength(builder.length() - 1);
        holder.getTvUser().setText(orderList.get(position).getCustomerID());
        holder.getTvDishCount().setText(String.valueOf(dishes));
        holder.getTvDishTitle().setText(builder.toString());
        holder.getTvUserAddress().setText(getOrderAddress(position));
        holder.getTvUserPhone().setText(orderList.get(position).getContact().getPhone());
        // set up different action according to what this adapter is set
        if(adapterType == DeliveryDataStore.TYPE_FINISHED){
            // if this adapter is created by the finished order fragment
            // show the "complaint customer" option and hide the action button
            holder.getvFinished().setVisibility(View.VISIBLE);
            holder.getBtnAction().setVisibility(View.GONE);
            holder.getTvComplaint().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != actionListener){
                        actionListener.onComplaintClick(orderList.get(position).get_id());
                    }
                }
            });
        } else {
            // if this adapter is created by either sending fragment or waiting fragment
            // hide the complaint option
            holder.getvFinished().setVisibility(View.GONE);
            if(adapterType == DeliveryDataStore.TYPE_WAITING){
                // if is created by waiting, change the text accordingly
                holder.getBtnAction().setText("Pick Up");
                holder.getBtnAction().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null != actionListener){
                            actionListener.onPickUpClick(orderList.get(position).get_id());
                        }
                    }
                });
            } else{
                holder.getBtnAction().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null != actionListener){
                            actionListener.onDeliveredClick(orderList.get(position).get_id());
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
    
    public void setOrderList(ArrayList<OrderBean> newList){
        this.orderList = new ArrayList<OrderBean>(newList);
        this.notifyDataSetChanged();
    }

    public static class DeliveryOrderViewHolder extends RecyclerView.ViewHolder{
        private TextView tvUser, tvDishCount, tvDishTitle, tvDeliverDate, tvUserAddress, tvUserPhone;
        private Button btnAction;
        private View vFinished;
        private TextView tvComplaint;
        public DeliveryOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvUser = (TextView) itemView.findViewById(R.id.tv_delivery_general_user);
            tvDishCount = (TextView) itemView.findViewById(R.id.tv_delivery_general_dishes_count);
            tvDishTitle = (TextView) itemView.findViewById(R.id.tv_delivery_general_dishes_list);
            tvDeliverDate = (TextView) itemView.findViewById(R.id.tv_delivery_general_finished_date);
            tvComplaint = (TextView) itemView.findViewById(R.id.tv_delivery_general_complaint);
            tvUserAddress = (TextView) itemView.findViewById(R.id.tv_delivery_general_user_address);
            tvUserPhone = (TextView) itemView.findViewById(R.id.tv_delivery_general_user_phone);
            btnAction = (Button) itemView.findViewById(R.id.button_delivery_general_action);
            vFinished = (View) itemView.findViewById(R.id.view_delivery_general_order_finished_show);
        }

        public TextView getTvUser() {
            return tvUser;
        }

        public TextView getTvDishCount() {
            return tvDishCount;
        }

        public TextView getTvDishTitle() {
            return tvDishTitle;
        }

        public TextView getTvDeliverDate() {
            return tvDeliverDate;
        }

        public Button getBtnAction() {
            return btnAction;
        }

        public View getvFinished() {
            return vFinished;
        }

        public TextView getTvComplaint() {
            return tvComplaint;
        }

        public TextView getTvUserPhone() {
            return tvUserPhone;
        }

        public TextView getTvUserAddress() {
            return tvUserAddress;
        }
    }
}
