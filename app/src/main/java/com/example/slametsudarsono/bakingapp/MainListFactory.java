package com.example.slametsudarsono.bakingapp;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.slametsudarsono.bakingapp.data.BakingRepository;

public class MainListFactory extends ViewModelProvider.NewInstanceFactory {

    private final BakingRepository mRepo;

    public MainListFactory(BakingRepository repo){
        mRepo = repo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new MainListViewModel(mRepo);
    }
}
