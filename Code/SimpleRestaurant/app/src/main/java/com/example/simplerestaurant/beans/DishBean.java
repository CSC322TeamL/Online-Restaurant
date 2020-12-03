package com.example.simplerestaurant.beans;

import java.util.List;

public class DishBean {
    private String _id;
    private String createBy;
    private String createDate;
    private String description;
    private int digitRating;
    private String image;
    private List<String> keywords;
    private float price;
    private List<String> ratings;
    private String title;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDigitRating() {
        return digitRating;
    }

    public void setDigitRating(int digitRating) {
        this.digitRating = digitRating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<String> getRatings() {
        return ratings;
    }

    public void setRatings(List<String> ratings) {
        this.ratings = ratings;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "DishBean{" +
                "_id='" + _id + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createDate='" + createDate + '\'' +
                ", description='" + description + '\'' +
                ", digitRating=" + digitRating +
                ", image='" + image + '\'' +
                ", keywords=" + keywords +
                ", price=" + price +
                ", ratings=" + ratings +
                ", title='" + title + '\'' +
                '}';
    }
}
