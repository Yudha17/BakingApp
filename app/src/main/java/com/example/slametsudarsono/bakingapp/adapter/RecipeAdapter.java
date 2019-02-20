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
import com.example.slametsudarsono.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    private Context mContext;
    private List<Recipe> recipeList = new ArrayList<>();

    public RecipeAdapter(Context mContext, List<Recipe> recipeList) {
        this.mContext = mContext;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recipe_card, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.MyViewHolder holder, int i) {

        Recipe recipe = recipeList.get(i);
        holder.title.setText(recipe.getName());


    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        ImageView image;

        public MyViewHolder(View view){

            super(view);
            title = (TextView) view.findViewById(R.id.recipeTitle);
            image = (ImageView) view.findViewById(R.id.recipeImage);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Recipe clickedDataItem = recipeList.get(pos);

                        Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                        intent.putExtra("Recipe", clickedDataItem);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
