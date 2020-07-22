package com.wrath.client.Retrofit;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @GET("api/society/cities/")
    Observable<String> getCities();

    @GET("api/society/societies/")
    Observable<String> getSocieties();

    @GET("api/society/socitiesbycity/")
    Observable<String> changeSocieties( @Query("city") String city);

    @POST("api/society/flat")
    Observable<String> registerResident(@Body RequestBody user);

    @POST("api/society/addbuilder")
    Observable<String> addBuilder(@Body RequestBody builderSociety);

    @POST("api/society/staff")
    Observable<String> addStaff(@Body RequestBody staffSociety);

    @GET("api/forums/getTopic/")
    Observable<String> getTopics(@Query("society_id") String society_id,@Query("flag") int flag);

    @GET("api/forums/getComplains/")
    Observable<String> getComplains(@Query("society_id") String society_id,@Query("flag") int flag);

    @POST("api/forums/addTopic")
    Observable<String> addTopic(@Body RequestBody topicDescription);

    @POST("api/forums/addcomment")
    Observable<String> addcomment(@Body RequestBody commentUser);

    @POST("api/gateregister/")
    Observable<String> sendnotification(@Body RequestBody securityRequest);

    @POST("api/gateregister/permission")
    Observable<String> sendPermission(@Body RequestBody permissionRequest);

    @GET("api/gateregister/getRequests/")
    Observable<String> getSecurityRequests(@Query("society_id") String society_id,@Query("flag") int flag);

    @GET("api/gateregister/getUserRequests/")
    Observable<String> getSecurityUserRequests(@Query("society_id") String society_id,@Query("flag") int flag,@Query("blockname") String blockname,@Query("flatnum") String flatnum);

    @POST("api/programmes/")
    Observable<String> addEvent(@Body RequestBody eventDetails);

    @GET("api/programmes/getEvents/")
    Observable<String> getEvents(@Query("society_id") String society_id,@Query("flag") int flag);

    @POST("api/programmes/register/")
    Observable<String> registerEvent(@Body RequestBody eventRegisterRequest);

    @GET("api/society/societybyid/")
    Observable<String> getSocietyName(@Query("society_id") String society_id);

    @POST("api/users/panic/")
    Observable<String> engagePanicSequence(@Body RequestBody request);

    @POST("api/concierge/")
    Observable<String> addConcierge(@Body RequestBody concierge);

    @GET("api/concierge/getBySociety/")
    Observable<String> getSocietyConcierge(@Query("society_id") String society_id,@Query("flag") int flag);

    @GET("api/concierge/getByFlat/")
    Observable<String> getFlatConcierge(@Query("society_id") String society_id,@Query("flag") int flag,@Query("blockname") String blockname,@Query("flatnum") String flatnum);

}
