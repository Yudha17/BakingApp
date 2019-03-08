package com.example.slametsudarsono.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.slametsudarsono.bakingapp.adapter.RecipeAdapter;

import com.example.slametsudarsono.bakingapp.data.model.Recipe;
import com.example.slametsudarsono.bakingapp.utils.Constants;
import com.example.slametsudarsono.bakingapp.utils.Injector;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeClickListener{

    private Context mContext;
    private RecyclerView recyclerView;
    private List<Recipe> recipeList = new ArrayList<>();
    private SimpleIdlingResource mSimpleIdlingResource;
    private boolean emptyDataShown;
    private TextView empty_tv;
    private ImageView empty_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        empty_tv = findViewById(R.id.empty_recipes_tv);
        empty_image = findViewById(R.id.empty_image);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext() ));
        final RecipeAdapter adapter = new RecipeAdapter(this, this);
        recyclerView.setAdapter(adapter);

        if (mSimpleIdlingResource != null) {
            mSimpleIdlingResource.setIdleState(false);
        }


        MainListFactory factory = Injector.provideMainListFactory(this);
        MainListViewModel viewModel = ViewModelProviders.of(this, factory).get(MainListViewModel.class);

        viewModel.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                //Set resources as idle at the end of database operations
                if (mSimpleIdlingResource != null) {
                    mSimpleIdlingResource.setIdleState(true);
                }

                if(recipes == null || recipes.isEmpty()){
                    showEmpty();
                } else{
                    adapter.setRecipes(recipes);
                    recipeList = recipes;
                    showData();
                }
            }
        });


    }

    private void showData() {
        if(emptyDataShown){
            empty_tv.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            empty_image.setVisibility(View.GONE);
            emptyDataShown = false;
        }
    }

    private void showEmpty() {

        if(!thereIsConnection()){
            showSnack();
        } else{
            empty_tv.setText(R.string.server_error);
        }
        empty_image.setVisibility(View.VISIBLE);
        empty_tv.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyDataShown = true;
    }

    private void showSnack() {

        Snackbar snackbar = Snackbar
                .make(recyclerView, R.string.click_retry, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Injector.provideRepository(MainActivity.this).fetchAndSaveRecipes();
                    }
                });
        snackbar.show();
    }

    private boolean thereIsConnection() {

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    @Override
    public void onRecipeClicked(int position) {

        int recipeId = recipeList.get(position).getRecipeId();

        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.WIDGET_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constants.LAST_CHOSEN_RECIPE_ID, recipeId);
        editor.putString(Constants.LAST_CHOSEN_RECIPE_NAME, recipeList.get(position).getRecipeName());
        editor.apply();


        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(Constants.RECIPE_ID, recipeId);
        startActivity(intent);
    }

    @NonNull
    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        if (mSimpleIdlingResource == null) {
            mSimpleIdlingResource = new SimpleIdlingResource();
        }

        return mSimpleIdlingResource;
    }
}
