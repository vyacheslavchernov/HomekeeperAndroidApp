package com.vydev.homekeeperapp.Api.Declaration;

import com.vydev.homekeeperapp.Api.RsBody.Core.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MonthDataService {
    @GET("api/monthdata/getlast")
    Call<ApiResponse> getlast();

    @GET("api/monthdata/get")
    Call<ApiResponse> get(@Query("id") String id);
}
