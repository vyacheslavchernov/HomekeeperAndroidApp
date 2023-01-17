package com.vydev.homekeeperapp.Api.RqBody;

import okhttp3.MultipartBody;

public class LoginBody {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public LoginBody setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginBody setPassword(String password) {
        this.password = password;
        return this;
    }

    public MultipartBody toMultipartBody() {
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .build();
    }
}
