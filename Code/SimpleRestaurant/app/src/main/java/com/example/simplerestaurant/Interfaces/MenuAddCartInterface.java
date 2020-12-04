package com.example.simplerestaurant.Interfaces;

import com.example.simplerestaurant.beans.DishBean;
import com.example.simplerestaurant.beans.DishInCart;

public interface MenuAddCartInterface {
    void dishAdd2Cart(DishInCart newDish, String dishName);
}
