package com.example.slametsudarsono.bakingapp.connection;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
    @GET(Route.END_POINT)
    Call<JsonArray> fetchBakingData();
}
