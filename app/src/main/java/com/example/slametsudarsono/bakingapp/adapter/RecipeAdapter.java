package com.example.slametsudarsono.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slametsudarsono.bakingapp.R;
import com.example.slametsudarsono.bakingapp.RecipeDetailActivity;
import com.example.slametsudarsono.bakingapp.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private final Context mContext;
    private final RecipeClickListener mListener;
    private List<Recipe> recipeList = new ArrayList<>();

    public RecipeAdapter(Context context, RecipeClickListener listener) {
        mContext = context;
        this.mListener = listener;
    }


    public void setRecipes(List<Recipe> recipes) {
        recipeList = recipes;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recipe_card, viewGroup, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {

        Recipe recipe = recipeList.get(position);
        holder.title.setText(recipe.getRecipeName());


    }

    @Override
    public int getItemCount() {
        return recipeList == null ? 0 : recipeList.size();
    }

    class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView title;
        final ImageView image;

        public RecipeHolder(View view) {

            super(view);
            title = view.findViewById(R.id.recipeTitle);
            image = view.findViewById(R.id.recipeImage);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mListener.onRecipeClicked(getLayoutPosition());
        }
    }


    public interface RecipeClickListener {
        void onRecipeClicked(int position);
    }

}
