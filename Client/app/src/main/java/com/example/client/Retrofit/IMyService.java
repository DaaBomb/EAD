package com.example.client.Retrofit;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IMyService {
    @POST("api/users/")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                            @Field("name") String name,
                            @Field("password") String password);

    @POST("api/auth/")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                   @Field("password") String password);


}
