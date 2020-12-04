package com.example.simplerestaurant.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.Adapters.UserMenuListAdapter;
import com.example.simplerestaurant.Interfaces.MenuAddCartInterface;
import com.example.simplerestaurant.Interfaces.UserMenuFragmentInterface;
import com.example.simplerestaurant.R;
import com.example.simplerestaurant.UnitTools;
import com.example.simplerestaurant.beans.DishBean;
import com.example.simplerestaurant.beans.DishInCart;
import com.example.simplerestaurant.beans.MenuBean;
import com.example.simplerestaurant.beans.OrderBean;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserMenuFragment extends Fragment implements MenuAddCartInterface, View.OnClickListener {
    private String userID, userType;
    private RecyclerView menuRecycler;
    private Button btnViewCart;
    private UserMenuFragmentInterface menuListener;
    private ArrayList<MenuBean> menuList;
    private UserMenuListAdapter menuAdapter;
    private ArrayList<UserMenuListBean> viewData;
    private ArrayList<DishInCart> dishesInCart;

    public UserMenuFragment(UserMenuFragmentInterface menuListener){
        super(R.layout.fragment_user_main_menu);
        this.menuListener = menuListener;
        dishesInCart = new ArrayList<DishInCart>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userType = requireArguments().getString("userType");
        userID = requireArguments().getString("userID");

        menuRecycler = (RecyclerView) view.findViewById(R.id.recycler_main_menu);
        btnViewCart = (Button) view.findViewById(R.id.btn_menu_view_cart);

        if(!userType.equals("Customer") && !userType.equals("VIP")){
            btnViewCart.setVisibility(View.GONE);
            // change the margin of the recyclerview
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) menuRecycler.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            menuRecycler.setLayoutParams(params);
        } else {
            btnViewCart.setOnClickListener(this);
        }

        viewData = new ArrayList<UserMenuListBean>(0);
        menuAdapter = new UserMenuListAdapter(viewData, userID, userType);
        menuAdapter.setAddCartListener(this);
        menuRecycler.setAdapter(menuAdapter);
        menuRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(null != menuListener){
            // call the activity to get the menu info from server
            menuListener.getMenuFromServer(userID);
        }
    }

    public void setGetMenuListener(UserMenuFragmentInterface listener){
        menuListener = listener;
    }

    /**
     * semi callback function to receive the data from server
     * @param res
     */
    public void menuResponse(String res){
        //Log.i("menu", res);
        setUpListView(res);
    }

    private void setUpListView(String res){
        ArrayList<MenuBean> menus = convertJson2MenuList(res);
        viewData = menus2ViewList(menus);
        if(null != menuAdapter){
            menuAdapter.setViewData(viewData);
            menuAdapter.notifyDataSetChanged();
        }
    }
    
    /**
     * take in the json res and return the list of menuBean objects
     * @param res the response from server, should be in json format
     * @return
     */
    private ArrayList<MenuBean> convertJson2MenuList(String res){
        // get the type of the array
        Type menuListType = new TypeToken<ArrayList<MenuBean>>(){}.getType();
        // converting
        menuList = UnitTools.getGson().fromJson(res, menuListType);
        Log.i("menu", menuList.toString());

        return menuList;
    }
    
    private ArrayList<UserMenuListBean> menus2ViewList(ArrayList<MenuBean> menuList){
        ArrayList<UserMenuListBean> viewList = new ArrayList<>();
        for(int i =0; i < menuList.size(); i++){
            MenuBean menu = menuList.get(i);
            List<DishBean> dishes = menu.getDishes();
            UserMenuListBean menuTitle = new UserMenuListBean(menu.getTitle());
            viewList.add(menuTitle);
            for (DishBean dish: dishes
                 ) {
                UserMenuListBean menuDish = new UserMenuListBean(dish);
                viewList.add(menuDish);
            }
        }
        return viewList;
    }

    private float getDishPrice(String dishID){
        for (UserMenuListBean dish :
                viewData) {
            if(dish.getType() == UserMenuListBean.TYPE_DISH && dish.getDish().get_id().equals(dishID)){
                return dish.getDish().getPrice();
            }
        }
        return 0;
    }

    private OrderBean processOrder(){
        float sum = 0;
        for (DishInCart cartDish:
             dishesInCart) {
            sum += getDishPrice(cartDish.getDishID());
        }
        OrderBean newOrder = new OrderBean();
        newOrder.setCustomerID(userID);
        newOrder.setOrderTotal(sum);
        newOrder.setDishDetail(dishesInCart);
        return newOrder;
    }

    @Override
    public void dishAdd2Cart(DishInCart newDish, String dishName) {
        //if the dish is already in cart, then increase the count
        for (DishInCart cartDish :
                dishesInCart) {
            if (cartDish.getDishID().equals(newDish.getDishID())){
                cartDish.setQuantity(cartDish.getQuantity() + newDish.getQuantity());
                Log.i("toCart", String.valueOf(cartDish.getQuantity()));
                Toast.makeText(getActivity(), dishName + " #" + cartDish.getQuantity(), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        // else, add to the list
        dishesInCart.add(newDish);
        Log.i("toCart", String.valueOf(newDish.getQuantity()));
        Toast.makeText(getActivity(), dishName + " Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_menu_view_cart){
            String res = UnitTools.getGson().toJson(processOrder());
            Log.i("order", res);
        }
    }

    public class UserMenuListBean{
        // use type to indicate which viewholder should be applied
        final public static int TYPE_MENU = 0;
        final public static int TYPE_DISH = 1;
        private int type;
        private String title;
        private DishBean dish;

        public UserMenuListBean(String title){
            this.type = TYPE_MENU;
            this.title = title;
            this.dish = null;
        }
        
        public UserMenuListBean(DishBean dish){
            this.type = TYPE_DISH;
            this.dish = dish;
            this.title = null;
        }
        
        public int getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public DishBean getDish() {
            return dish;
        }
    }
}
