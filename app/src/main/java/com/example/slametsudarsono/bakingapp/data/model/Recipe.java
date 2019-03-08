package com.example.slametsudarsono.bakingapp.data.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "recipes")
public class Recipe {

    @PrimaryKey
    @SerializedName("id")
    private int recipeId;

    @SerializedName("name")
    private String recipeName;

    @SerializedName("ingredients")
    private List<Ingredient> ingredientList;

    @SerializedName("steps")
    private List<Step> stepList;

    private int servings;

    @SerializedName("image")
    private String recipeImage;

    @Ignore
    public Recipe(){}

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public List<Step> getStepList() {
        return stepList;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public Recipe(int recipeId, String recipeName, List<Ingredient> ingredientList, List<Step> stepList, int servings, String recipeImage) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.ingredientList = ingredientList;
        this.stepList = stepList;
        this.servings = servings;
        this.recipeImage = recipeImage;


    }
}
