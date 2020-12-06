package com.example.simplerestaurant.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.R;
import com.example.simplerestaurant.beans.DishInCart;
import com.example.simplerestaurant.beans.OrderBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class OrderHistoryDIshListAdapter extends RecyclerView.Adapter<OrderHistoryDIshListAdapter.DishHisListViewHolder> {

    private String userID, userType;
    private OrderBean currentOrder;
    private ArrayList<DishInCart> dishList;
    private double discountNum;

    public OrderHistoryDIshListAdapter(String userID, String userType, OrderBean currentOrder) {
        this.userID = userID;
        this.userType = userType;
        this.currentOrder = currentOrder;
        this.dishList= new ArrayList<>(currentOrder.getDishDetail().size());
        dishList.addAll(currentOrder.getDishDetail());
        if(currentOrder.getDiscount() == 0){
            discountNum = 0;
        } else {
            this.discountNum = 1.0 - (currentOrder.getOrderCharged()/currentOrder.getOrderTotal());
        }
    }

    @NonNull
    @Override
    public DishHisListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dish_in_order, parent, false);
        return new DishHisListViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull DishHisListViewHolder holder, int position) {
        String title = dishList.get(position).getTitle();
        int quantity = dishList.get(position).getQuantity();
        if(quantity > 1){
            title += (" * " + quantity);
        }
        holder.getTvDishTitle().setText(title);
        holder.getTvDishPrice().setText("$" + dishList.get(position).getPrice());
        holder.getTvDishCharged().setText("$" + getDiscountPrice(dishList.get(position).getPrice()));

        String note = dishList.get(position).getSpecialNote();
        if(!note.equals("") || !note.isEmpty()){
            holder.getTvDishNote().setVisibility(View.VISIBLE);
            holder.getTvDishNote().setText(note);
        }
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    private float getDiscountPrice(float price){
        if(this.discountNum == 0){
            return price;
        }
        BigDecimal orPrice = new BigDecimal(price);
        BigDecimal result = orPrice.multiply(new BigDecimal(this.discountNum));
        result.setScale(2, BigDecimal.ROUND_HALF_UP);
        return result.floatValue();
    }

    public static class DishHisListViewHolder extends RecyclerView.ViewHolder{
        private TextView tvDishTitle, tvDishPrice, tvDishCharged, tvDishNote;
        private Button btnRate;
        public DishHisListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDishTitle = (TextView) itemView.findViewById(R.id.tv_dish_in_order_title);
            tvDishPrice = (TextView) itemView.findViewById(R.id.tv_dish_in_order_price);
            tvDishCharged = (TextView) itemView.findViewById(R.id.tv_dish_in_order_charged);
            tvDishNote = (TextView) itemView.findViewById(R.id.tv_dish_in_order_note);
            btnRate = (Button) itemView.findViewById(R.id.btn_dish_in_order_rate);
        }

        public TextView getTvDishTitle() {
            return tvDishTitle;
        }

        public TextView getTvDishPrice() {
            return tvDishPrice;
        }

        public TextView getTvDishCharged() {
            return tvDishCharged;
        }

        public Button getBtnRate() {
            return btnRate;
        }

        public TextView getTvDishNote() {
            return tvDishNote;
        }
    }
}
