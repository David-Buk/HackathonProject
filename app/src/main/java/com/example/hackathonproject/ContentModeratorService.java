package com.example.hackathonproject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ContentModeratorService {
    @Headers({
            "Ocp-Apim-Subscription-Key: YOUR_SUBSCRIPTION_KEY",
            "Content-Type: text/plain"
    })
    @POST("/contentmoderator/moderate/v1.0/ProcessText/Screen")
    Call<ContentModeratorResponse> analyzeText(@Body String text);
}

