package com.example.simplerestaurant.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.simplerestaurant.Interfaces.UserMenuFragmentInterface;
import com.example.simplerestaurant.R;
import com.example.simplerestaurant.UnitTools;
import com.example.simplerestaurant.beans.MenuBean;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserMenuFragment extends Fragment {
    private TextView greeting;
    private OkHttpClient client;
    private String userID, userType;
    private UserMenuFragmentInterface menuListener;
    private ArrayList<MenuBean> menuList;

    public UserMenuFragment(UserMenuFragmentInterface menuListener){
        super(R.layout.fragment_user_main_menu);
        this.menuListener = menuListener;
        Log.i("menu", "Fragment created");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userType = requireArguments().getString("userType");
        userID = requireArguments().getString("userID");
        greeting = (TextView) view.findViewById(R.id.greeting_name);
        greeting.setText(userType);
        Log.i("menu", "Fragment called");
        client = UnitTools.getOkHttpClient();
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
        convertJson2MenuList(res);
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
}
