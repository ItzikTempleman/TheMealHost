package com.example.therecipehost.Models;

/**
 * Created by Liad Horovitz on 15,February,2021
 */
public class Category {

    private String text;
    private int id;
    private boolean isClicked;

    public Category(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
}
