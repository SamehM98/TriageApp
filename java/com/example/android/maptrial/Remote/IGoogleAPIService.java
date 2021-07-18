package com.example.android.maptrial.Remote;

import com.example.android.maptrial.Model.MyPlaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleAPIService {

    @GET
    Call<MyPlaces> getNearByPlaces(@Url String url);
}
