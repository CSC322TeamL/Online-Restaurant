package com.example.simplerestaurant;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.simplerestaurant.Fragments.UserAccountFragment;
import com.example.simplerestaurant.Fragments.UserDiscussionFragment;
import com.example.simplerestaurant.Fragments.UserMenuFragment;
import com.example.simplerestaurant.Fragments.UserOrderFragment;
import com.example.simplerestaurant.Interfaces.UserAccountFragmentInterface;
import com.example.simplerestaurant.Interfaces.UserDiscussionFragmentInterface;
import com.example.simplerestaurant.Interfaces.UserMenuFragmentInterface;
import com.example.simplerestaurant.Interfaces.UserOrderFragmentInterface;
import com.example.simplerestaurant.beans.DishBean;
import com.example.simplerestaurant.beans.DishInCart;
import com.example.simplerestaurant.beans.MenuBean;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserMainPageActivity extends BaseActivity implements UserMenuFragmentInterface
        , UserAccountFragmentInterface
        , UserDiscussionFragmentInterface
        , UserOrderFragmentInterface {

    final private static String MENU_TAG = "fragment_user_menu";
    final private static String ORDER_TAG = "fragment_user_order";
    final private static String DISC_TAG = "fragment_user_discussion";
    final private static String ACC_TAG = "fragment_user_account";

    private String userType;
    private String userID;
    private BottomNavigationView navigationView;
    private OkHttpClient client;

    private UserMenuFragment userMenuFragment;
    private UserAccountFragment userAccountFragment;
    private UserDiscussionFragment userDiscussionFragment;
    private UserOrderFragment userOrderFragment;

    private Fragment activeFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_page);

        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setCurrentFragment(item.getItemId());
                return true;
            }
        });
        client = UnitTools.getOkHttpClient();

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        userType = intent.getStringExtra("userType");

        if(userType.equals("Surfer")){
            navigationView.getMenu().findItem(R.id.nav_user_main_account).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_user_main_order).setVisible(false);
        }

        fragmentManager = getSupportFragmentManager();
        userMenuFragment = (UserMenuFragment)findFragmentByTag(MENU_TAG);
        activeFragment = userMenuFragment;
        setCurrentFragment(R.id.nav_user_main_menu);
    }

    /**
     * display different page base on which button in the bottom bar is clicked
     * @param navID
     */
    private void setCurrentFragment(int navID){
        switch (navID){
            case R.id.nav_user_main_menu:
                if(null == userMenuFragment){
                    userMenuFragment = (UserMenuFragment)findFragmentByTag(MENU_TAG);
                }
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(userMenuFragment)
                        .commit();
                activeFragment = userMenuFragment;
                break;

            case R.id.nav_user_main_discussion:
                if(null == userDiscussionFragment){
                    userDiscussionFragment = (UserDiscussionFragment) findFragmentByTag(DISC_TAG);
                }
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(userDiscussionFragment)
                        .commit();
                activeFragment = userDiscussionFragment;
                break;

            case R.id.nav_user_main_order:
                if(null == userOrderFragment){
                    userOrderFragment = (UserOrderFragment) findFragmentByTag(ORDER_TAG);
                }
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(userOrderFragment)
                        .commit();
                activeFragment = userOrderFragment;
                break;
            case R.id.nav_user_main_account:
                if(null == userAccountFragment){
                    userAccountFragment = (UserAccountFragment) findFragmentByTag(ACC_TAG);
                }
                fragmentManager.beginTransaction()
                        .hide(activeFragment)
                        .show(userAccountFragment)
                        .commit();
                activeFragment = userAccountFragment;
                break;
        }

    }

    /**
     * handle the server response for getting the menu
     * @param res
     */
    private void menuResponse(String res){
        if(null == userMenuFragment){
            userMenuFragment = (UserMenuFragment) findFragmentByTag(MENU_TAG);
        }
        userMenuFragment.menuResponse(res);
    }

    /**
     * handle the server response for getting user info
     * @param res
     */
    private void accInfoResponse(String res){
        if(null == userAccountFragment){
            userAccountFragment = (UserAccountFragment) findFragmentByTag(ACC_TAG);
        }
        userAccountFragment.userInfoResponse(res);
    }

    private void orderListResponse(String res){
        if(null == userOrderFragment){
            userOrderFragment = (UserOrderFragment) findFragmentByTag(ORDER_TAG);
        }
        userOrderFragment.orderListResponse(res);
    }

    private void discussionResponse(String res){
        if(null == userDiscussionFragment){
            userDiscussionFragment = (UserDiscussionFragment) findFragmentByTag(DISC_TAG);
        }
        userDiscussionFragment.discussionResponse(res);
    }

    private void searchMenuResponse(String res){
        try {
            JSONObject jsonObject = new JSONObject(res);
            Type dishList = new TypeToken<ArrayList<DishBean>>(){}.getType();
            ArrayList<DishBean> dishes = UnitTools.getGson()
                    .fromJson(jsonObject.getString("result").toString(), dishList);
            MenuBean resultMenu = new MenuBean();
            resultMenu.setTitle("Search");
            resultMenu.setDishes(dishes);
            ArrayList<MenuBean> menus = new ArrayList<>(1);
            menus.add(resultMenu);
            if(null != userMenuFragment){
                userMenuFragment.setMenuList(menus);
            }
        }catch (Exception e){
            e.printStackTrace();
            toastMessage("Failed to search");
        }
    }

    /**
     * get the existing fragment by tag
     * or create the new one if the fragment is not exist
     * @param tag
     * @return
     */
    private Fragment findFragmentByTag(String tag){
        Fragment result = (Fragment) getSupportFragmentManager().findFragmentByTag(tag);
        if(null == result){
            switch (tag){
                case MENU_TAG:
                    if(null == userMenuFragment){
                        userMenuFragment = new UserMenuFragment(this);
                        Bundle bundle = new Bundle();
                        bundle.putString("userType", userType);
                        bundle.putString("userID", userID);
                        userMenuFragment.setArguments(bundle);
                        userMenuFragment.setGetMenuListener(this);
                        if(!userMenuFragment.isAdded()){
                            fragmentManager.beginTransaction()
                                    .add(R.id.view_user_main_for_replace, userMenuFragment, MENU_TAG)
                                    .hide(userMenuFragment)
                                    .commit();
                        }
                        return userMenuFragment;
                    }
                    break;
                case DISC_TAG:
                    if(null == userDiscussionFragment){
                        userDiscussionFragment = new UserDiscussionFragment(this);
                        Bundle bundle = new Bundle();
                        bundle.putString("userType", userType);
                        bundle.putString("userID", userID);
                        userDiscussionFragment.setArguments(bundle);
                        if(!userDiscussionFragment.isAdded()){
                            fragmentManager.beginTransaction()
                                    .add(R.id.view_user_main_for_replace, userDiscussionFragment, DISC_TAG)
                                    .hide(userDiscussionFragment)
                                    .commit();
                        }
                        return userDiscussionFragment;
                    }
                    break;
                case ACC_TAG:
                    if(null == userAccountFragment){
                        userAccountFragment = new UserAccountFragment(this);
                        Bundle bundle = new Bundle();
                        bundle.putString("userID", userID);
                        bundle.putString("userType", userType);
                        userAccountFragment.setArguments(bundle);
                        if(!userAccountFragment.isAdded()){
                            fragmentManager.beginTransaction()
                                    .add(R.id.view_user_main_for_replace, userAccountFragment, ACC_TAG)
                                    .hide(userAccountFragment)
                                    .commit();
                        }
                        return userAccountFragment;
                    }
                    break;
                case ORDER_TAG:
                    if(null == userOrderFragment){
                        userOrderFragment = new UserOrderFragment(userID, userType, this);
                        if(!userOrderFragment.isAdded()){
                            fragmentManager.beginTransaction()
                                    .add(R.id.view_user_main_for_replace, userOrderFragment, ORDER_TAG)
                                    .hide(userOrderFragment)
                                    .commit();
                        }
                        return userOrderFragment;
                    }
                    break;
            }
        }
        return result;
    }

    /**
     * interface
     * menu fragment use this to get the data from server
     * @param uerID
     */
    @Override
    public void getMenuFromServer(String uerID){
        String url = getString(R.string.base_url) + "/get_menu";
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", userID);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                toastMessage("Cannot connect to server");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    final String res = response.body().string();
                    @Override
                    public void run() {
                        menuResponse(res);
                    }
                });
            }
        });
    }

    @Override
    public void searchMenu(String userID, String userType, String keyword) {
        String url = getString(R.string.base_url) + "/search";
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", userID);
        bodyBuilder.add("role", userType);
        bodyBuilder.add("keyword", keyword);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Failed to connect server");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string().trim();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchMenuResponse(res);
                    }
                });
            }
        });
    }

    /**
     * interface
     * user account page use this to get the data from server
     * @param userID
     * @param userType
     */
    @Override
    public void getUserInfoFromServer(String userID, String userType) {
        String url = getString(R.string.base_url) + "/get_info";
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", userID);
        bodyBuilder.add("role", userType);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                toastMessage("Cannot connect to server");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    final String res = response.body().string();
                    @Override
                    public void run() {
                        accInfoResponse(res);
                    }
                });
            }
        });
    }

    /**
     * interface
     * user discussion page use this to get the data from server
     * @param userID
     */
    @Override
    public void getDiscussionFromServer(String userID) {
        String url = getString(R.string.base_url) + "/get_discussionHeads";
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", userID);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Cannot connect to server");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    final String res = response.body().string();
                    @Override
                    public void run() {
                        discussionResponse(res);
                    }
                });
            }
        });
    }


    @Override
    public void getOrderListFromServer(String userID, String userType) {
        String url = getString(R.string.base_url) + "/get_order";
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("userID", userID);
        bodyBuilder.add("role", userType);
        Request request = new Request.Builder().url(url).post(bodyBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Cannot connect to server");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String res = response.body().string().trim();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        orderListResponse(res);
                    }
                });
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case UnitTools.REQUEST_USER_ORDER_CART:
                if (null == data) {
                    //toastMessage("For result data empty");
                    return;
                }
                // get the change from the cart activity
                String dishesInCartStr = data.getStringExtra("dishes");
                Type listType = new TypeToken<ArrayList<DishInCart>>() {
                }.getType();
                ArrayList<DishInCart> toFragment = UnitTools.getGson().fromJson(dishesInCartStr, listType);
                // apply the change to menu fragment
                if (null != userMenuFragment) {
                    userMenuFragment.setDishesInCart(toFragment);
                }
                break;
        }
    }


}
