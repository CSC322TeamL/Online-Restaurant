package com.example.simplerestaurant.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.Interfaces.UserOrderCartInterface;
import com.example.simplerestaurant.R;
import com.example.simplerestaurant.beans.DishBean;
import com.example.simplerestaurant.beans.DishInCart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public class OrderCartListAdapter extends RecyclerView.Adapter<OrderCartListAdapter.OrderCartViewHolder> {

    private ArrayList<DishInCart> viewData;
    private Map<String, DishBean> nameMap;
    private UserOrderCartInterface listener;

    public OrderCartListAdapter(ArrayList<DishInCart> dishInCarts, Map<String, DishBean> nameMap){
        this.viewData = dishInCarts;
        this.nameMap = nameMap;
    }

    @NonNull
    @Override
    public OrderCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_cart_dish_item, parent, false);
        return new OrderCartViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderCartViewHolder holder, final int position) {
        setDishQuantityPrice(holder, position);
        holder.getTvDishTitle().setText((nameMap.get(viewData.get(position).getDishID())).getTitle());

        holder.getImgbtnQMinus().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int originQ = viewData.get(position).getQuantity();
                viewData.get(position).setQuantity(originQ - 1);
                // update display
                setDishQuantityPrice(holder, position);
                if(null != listener){
                    listener.updateTotalPrice("$" + calculateTotalPrice());
                }
            }
        });

        holder.getImgbtnQPlus().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int originQ = viewData.get(position).getQuantity();
                viewData.get(position).setQuantity(originQ + 1);
                // update display
                setDishQuantityPrice(holder, position);
                if(null != listener){
                    listener.updateTotalPrice("$" + calculateTotalPrice());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(null == this.viewData){
            return 0;
        } else {
            return viewData.size();
        }
    }

    private float calculateTotalPrice(){
        float total = 0;
        for(int i = 0; i < viewData.size(); i++){
            total += getDisplayPrice(i);
        }
        return total;
    }

    public void setOrderCartListener(UserOrderCartInterface listener){
        this.listener = listener;
    }

    public void setUpData(ArrayList<DishInCart> viewData, Map<String, DishBean> nameMap){
        this.viewData = viewData;
        this.nameMap = nameMap;
    }

    public ArrayList<DishInCart> getDishInCart(){
        return this.viewData;
    }

    private void setDishQuantityPrice(OrderCartViewHolder holder, int position){
        float total = getDisplayPrice(position);
        holder.getTvDishPrice().setText("$"+total);
        holder.getTvDishQuantity().setText(String.valueOf(viewData.get(position).getQuantity()));
    }

    private float getDisplayPrice(int position){
        float singlePrice = (nameMap.get(viewData.get(position).getDishID())).getPrice();
        int quantity = viewData.get(position).getQuantity();
        return twoDecimalPrice(singlePrice, quantity);
    }

    private float twoDecimalPrice(float singlePrice, int quantity){
        BigDecimal price = new BigDecimal(singlePrice);
        BigDecimal total = price.multiply(new BigDecimal(quantity));
        total.setScale(2, BigDecimal.ROUND_HALF_UP);
        return total.floatValue();
    }


    // viewholder for item in cart
    public static class OrderCartViewHolder extends RecyclerView.ViewHolder{
        private TextView tvDishTitle, tvDishPrice, tvDishQuantity;
        private ImageButton imgbtnQMinus, imgbtnQPlus, imgbtnRemove;

        public OrderCartViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDishTitle = (TextView) itemView.findViewById(R.id.tv_order_cart_dish_title);
            tvDishPrice = (TextView) itemView.findViewById(R.id.tv_order_cart_dish_price);
            tvDishQuantity = (TextView) itemView.findViewById(R.id.tv_order_cart_quantity);

            imgbtnQMinus = (ImageButton) itemView.findViewById(R.id.imgbtn_order_cart_q_minus);
            imgbtnQPlus = (ImageButton) itemView.findViewById(R.id.imgbtn_order_cart_q_add);
            imgbtnRemove = (ImageButton) itemView.findViewById(R.id.imgbtn_order_cart_dish_remove);
        }

        public TextView getTvDishTitle() {
            return tvDishTitle;
        }

        public TextView getTvDishPrice() {
            return tvDishPrice;
        }

        public TextView getTvDishQuantity() {
            return tvDishQuantity;
        }

        public ImageButton getImgbtnQMinus() {
            return imgbtnQMinus;
        }

        public ImageButton getImgbtnQPlus() {
            return imgbtnQPlus;
        }

        public ImageButton getImgbtnRemove() {
            return imgbtnRemove;
        }
    }
}
