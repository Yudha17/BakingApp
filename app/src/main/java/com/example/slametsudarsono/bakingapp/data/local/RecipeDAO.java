package com.example.slametsudarsono.bakingapp.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.slametsudarsono.bakingapp.data.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDAO {

    @Query("SELECT * FROM recipes")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE recipeId = :recipeId")
    LiveData<Recipe> getChosenRecipe(int recipeId);

    @Query("SELECT * FROM recipes WHERE recipeId = :recipeId")
    Recipe getChosenRecipeForWidget(int recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(List<Recipe> recipe);

    @Query("DELETE FROM recipes")
    void deleteRecipes();
}
