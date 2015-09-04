package com.example.classes;

import retrofit.http.GET;
import retrofit.http.Query;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.User;

public interface CustomService 
{
        @GET("/1.1/users/show.json")
        void show(@Query("user_id") long id, Callback<User> cb);

}
