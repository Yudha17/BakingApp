package com.example.slametsudarsono.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class RecipeStepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_step_fragment);

        setTitle("Recipe Video");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null){
            Bundle arguments = new Bundle();
            arguments.putParcelable("Steps", getIntent().getParcelableExtra("Steps"));

            RecipeStepFragment fragment = new RecipeStepFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.video_view, fragment)
                    .commit();
        }
    }

}
