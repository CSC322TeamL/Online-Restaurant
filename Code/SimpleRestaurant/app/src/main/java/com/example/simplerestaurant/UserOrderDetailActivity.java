package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplerestaurant.Adapters.OrderHistoryDIshListAdapter;
import com.example.simplerestaurant.Interfaces.UserDishRatingInterface;
import com.example.simplerestaurant.beans.OrderBean;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserOrderDetailActivity extends BaseActivity implements UserDishRatingInterface ,
    View.OnClickListener{

    private OrderBean currentOrder;
    private TextView tvOrderID, tvOrderCreated, tvOrderCharged, tvDishQuantity, tvOrderStatus;
    private RecyclerView dishRecycler;
    private OrderHistoryDIshListAdapter adapter;
    private ImageButton imgbtnBack;
    private PopupWindow myPopUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_detail);
        Intent intent = getIntent();
        String orderStr = intent.getStringExtra("order");
        String userID = intent.getStringExtra("userID");
        String userType = intent.getStringExtra("userType");
        this.currentOrder = UnitTools.getGson().fromJson(orderStr, OrderBean.class);

        tvOrderID = (TextView) findViewById(R.id.tv_user_order_detail_id);
        tvOrderCreated = (TextView) findViewById(R.id.tv_user_order_detail_date);
        tvOrderCharged = (TextView) findViewById(R.id.tv_user_order_detail_charged);
        tvDishQuantity = (TextView) findViewById(R.id.tv_user_order_detail_quantity);
        tvOrderStatus = (TextView) findViewById(R.id.tv_user_order_detail_status);

        tvOrderID.setText(currentOrder.get_id());
        tvOrderCreated.setText(currentOrder.getCreateDate());
        tvOrderCharged.setText("$" + currentOrder.getOrderCharged());
        tvDishQuantity.setText(String.valueOf(currentOrder.getDishDetail().size()));
        tvOrderStatus.setText(currentOrder.getStatus());

        imgbtnBack = (ImageButton) findViewById(R.id.imagebtn_backward);
        imgbtnBack.setOnClickListener(this);

        dishRecycler = (RecyclerView) findViewById(R.id.recycler_user_order_detail);
        adapter = new OrderHistoryDIshListAdapter(userID, userType, currentOrder);
        adapter.setRatingListener(this);
        dishRecycler.setAdapter(adapter);
        dishRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void submitRating2Server(String userID, String dishID, final int rating){
        String url = getString(R.string.base_url) + "/rating";
        // get OkHttp3 client from the base activity
        OkHttpClient client = UnitTools.getOkHttpClient();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", userID);
        bodyBuilder.add("dishID", dishID);
        bodyBuilder.add("rating", String.valueOf(rating));
        final Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = client.newCall(request);
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
                        ratingResponse(res);
                    }
                });
            }
        });
    }

    private void ratingResponse(String res){
        if(res.equals("0")){
            if(myPopUp != null){
                myPopUp.dismiss();
            }
        } else {
            toastMessage("Can't submit your rating");
        }
    }

    private void popupWindow4Rating(final String userID, final String dishID, String dishTitle){
        int currentRating = 1;
        //build up the popup window here
        LayoutInflater inflater = LayoutInflater.from(this);
        View root = inflater.inflate(R.layout.popup_dish_rating, null);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        myPopUp = new PopupWindow(root, width, height, focusable);
        myPopUp.showAtLocation(root, Gravity.CENTER, 0,0);
        myPopUp.setOutsideTouchable(true);
        // get views
        TextView title = (TextView) root.findViewById(R.id.popup_dish_rating_title);
        final RatingBar ratingBar = (RatingBar) root.findViewById(R.id.ratingbar_popup_rating);
        Button submit = (Button) root.findViewById(R.id.btn_popup_submit);

        title.setText(dishTitle);
        ratingBar.setStepSize(1);
        ratingBar.setRating(currentRating);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRating2Server(userID, dishID, ((int)ratingBar.getRating()));
            }
        });

    }

    @Override
    public void openRatingPopupWindow(String userID, String dishID, String dishTitle) {
        popupWindow4Rating(userID, dishID, dishTitle);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imagebtn_backward:
                this.finish();
                break;
        }
    }
}
