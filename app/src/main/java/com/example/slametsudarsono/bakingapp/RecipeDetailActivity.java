package com.example.slametsudarsono.bakingapp;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.transition.Slide;
import android.view.Gravity;
import android.support.v7.app.AppCompatActivity;

import com.example.slametsudarsono.bakingapp.fragment.MasterListFragment;
import com.example.slametsudarsono.bakingapp.utils.Constants;
import com.example.slametsudarsono.bakingapp.utils.Injector;

public class RecipeDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        RecipeDetailViewModelFactory factory = Injector.provideDetailsViewModelFactory(this);
        RecipeDetailViewModel viewModel = ViewModelProviders.of(this, factory).get(RecipeDetailViewModel.class);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int mRecipeId = bundle.getInt(Constants.RECIPE_ID);
            viewModel.setRecipeId(mRecipeId);
        }

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        if(!isTablet && (savedInstanceState == null)){
            MasterListFragment masterListFrag = new MasterListFragment();
            masterListFrag.setExitTransition(new Slide(Gravity.START));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_fragment_container, masterListFrag)
                    .commit();
        }

    }


}
