package com.example.hackathonproject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://YOUR_AZURE_CONTENT_MODERATOR_ENDPOINT";
    private static Retrofit retrofit;

    public static ContentModeratorService createService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ContentModeratorService.class);
    }
}
