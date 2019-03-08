package com.example.slametsudarsono.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.slametsudarsono.bakingapp.R;
import com.example.slametsudarsono.bakingapp.data.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {

    private List<Ingredient> mIngredientList;
    private final List<Boolean> mCheckedStates = new ArrayList<>();
    private final Context mContext;
    private final OnIngredientCheckedListener mListener;
    private static final String TAG = "IngredientsAdapter";


    public IngredientAdapter(Context context, OnIngredientCheckedListener listener){
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_ingredient, viewGroup, false);
        return new IngredientHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientHolder ingredientHolder, int position) {

        Ingredient currentIngredient = mIngredientList.get(position);
        ingredientHolder.quantity_tv.setText(String.valueOf(currentIngredient.getQuantity()));
        ingredientHolder.unit_tv.setText(currentIngredient.getMeasure());
        ingredientHolder.ingredient_tv.setText(currentIngredient.getIngredient());
        if(position%2 == 1){
            ingredientHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.item_background));
        } else {
            ingredientHolder.itemView.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
        }
        ingredientHolder.checkBox.setChecked(mCheckedStates.get(position));
    }

    @Override
    public int getItemCount() { return mIngredientList == null ? 0 : mIngredientList.size(); }

    public void setIngredients(List<Ingredient> ingredients) {
        this.mIngredientList = ingredients;
        notifyDataSetChanged();
    }

    public void setCheckedStates(List<Boolean> checkedStates){
        mCheckedStates.clear();
        mCheckedStates.addAll(checkedStates);
    }

    class IngredientHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{

        final TextView quantity_tv;
        final TextView unit_tv;
        final TextView ingredient_tv;
        final CheckBox checkBox;

        IngredientHolder(View itemView) {
            super(itemView);
            quantity_tv = itemView.findViewById(R.id.ingredientQuantity);
            unit_tv = itemView.findViewById(R.id.ingredientMeasure);
            ingredient_tv = itemView.findViewById(R.id.ingredientName);
            checkBox = itemView.findViewById(R.id.ingredientCheckbox);
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mListener.onIngredientChecked(getAdapterPosition(), isChecked);
            Log.d(TAG, "checked state changed. " + getAdapterPosition() + " " + isChecked);
        }
    }

    public interface OnIngredientCheckedListener {
        void onIngredientChecked(int position, boolean checkedState);
    }
}
