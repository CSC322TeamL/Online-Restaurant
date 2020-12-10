package com.example.simplerestaurant.Interfaces;

public interface UserMenuFragmentInterface {

    void getMenuFromServer(String userID);
    void searchMenu(String userID, String userType, String keyword);
}
