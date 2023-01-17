package com.vydev.homekeeperapp.Api.Declaration;

import com.vydev.homekeeperapp.Api.RqBody.LoginBody;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST("perform-login")
    Call<ResponseBody> performLogin(@Body MultipartBody loginBody);
}
