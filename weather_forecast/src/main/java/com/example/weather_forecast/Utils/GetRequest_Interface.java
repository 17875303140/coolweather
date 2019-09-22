package com.example.weather_forecast.Utils;


import com.example.weather_forecast.Beans.HeWeather;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetRequest_Interface {
//    @GET("/v5/weather?city=meizhou&key=bc0418b57b2d4918819d3974ac1285d9")
//    Call<HeWeather> getCall();
//    Observable<HeWeather> getCall();
    @GET("/v5/weather")
    Observable<HeWeather> getCall(@Query("city") String city,@Query("key")String key);
}
