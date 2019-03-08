package com.example.slametsudarsono.bakingapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.slametsudarsono.bakingapp.data.BakingRepository;
import com.example.slametsudarsono.bakingapp.data.model.Recipe;

import java.util.List;

public class MainListViewModel extends ViewModel {

    private final LiveData<List<Recipe>> recipes;

    MainListViewModel(BakingRepository repository) {
        recipes = repository.getAllRecipes();
    }

    public LiveData<List<Recipe>> getAllRecipes(){
        return recipes;
    }
}
