package com.example.simplerestaurant.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.Fragments.UserMenuFragment;
import com.example.simplerestaurant.Interfaces.MenuAddCartInterface;
import com.example.simplerestaurant.R;
import com.example.simplerestaurant.beans.DishBean;
import com.example.simplerestaurant.beans.DishInCart;

import java.util.ArrayList;

public class UserMenuListAdapter extends RecyclerView.Adapter<UserMenuListAdapter.MenuItemViewHolder> {
    private ArrayList<UserMenuFragment.UserMenuListBean> viewData;
    private String userType, userID;
    private MenuAddCartInterface listener;

    public UserMenuListAdapter(ArrayList<UserMenuFragment.UserMenuListBean> viewData, String userID, String userType) {
        this.userID = userID;
        this.userType = userType;
        setViewData(viewData);
    }

    public void setViewData(ArrayList<UserMenuFragment.UserMenuListBean> viewData){
        this.viewData = new ArrayList<UserMenuFragment.UserMenuListBean>(viewData);
    }

    public void setAddCartListener(MenuAddCartInterface listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public UserMenuListAdapter.MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_menu_item, parent, false);
        return new MenuItemViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull UserMenuListAdapter.MenuItemViewHolder holder, int position) {
        final UserMenuFragment.UserMenuListBean viewItem = viewData.get(position);
        // bind the data to different view according to the type
        switch (viewItem.getType()){
            case UserMenuFragment.UserMenuListBean.TYPE_MENU:
                holder.getTvMenuTitle().setVisibility(View.VISIBLE);
                holder.getvDish().setVisibility(View.GONE);
                holder.getTvMenuTitle().setText(viewItem.getTitle());
                break;
            case UserMenuFragment.UserMenuListBean.TYPE_DISH:
                DishBean dish = viewItem.getDish();
                holder.getTvDishTitle().setText(dish.getTitle());
                holder.getTvDishPrice().setText("$" + dish.getPrice());
                holder.getTvDishRating().setText(String.valueOf(dish.getDigitRating()));
                holder.getTvDishRatingCount().setText(String.valueOf(dish.getRatings().size()));
                break;
        }
        if(userType.equals("Customer") || userType.equals("VIP")){
            holder.getBtnAdd2Cart().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != listener){
                        DishInCart newItem = new DishInCart();
                        DishBean dish = viewItem.getDish();
                        newItem.setDishID(dish.get_id());
                        newItem.setQuantity(1);
                        listener.dishAdd2Cart(newItem, dish.getTitle());
                    }
                }
            });
        } else {

            holder.getBtnAdd2Cart().setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        if(null == viewData){
            return 0;
        } else {
            return viewData.size();
        }
    }

    // viewholder class the recycler view
    public static class MenuItemViewHolder extends RecyclerView.ViewHolder{
        private TextView tvMenuTitle;
        private TextView tvDishTitle, tvDishRating, tvDishRatingCount, tvDishPrice;
        private ImageButton btnAdd2Cart;
        private View vDish;


        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDishTitle = (TextView) itemView.findViewById(R.id.textview_menu_item_dish_title);
            tvDishRating = (TextView) itemView.findViewById(R.id.textview_menu_item_dish_rating);
            tvDishRatingCount = (TextView) itemView.findViewById(R.id.textview_menu_item_dish_rating_count);
            tvDishPrice = (TextView) itemView.findViewById(R.id.textview_menu_item_dish_price);
            btnAdd2Cart = (ImageButton) itemView.findViewById(R.id.button_menu_item_add);
            tvMenuTitle = (TextView) itemView.findViewById(R.id.textview_menu_item_title);
            vDish = (View) itemView.findViewById(R.id.view_menu_item_dish);
        }

        public TextView getTvDishTitle() {
            return tvDishTitle;
        }

        public TextView getTvDishRating() {
            return tvDishRating;
        }

        public TextView getTvDishRatingCount() {
            return tvDishRatingCount;
        }

        public TextView getTvDishPrice() {
            return tvDishPrice;
        }

        public ImageButton getBtnAdd2Cart() {
            return btnAdd2Cart;
        }

        public TextView getTvMenuTitle() {
            return tvMenuTitle;
        }

        public View getvDish() {
            return vDish;
        }
    }
}


