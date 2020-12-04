package com.example.simplerestaurant.beans;

public class DishInCart {
    private String dishID;
    private String specialNote = "";
    private int quantity;

    public String getDishID() {
        return dishID;
    }

    public void setDishID(String dishID) {
        this.dishID = dishID;
    }

    public String getSpecialNote() {
        return specialNote;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "DishInCart{" +
                "dishID='" + dishID + '\'' +
                ", specialNote='" + specialNote + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
