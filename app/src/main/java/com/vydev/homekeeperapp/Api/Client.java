package com.vydev.homekeeperapp.Api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class Client {
    private static final String BASE_URI = "http://185.38.84.15:8081/";

    private static final Retrofit client = new Retrofit.Builder()
            .baseUrl(BASE_URI)
            .addConverterFactory(JacksonConverterFactory.create())
            .client(new OkHttpClient()
                    .newBuilder()
                    .followRedirects(false)
                    .cookieJar(new SessionCookieJar())
                    .build())
            .build();

    public static Retrofit getInstance() {
        return client;
    }

    private static class SessionCookieJar implements CookieJar {

        private List<Cookie> cookies = new ArrayList<Cookie>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (url.encodedPath().endsWith("login")) {
                this.cookies.addAll(cookies);
            }
        }


        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
                return cookies;
        }
    }
}
