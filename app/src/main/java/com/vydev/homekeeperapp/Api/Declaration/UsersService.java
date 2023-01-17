package com.vydev.homekeeperapp.Api.Declaration;

import com.vydev.homekeeperapp.Api.RsBody.Core.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UsersService {

    @GET("api/users/getall")
    Call<ApiResponse> getAll();
}
