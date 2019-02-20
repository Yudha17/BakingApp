package com.example.slametsudarsono.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slametsudarsono.bakingapp.model.Ingredients;
import com.example.slametsudarsono.bakingapp.model.Recipe;
import com.example.slametsudarsono.bakingapp.model.Step;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeDetailActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    private boolean mTwoPane = false;
    private RecyclerView recyclerView;
    public Recipe recipe;
    public List<Ingredients> ingredients;
    public List<Step> step;
    public String recipeName;
    public ArrayList<Object> object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);



        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        object = new ArrayList<Object>();


        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("Recipe")) {

            recipe = getIntent().getParcelableExtra("Recipe");
            ingredients = recipe.getIngredients();
            step = recipe.getSteps();
            recipeName = recipe.getName();
            object.addAll(ingredients);
            object.addAll(step);

            //Toast.makeText(this, recipeStep + " ", Toast.LENGTH_SHORT).show();
            setTitle(recipeName);

        }else{
            Toast.makeText(this, "Data not available", Toast.LENGTH_SHORT).show();
        }


        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new RecipeAndStepAdapter(object, mTwoPane));
    }


    public class RecipeAndStepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        // The items to display in your RecyclerView
        private List<Object> dataSet;
        private static final int INGREDIENT = 0;
        private static final int STEP = 1;
        public  boolean isTwoPane;

        public RecipeAndStepAdapter(List<Object> dataSet, boolean isTwoPane) {
            this.dataSet = dataSet;
            this.isTwoPane = isTwoPane;
        }

        @Override
        public int getItemViewType(int position) {
            if (dataSet.get(position) instanceof Ingredients ) {
                return INGREDIENT;
            } else if (dataSet.get(position) instanceof Step) {
                return STEP;
            }
            return -1;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            RecyclerView.ViewHolder viewHolder;
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            switch (viewType) {
                case INGREDIENT:
                    View ingredientView = inflater.inflate(R.layout.view_holder_ingredient, viewGroup, false);
                    viewHolder = new IngredientViewHolder(ingredientView);
                    break;
                case STEP:
                    View stepView = inflater.inflate(R.layout.view_holder_step, viewGroup, false);
                    viewHolder = new StepViewHolder(stepView);
                    break;
                default:
                    View view = inflater.inflate(R.layout.view_holder_step, viewGroup, false);
                    viewHolder = new StepViewHolder(view);
                    break;
            }
            return viewHolder;
        }



        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            switch (viewHolder.getItemViewType()) {
                case INGREDIENT:
                    IngredientViewHolder ingredientVh = (IngredientViewHolder) viewHolder;
                    configureIngredientViewHolder(ingredientVh, position);
                    break;
                case STEP:
                    StepViewHolder stepVh = (StepViewHolder) viewHolder;
                    configureStepViewHolder(stepVh, position);
                    break;
                default:
                    StepViewHolder defaultVh = (StepViewHolder) viewHolder;
                    configureStepViewHolder(defaultVh, position);
                    break;
            }
        }

        private void configureIngredientViewHolder(IngredientViewHolder ingredientVh, int position) {
            Ingredients ingredient = (Ingredients) dataSet.get(position);
            if (ingredient != null) {
                ingredientVh.getIngredient().setText( ingredient.getIngredient());
                ingredientVh.getQuantity().setText(Double.toString(ingredient.getQuantity()));

            }
        }

        private void configureStepViewHolder(StepViewHolder stepVh, int position) {
            Step step = (Step) dataSet.get(position);
            if (step != null) {
                stepVh.getShortDesc().setText(step.getShortDescription());
            }
        }


        @Override
        public int getItemCount() {
            return this.dataSet.size();
        }

        public class StepViewHolder extends RecyclerView.ViewHolder {

            public TextView shortDesc, desc, videoUrl;

            public StepViewHolder(View view) {
                super(view);
                shortDesc = (TextView) view.findViewById(R.id.short_description);
                desc = (TextView) view.findViewById(R.id.description);
                videoUrl = (TextView) view.findViewById(R.id.video_url);

                view.setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            if (isTwoPane) {
                                Step clickedDataItem = (Step)dataSet.get(pos);
                                Bundle arguments = new Bundle();
                                arguments.putParcelable("Steps", clickedDataItem);
                                RecipeStepFragment fragment = new RecipeStepFragment();
                                fragment.setArguments(arguments);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.video_view, fragment)
                                        .commit();
                            } else {
                                Context context = v.getContext();
                                Step clickedDataItem = (Step)dataSet.get(pos);
                                Intent intent = new Intent(context, RecipeStepActivity.class);
                                intent.putExtra("Steps", clickedDataItem);
                                context.startActivity(intent);
                            }
                        }

                    }
                });
            }

            public TextView getShortDesc() {
                return this.shortDesc;
            }

            public void setShortDesc(TextView shortDesc) {
                this.shortDesc = shortDesc;
            }

            public TextView getDesc() {
                return this.desc;
            }

            public void setDesc(TextView desc) {
                this.desc = desc;
            }

            public TextView getVideoUrl() {
                return this.videoUrl;
            }

            public void setVideoUrl(TextView videoUrl) {
                this.videoUrl = videoUrl;
            }
        }

        public class IngredientViewHolder extends RecyclerView.ViewHolder {

            public TextView ingredient, quantity;

            public IngredientViewHolder(View view) {
                super(view);
                quantity = (TextView) view.findViewById(R.id.recipeQuantity);
                ingredient = (TextView) view.findViewById(R.id.recipeIngred);
            }

            public TextView getIngredient() { return this.ingredient; }

            public void setIngredient(TextView ingredient) {
                this.ingredient = ingredient;
            }

            public  TextView getQuantity(){return  this.quantity;}

            public void setQuantity(TextView quantity) {this.quantity = quantity;}
        }

    }
}
