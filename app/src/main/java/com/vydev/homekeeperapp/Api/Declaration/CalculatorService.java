package com.vydev.homekeeperapp.Api.Declaration;

import com.vydev.homekeeperapp.Api.RsBody.Core.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CalculatorService {
    @GET("api/calculator/calc")
    Call<ApiResponse> calc(@Query("id") String id);
}
