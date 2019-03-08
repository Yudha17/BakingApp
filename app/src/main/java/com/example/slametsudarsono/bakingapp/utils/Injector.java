package com.example.slametsudarsono.bakingapp.utils;

import android.content.Context;

import com.example.slametsudarsono.bakingapp.MainListFactory;
import com.example.slametsudarsono.bakingapp.RecipeDetailViewModelFactory;
import com.example.slametsudarsono.bakingapp.data.BakingRepository;
import com.example.slametsudarsono.bakingapp.data.local.AppDatabase;

public class Injector {

    public static BakingRepository provideRepository(Context context){
        AppExecutors executors = AppExecutors.getInstance();
        AppDatabase db = AppDatabase.getInstance(context);
        return BakingRepository.getInstance(db.recipeDao(), executors);
    }


    public static MainListFactory provideMainListFactory(Context context){
        BakingRepository repository = provideRepository(context);
        return new MainListFactory(repository);
    }

    public static RecipeDetailViewModelFactory provideDetailsViewModelFactory(Context context) {
        BakingRepository repository = provideRepository(context.getApplicationContext());
        return new RecipeDetailViewModelFactory(repository);
    }
}
