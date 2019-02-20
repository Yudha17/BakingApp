package com.example.slametsudarsono.bakingapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.slametsudarsono.bakingapp.adapter.RecipeAdapter;
import com.example.slametsudarsono.bakingapp.connection.DataService;
import com.example.slametsudarsono.bakingapp.connection.Service;
import com.example.slametsudarsono.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static Context mContext;
    private RecyclerView recyclerView;
    private List<Recipe> baking = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        loadData();
    }

    private void loadData() {
        Service service = DataService.createService(Service.class);
        Call<JsonArray> call = service.fetchBakingData();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String listString = response.body().toString();

                        Type listType = new TypeToken<List<Recipe>>() {
                        }.getType();
                        baking = getListFromJson(listString, listType);

                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(new RecipeAdapter(getApplicationContext(), baking));
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    private static <T> List<T> getListFromJson(String jsonString, Type type) {
        if (!isValid(jsonString)) {
            return null;
        }
        return new Gson().fromJson(jsonString, type);
    }

    private static boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }

}
