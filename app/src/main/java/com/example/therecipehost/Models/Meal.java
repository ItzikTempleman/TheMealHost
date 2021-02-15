package com.example.therecipehost.Models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Meal implements Parcelable {
    private String title, nationality, category, instructions, thumbPath, id;
    private List<String> ingredients, amountOfEach;
    private boolean isLiked;

    public Meal(String title, String nationality, String category, String instructions, String thumbPath, String id) {
        this.title = title;
        this.nationality = nationality;
        this.category = category;
        this.instructions = instructions;
        this.thumbPath = thumbPath;
        this.id = id;
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Meal(Parcel in) {
        title = in.readString();
        nationality = in.readString();
        category = in.readString();
        instructions = in.readString();
        thumbPath = in.readString();
        ingredients = in.createStringArrayList();
        amountOfEach = in.createStringArrayList();
        isLiked = in.readBoolean();
        id = in.readString();
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getNationality() {
        return nationality;
    }

    public String getCategory() {
        return category;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public String getIngredients() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
            stringBuilder.append(ingredients.get(i));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public String getAmountOfEach() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < amountOfEach.size(); i++) {
            stringBuilder.append(amountOfEach.get(i));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public String getId() {
        return id;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public void initAmounts(JSONObject amountObj) {
        amountOfEach = new ArrayList<>();
        Iterator<String> amountIter = amountObj.keys();
        int i = 1;
        while (amountIter.hasNext()) {
            String amountKey = amountIter.next();
            try {
                String amountsValue = amountObj.get(amountKey).toString();
                if (amountKey.equals("strMeasure" + i) && !amountsValue.isEmpty() && !amountsValue.trim().isEmpty()) {
                    i++;
                    amountOfEach.add(amountsValue);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void initIngredients(JSONObject ingredientsObj) {
        ingredients = new ArrayList<>();
        Iterator<String> iter = ingredientsObj.keys();
        int i = 1;
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                String value = ingredientsObj.get(key).toString();
                if (key.equals("strIngredient" + i) && !value.isEmpty()) {
                    i++;
                    // we have the current Ingredient
                    ingredients.add(value);
                }
            } catch (JSONException e) {
                // Something went wrong!
            }
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(nationality);
        dest.writeString(category);
        dest.writeString(instructions);
        dest.writeString(thumbPath);
        dest.writeStringList(ingredients);
        dest.writeStringList(amountOfEach);
        dest.writeBoolean(isLiked);
    }
}
