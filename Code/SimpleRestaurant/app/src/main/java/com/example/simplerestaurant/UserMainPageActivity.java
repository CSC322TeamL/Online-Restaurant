package com.example.simplerestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.simplerestaurant.Fragments.UserAccountFragment;
import com.example.simplerestaurant.Fragments.UserDiscussionFragment;
import com.example.simplerestaurant.Fragments.UserMenuFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import okhttp3.OkHttpClient;

public class UserMainPageActivity extends BaseActivity {

    final private static String MENU_TAG = "fragment_user_menu";
    final private static String ORDER_TAG = "fragment_user_order";
    final private static String DISC_TAG = "fragment_user_discussion";
    final private static String ACC_TAG = "fragment_user_account";

    private String userType;
    private String userID;
    private BottomNavigationView navigationView;
    private OkHttpClient client;

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

        setCurrentFragment(R.id.nav_user_main_menu);
    }

    private void setCurrentFragment(int navID){
        Fragment fragment = null;
        String tag = null;
        switch (navID){
            case R.id.nav_user_main_menu:
            case R.id.nav_user_main_order:
                fragment = findFragmentByTag(MENU_TAG);
                Bundle bundle = new Bundle();
                bundle.putString("userType", userType);
                bundle.putString("userID", userID);
                fragment.setArguments(bundle);
                tag = MENU_TAG;
                break;

            case R.id.nav_user_main_discussion:
                fragment = findFragmentByTag(DISC_TAG);
                tag = DISC_TAG;
                break;
            case R.id.nav_user_main_account:
                fragment = findFragmentByTag(ACC_TAG);
                tag = ACC_TAG;
                break;
        }

        setCurrentFragment(fragment, tag);
    }

    private void setCurrentFragment(Fragment fragment, String tag){
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.view_user_main_for_replace, fragment, tag)
                .commit();
    }

    private Fragment findFragmentByTag(String tag){
        Fragment result = (Fragment) getSupportFragmentManager().findFragmentByTag(tag);
        if(null == result){
            switch (tag){
                case MENU_TAG:
                    result = new UserMenuFragment();
                    break;
                case DISC_TAG:
                    result = new UserDiscussionFragment();
                    break;
                case ACC_TAG:
                    result = new UserAccountFragment();
                    break;
            }
        }
        return result;
    }
}
