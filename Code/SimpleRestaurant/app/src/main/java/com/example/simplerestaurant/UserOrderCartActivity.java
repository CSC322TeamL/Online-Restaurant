package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.Adapters.OrderCartListAdapter;
import com.example.simplerestaurant.Fragments.UserMenuFragment;
import com.example.simplerestaurant.Interfaces.UserOrderCartInterface;
import com.example.simplerestaurant.beans.DishBean;
import com.example.simplerestaurant.beans.DishInCart;
import com.example.simplerestaurant.beans.MenuBean;
import com.example.simplerestaurant.beans.OrderBean;
import com.example.simplerestaurant.beans.UserMenuListBean;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserOrderCartActivity extends BaseActivity implements View.OnClickListener ,
        UserOrderCartInterface {

    private RecyclerView dishRecycler;
    private TextView tvOrderTotal;
    private View navigationBar, summaryBar;
    private ImageButton imgbtnBack, imgbtnEmpty;
    private Button btnPlace;

    private View afterSubmit, orderFroze, orderSuccess;

    private OrderBean currentOrder;
    private ArrayList<UserMenuListBean> dishDetail;
    private Map<String, DishBean> dishMap;

    private OrderCartListAdapter orderCartListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_cart);

        dishRecycler = (RecyclerView) findViewById(R.id.recycler_order_cart);
        tvOrderTotal = (TextView) findViewById(R.id.tv_order_cart_total);
        btnPlace = (Button) findViewById(R.id.button_order_cart_place);

        navigationBar = (View) findViewById(R.id.view_order_cart_navigation);
        imgbtnBack = (ImageButton) navigationBar.findViewById(R.id.imagebtn_backward);
        imgbtnEmpty = (ImageButton) navigationBar.findViewById(R.id.imagebtn_cart_empty);

        summaryBar = (View) findViewById(R.id.view_order_cart_summary);
        afterSubmit = (View) findViewById(R.id.view_order_cart_in_response);
        orderFroze = (View) findViewById(R.id.view_order_submit_freeze);
        orderSuccess = (View) findViewById(R.id.view_order_submit_success);

        btnPlace.setOnClickListener(this);
        imgbtnBack.setOnClickListener(this);
        imgbtnEmpty.setOnClickListener(this);

        Intent intent = getIntent();
        String order = intent.getStringExtra("order");
        String dishDetailStr = intent.getStringExtra("dishes");

        this.currentOrder = UnitTools.getGson().fromJson(order, OrderBean.class);
        Type menuListType = new TypeToken<ArrayList<UserMenuListBean>>(){}.getType();
        this.dishDetail = UnitTools.getGson().fromJson(dishDetailStr, menuListType);
        setDishMap();

        orderCartListAdapter = new OrderCartListAdapter(new ArrayList<DishInCart>(currentOrder.getDishDetail()), dishMap);
        orderCartListAdapter.setOrderCartListener(this);
        dishRecycler.setAdapter(orderCartListAdapter);
        dishRecycler.setLayoutManager(new LinearLayoutManager(this));

        tvOrderTotal.setText("$" + currentOrder.getOrderTotal());

    }

    // set up a dishID-dishBean map
    private void setDishMap(){
        if(null == dishMap){
            dishMap = new HashMap<>(dishDetail.size());
        }
        for (UserMenuListBean bean :
                dishDetail) {
            if(bean.getType() == UserMenuListBean.TYPE_DISH){
                dishMap.put(bean.getDish().get_id(), bean.getDish());
            }
        }
    }

    private DishBean getDishByID(String dishID){
        for (UserMenuListBean menuItem:
            dishDetail) {
            if(menuItem.getType() == UserMenuListBean.TYPE_DISH &&
            menuItem.getDish().get_id().equals(dishID)){
                return menuItem.getDish();
            }
        }
        return null;
    }

    private void orderStatus(String res){
        //Log.i("order", res);
        dishRecycler.setVisibility(View.GONE);
        summaryBar.setVisibility(View.GONE);
        afterSubmit.setVisibility(View.VISIBLE);
        OrderSubmitResult result = UnitTools.getGson().fromJson(res, OrderSubmitResult.class);
        if(result.getCode() == 0){
            toastMessage(result.getContent());
            this.finish();
        } else if(result.getCode() == 1){
            orderSuccess.setVisibility(View.GONE);
            orderFroze.setVisibility(View.VISIBLE);
        }
    }

    private void upload2server(String order){
        String url = getString(R.string.base_url) + "/place_order";
        RequestBody body = RequestBody.create(order, MediaType.parse(UnitTools.TYPE_JSON));
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = UnitTools.getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Failed to connect to server");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string().trim();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        orderStatus(res);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_order_cart_place:
                currentOrder.setDishDetail(orderCartListAdapter.getDishInCart());
                String orderStr = UnitTools.getGson().toJson(currentOrder);
                upload2server(orderStr);
                break;
            case R.id.imagebtn_backward:
                this.finish();
                break;
            case R.id.imagebtn_cart_empty:
                this.currentOrder.getDishDetail().clear();
                orderCartListAdapter.setUpData(new ArrayList<DishInCart>(currentOrder.getDishDetail()), dishMap);
                orderCartListAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void updateTotalPrice(String newPrice) {
        tvOrderTotal.setText(newPrice);
    }

    public class OrderSubmitResult{
        int code;
        String content;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
