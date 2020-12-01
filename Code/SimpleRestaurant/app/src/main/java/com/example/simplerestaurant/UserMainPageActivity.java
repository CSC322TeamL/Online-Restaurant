package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.simplerestaurant.Fragments.UserAccountFragment;
import com.example.simplerestaurant.Fragments.UserDiscussionFragment;
import com.example.simplerestaurant.Fragments.UserMenuFragment;
import com.example.simplerestaurant.Interfaces.UserAccountFragmentInterface;
import com.example.simplerestaurant.Interfaces.UserMenuFragmentInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserMainPageActivity extends BaseActivity implements UserMenuFragmentInterface, UserAccountFragmentInterface {

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

    private void setCurrentFragment(int navID){
        switch (navID){
            case R.id.nav_user_main_menu:
            case R.id.nav_user_main_order:
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

    private void menuResponse(String res){
        if(null == userMenuFragment){
            userMenuFragment = (UserMenuFragment) findFragmentByTag(MENU_TAG);
        }
        userMenuFragment.menuResponse(res);
    }

    private void accInfoResponse(String res){
        if(null == userAccountFragment){
            userAccountFragment = (UserAccountFragment) findFragmentByTag(ACC_TAG);
        }
        userAccountFragment.userInfoResponse(res);
    }

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
                        userDiscussionFragment = new UserDiscussionFragment();
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
            }
        }
        return result;
    }

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
    public void getUserInfoFromServer(String userID, String userType) {
        String url = getString(R.string.base_url) + "/get_menu";
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
}
