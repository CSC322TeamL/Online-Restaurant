package com.example.simplerestaurant.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.example.simplerestaurant.UserOrderCartActivity;
import com.example.simplerestaurant.beans.DishBean;
import com.example.simplerestaurant.beans.DishInCart;
import com.example.simplerestaurant.beans.MenuBean;
import com.example.simplerestaurant.beans.OrderBean;
import com.example.simplerestaurant.beans.UserMenuListBean;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserMenuFragment extends Fragment implements MenuAddCartInterface
        , View.OnClickListener
        , TextWatcher {
    private String userID, userType;
    private RecyclerView menuRecycler;
    private Button btnViewCart;
    private EditText etKeyword;
    private View vSearch;
    private UserMenuFragmentInterface menuListener;
    private ArrayList<MenuBean> menuList;
    private UserMenuListAdapter menuAdapter;
    private ArrayList<UserMenuListBean> viewData;
    private ArrayList<DishInCart> dishesInCart;

    private int popUpDishQuantity;
    private ImageButton popUpDishPlus, popUpDishMinus;
    private Button popUpDishAdd2Cart;
    private EditText popUpNote;
    private DishInCart popUpDishInCart;
    private TextView popUpTotalPrice, popUpQ;
    private View popUpBackground;
    private PopupWindow popupWindow;

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
        etKeyword = (EditText) view.findViewById(R.id.et_main_menu_search_keyword);
        vSearch = (View) view.findViewById(R.id.view_main_menu_search_click);

        vSearch.setOnClickListener(this);
        etKeyword.addTextChangedListener(this);

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
        viewData = new ArrayList<>(menus2ViewList(menus));
        if(null != menuAdapter){
            menuAdapter.setViewData(viewData);
            menuAdapter.notifyDataSetChanged();
        }
    }

    public void setMenuList(ArrayList<MenuBean> menuList){
        viewData = new ArrayList<>(menus2ViewList(menuList));
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
        //Log.i("menu", res);

        return menuList;
    }

    /**
     * prepare the object array for the recycler view
     * @param menuList
     * @return
     */
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
        Log.i("menu", viewList.toString());
        return viewList;
    }

    /**
     * get dish price by dishID
     * @param dishID
     * @return
     */
    private float getDishPrice(String dishID){
        for (UserMenuListBean dish :
                viewData) {
            if(dish.getType() == UserMenuListBean.TYPE_DISH && dish.getDish().get_id().equals(dishID)){
                return dish.getDish().getPrice();
            }
        }
        return 0;
    }

    /**
     * prepare the order bean that being passed to the order activity
     * @return
     */
    private OrderBean processOrder(){
        float sum = 0;
        for (DishInCart cartDish:
             dishesInCart) {
            sum += (getDishPrice(cartDish.getDishID()) * cartDish.getQuantity());
        }
        OrderBean newOrder = new OrderBean();
        newOrder.setCustomerID(userID);
        newOrder.setOrderTotal(sum);
        newOrder.setDishDetail(dishesInCart);
        return newOrder;
    }

    private DishInCart findDishInCart(String dishID){
        for (DishInCart dish :
                dishesInCart) {
            if (dishID.equals(dish.getDishID())){
                return dish;
            }
        }
        return null;
    }

    private DishBean findDish(String dishID){
        for (UserMenuListBean item :
                viewData) {
            if(item.getType() == UserMenuListBean.TYPE_DISH){
                if(dishID.equals(item.getDish().get_id())){
                    return item.getDish();
                }
            }
        }
        return null;
    }

    /**
     * this is used to update the dishInCart
     * when change is made in the order cart activity
     * @param newList
     */
    public void setDishesInCart(ArrayList<DishInCart> newList){
        this.dishesInCart = newList;
    }

    /**
     * implementing interface
     * when a dish is added or its quantity changes
     * this interface will notify the fragment to update the change
     * @param newDish
     * @param dishName
     */
    @Override
    public void dishAdd2Cart(DishInCart newDish, String dishName) {
        //if the dish is already in cart, then increase the count
        for (DishInCart cartDish :
                dishesInCart) {
            if (cartDish.getDishID().equals(newDish.getDishID())){
                cartDish.setQuantity(cartDish.getQuantity() + newDish.getQuantity());
                Toast.makeText(getActivity(), dishName + " #" + cartDish.getQuantity(), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        // else, add to the list
        dishesInCart.add(newDish);
        Toast.makeText(getActivity(), dishName + " Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dishPopupWindow(String dishID) {
        DishBean dish = findDish(dishID);
        if(null == dish){
            return;
        }
        popUpDishInCart = findDishInCart(dishID);
        if(null == popUpDishInCart){
            popUpDishInCart = new DishInCart();
            popUpDishInCart.setDishID(dishID);
            popUpDishInCart.setTitle(dish.getTitle());
            popUpDishInCart.setPrice(dish.getPrice());
            popUpDishInCart.setQuantity(1);
            popUpDishInCart.setSpecialNote("");
        }

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View root = inflater.inflate(R.layout.popup_dish_detail, null);

        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = width;
        popupWindow = new PopupWindow(root, width, height, true);

        TextView tvTitle, tvRating, tvRatingCount, tvPrice, tvDescription;

        tvTitle = root.findViewById(R.id.tv_dish_detail_title);
        tvRating = root.findViewById(R.id.tv_dish_detail_rating);
        tvRatingCount = root.findViewById(R.id.tv_dish_detail_rating_count);
        tvPrice = root.findViewById(R.id.tv_dish_detail_price);
        popUpTotalPrice = root.findViewById(R.id.tv_dish_detail_total_price);
        tvDescription = root.findViewById(R.id.tv_dish_detail_description);
        popUpQ = root.findViewById(R.id.tv_dish_detail_quantity);
        popUpBackground = root.findViewById(R.id.view_popup_background);

        popUpDishPlus = root.findViewById(R.id.imgbtn_dish_detail_q_add);
        popUpDishMinus = root.findViewById(R.id.imgbtn_dish_detail_q_minus);
        popUpDishAdd2Cart = root.findViewById(R.id.button_dish_detail_add_cart);

        popUpNote = root.findViewById(R.id.et_dish_detail_note);

        tvTitle.setText(popUpDishInCart.getTitle());
        tvRating.setText(String.valueOf(dish.getDigitRating()));
        tvRatingCount.setText(String.valueOf(dish.getRatings()));
        tvPrice.setText("$"+ dish.getPrice());
        popUpTotalPrice.setText("$" + calculateTotalPrice(dish.getPrice(), popUpDishInCart.getQuantity()));
        popUpQ.setText(String.valueOf(popUpDishInCart.getQuantity()));
        tvDescription.setText(dish.getDescription());

        popUpNote.setText(popUpDishInCart.getSpecialNote());

        popUpDishQuantity = popUpDishInCart.getQuantity();
        popUpDishPlus.setOnClickListener(this);
        popUpDishMinus.setOnClickListener(this);
        popUpDishAdd2Cart.setOnClickListener(this);
        popUpBackground.setOnClickListener(this);
        popupWindow.showAtLocation(root, Gravity.CENTER, 0, 0);
    }

    private float calculateTotalPrice(float singlePrice, int quantity){
        BigDecimal bigDecimal = new BigDecimal(singlePrice);
        BigDecimal result = bigDecimal.multiply(new BigDecimal(quantity));
        result.setScale(2, BigDecimal.ROUND_HALF_UP);
        return result.floatValue();
    }

    private int getDishInCartPosition(String dishID){
        int length = dishesInCart.size();
        for(int i = 0; i < length; i++){
            if (dishID.equals(dishesInCart.get(i).getDishID())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_menu_view_cart){
            String order = UnitTools.getGson().toJson(processOrder());
            String dishes = UnitTools.getGson().toJson(viewData);
            Intent intent = new Intent(getActivity(), UserOrderCartActivity.class);
            intent.putExtra("order",order);
            intent.putExtra("dishes", dishes);
            getActivity().startActivityForResult(intent, UnitTools.REQUEST_USER_ORDER_CART);
        } else if(popUpDishPlus != null && v.getId() == popUpDishPlus.getId()){
            popUpDishQuantity++;
            popUpQ.setText(String.valueOf(popUpDishQuantity));
            popUpTotalPrice.setText("$" + calculateTotalPrice(popUpDishInCart.getPrice(), popUpDishQuantity));
        } else if(popUpDishMinus != null && v.getId() == popUpDishMinus.getId()){
            if(popUpDishQuantity > 1){
                popUpDishQuantity--;
                popUpQ.setText(String.valueOf(popUpDishQuantity));
                popUpTotalPrice.setText("$" + calculateTotalPrice(popUpDishInCart.getPrice(), popUpDishQuantity));
            }
        } else if(popUpDishAdd2Cart != null && v.getId() == popUpDishAdd2Cart.getId()){
            popUpDishInCart.setQuantity(popUpDishQuantity);
            popUpDishInCart.setSpecialNote(popUpNote.getText().toString().trim());
            int position = getDishInCartPosition(popUpDishInCart.getDishID());
            if(position == -1){
                dishesInCart.add(popUpDishInCart);
            } else {
                dishesInCart.remove(position);
                dishesInCart.add(popUpDishInCart);
            }
            Log.i("cart", dishesInCart.toString());
            popupWindow.dismiss();
        } else if(null != popUpBackground && v.getId() == popUpBackground.getId()){
            if(null != popupWindow){
                popupWindow.dismiss();
            }
        } else if(v.getId() == R.id.view_main_menu_search_click){
            String keyword = etKeyword.getText().toString().trim();
            if(keyword.isEmpty() || keyword.equals("")){
                return;
            }
            if(null != menuListener){
                menuListener.searchMenu(userID, userType, keyword);
            }
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String keyword = s.toString();
        if(null == keyword || keyword.isEmpty() || keyword.equals("")){
            if(null != menuListener){
                menuListener.getMenuFromServer(userID);
            }
        }
    }
}
