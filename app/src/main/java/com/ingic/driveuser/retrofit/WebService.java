package com.ingic.driveuser.retrofit;


import com.ingic.driveuser.entities.AllDriversHomeEnt;
import com.ingic.driveuser.entities.CMSEnt;
import com.ingic.driveuser.entities.CancelReasonEnt;
import com.ingic.driveuser.entities.CreateRideEnt;
import com.ingic.driveuser.entities.EstimateFareEnt;
import com.ingic.driveuser.entities.NotificationListEnt;
import com.ingic.driveuser.entities.ProgressEnt;
import com.ingic.driveuser.entities.PromoCodeEnt;
import com.ingic.driveuser.entities.RegistrationEnt;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.entities.RideDriverEnt;
import com.ingic.driveuser.entities.RideEnt;
import com.ingic.driveuser.entities.RideFeedbackEnt;
import com.ingic.driveuser.entities.RideIdEnt;
import com.ingic.driveuser.entities.RideTrackEnt;
import com.ingic.driveuser.entities.SelectCarEnt;
import com.ingic.driveuser.entities.UpdatedLocationEnt;
import com.ingic.driveuser.entities.UserEnt;
import com.ingic.driveuser.entities.UserMessageEnt;
import com.ingic.driveuser.entities.WalletInfoEnt;
import com.ingic.driveuser.entities.getSettingeEnt;
import com.ingic.driveuser.entities.updateTokenEnt;
import com.ingic.driveuser.entities.VerifyForgotPassEnt;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface WebService {

    @Multipart
    @POST("user/register")
    Call<ResponseWrapper<RegistrationEnt>> registerUser(@Part("full_name") RequestBody userFullname,
                                                        @Part("email") RequestBody useremail,
                                                        @Part("gender") RequestBody usergender,
                                                        @Part("phone_no") RequestBody PhoneNumber,
                                                        @Part("address") RequestBody userfulladdress,
                                                        @Part("zip_code") RequestBody zip_code,
                                                        @Part("dob") RequestBody dob,
                                                        @Part("invitation_code") RequestBody invitation_code,
                                                        @Part("password") RequestBody password,
                                                        @Part("password_confirmation") RequestBody password_confirmation,
                                                        @Part("social_media_id") RequestBody FaceBookUID,
                                                        @Part("social_media_platform") RequestBody SocialName,
                                                        @Part MultipartBody.Part profile_picture
    );


    @FormUrlEncoded
    @POST("user/login")
    Call<ResponseWrapper<UserEnt>> loginUser(@Field("email") String email,
                                             @Field("password") String password
    );

    @FormUrlEncoded
    @POST("user/resendCode")
    Call<ResponseWrapper<UserEnt>> VerifyNumber(@Field("user_id") String userID,
                                                @Field("phone_no") String phonenumber);

    @FormUrlEncoded
    @POST("user/accountVerification")
    Call<ResponseWrapper<UserEnt>> VerifyCode(@Field("user_id") String userID,
                                              @Field("verification_code") String verification_code);

    @FormUrlEncoded
    @POST("user/socialMediaLogin")
    Call<ResponseWrapper<UserEnt>> loginFacebookUser(@Field("social_media_id") String FacebookUID,
                                                     @Field("social_media_platform") String Facebook
    );

    @FormUrlEncoded
    @POST("user/forgotPassword")
    Call<ResponseWrapper> ForgotPassword(@Field("email") String email);

    @Multipart
    @POST("user/updateProfile")
    Call<ResponseWrapper<UserEnt>> UpdateUser(@Part("full_name") RequestBody userFullname,
                                              @Part("phone_no") RequestBody PhoneNumber,
                                              @Part("zip_code") RequestBody zip_code,
                                              @Part("gender") RequestBody usergender,
                                              @Part("address") RequestBody userfulladdress,
                                              @Part("city") RequestBody city,
                                              @Part("state") RequestBody state,
                                              @Part MultipartBody.Part userprofileImage

    );

    @FormUrlEncoded
    @POST("user/renewToken")
    Call<ResponseWrapper<updateTokenEnt>> UpdateToken(@Field("user_id") String user_id,
                                                      @Field("email") String email,
                                                      @Field("role_id") String role_id);

    @GET("user/getProfile")
    Call<ResponseWrapper<UserEnt>> getProfile(@Query("user_id") String userID);

    @FormUrlEncoded
    @POST("user/updateDeviceToken")
    Call<ResponseWrapper> updateToken(@Field("device_type") String deviceType,
                                      @Field("device_token") String token);


    @GET("user/logout")
    Call<ResponseWrapper> LogoutUser();


    @FormUrlEncoded
    @POST("user/changePassword")
    Call<ResponseWrapper<UserEnt>> ChangePassword(@Field("old_password") String old_password,
                                                  @Field("password") String password,
                                                  @Field("password_confirmation") String password_confirmation);

    @GET("user/getUserMessages")
    Call<ResponseWrapper<ArrayList<UserMessageEnt>>> getUserMessages();

    @GET("user/getCancelReason")
    Call<ResponseWrapper<ArrayList<CancelReasonEnt>>> getCancelReasons();

    @GET("user/getImproveType")
    Call<ResponseWrapper<ArrayList<RideFeedbackEnt>>> getImproveType();


    @GET("user/getNotifications")
    Call<ResponseWrapper<ArrayList<NotificationListEnt>>> GetNotifiation();


    @GET("ride/home")
    Call<ResponseWrapper<AllDriversHomeEnt>> getNearbyDrivers(@Query("latitude") String latitude,
                                                              @Query("longitude") String longitude);

    @GET("ride/getVehicle")
    Call<ResponseWrapper<ArrayList<SelectCarEnt>>> getVehicles();


    @GET("ride/estimatedFare")
    Call<ResponseWrapper<EstimateFareEnt>> getRideEstimate(@Query("vehicle_id") String vehicle_id,
                                                           @Query("percentage") String percentage,
                                                           @Query("distance") String distance,
                                                           @Query("time_duration") String time_duration,
                                                           @Query("peak_factor") String peakFactor);

    @FormUrlEncoded
    @POST("ride/rideCreate")
    Call<ResponseWrapper<CreateRideEnt>> createNewRide(@Field("pickup_latitude") String pickup_latitude,
                                                       @Field("pickup_longitude") String pickup_longitude,
                                                       @Field("pickup_address") String pickup_address,
                                                       @Field("pickup_place") String pickup_place,
                                                       @Field("destination_latitude") String destination_latitude,
                                                       @Field("destination_longitude") String destination_longitude,
                                                       @Field("destination_address") String destination_address,
                                                       @Field("destination_place") String destination_place,
                                                       @Field("vehicle_id") String vehicle_id,
                                                       @Field("payment_type") String payment_type,
                                                       @Field("percentage") String percentage,
                                                       @Field("date") String date,
                                                       @Field("time") String time,
                                                       @Field("type") String type,
                                                       @Field("estimate_fare") String estimate_fare,
                                                       @Field("time_duration") String time_duration,
                                                       @Field("distance") String distance,
                                                       @Field("peak_factor") String peakFactor,
                                                       @Field("user_preference") String user_preference);

    @FormUrlEncoded
    @POST("ride/userCancelRide")
    Call<ResponseWrapper<RideEnt>> ChangeRideStatus(@Field("id") String ride_id,
                                                    @Field("cancel_id") String cancel_id
    );

    @GET("ride/promoCode")
    Call<ResponseWrapper<PromoCodeEnt>> getPromoCode(@Query("promo_code") String promocode);


    @GET("ride/lastRideFeedback")
    Call<ResponseWrapper<RideDriverEnt>> getLastFeedback();


    @FormUrlEncoded
    @POST("cms/contactUs")
    Call<ResponseWrapper> ContactUs(@Field("text") String text);


    @GET("cms/getCms")
    Call<ResponseWrapper<CMSEnt>> getCMS(@Query("type") String Type);


    @FormUrlEncoded
    @POST("changePushNotificationStatus")
    Call<ResponseWrapper<UserEnt>> ChangeNotifiationStatus(@Field("push_status") Integer status);

    @GET("driver/getDriverLocation")
    Call<ResponseWrapper<UpdatedLocationEnt>> getUpdatedLocation(
            @Query("driver_id") String driver_id);

    @GET("ride/rideHistory")
    Call<ResponseWrapper<ArrayList<RideEnt>>> getUserRideHistory();


    @GET("ride/approvedRideDetail")
    Call<ResponseWrapper<RideDriverEnt>> getApproveDriver(@Query("ride_id") String ride_id);

    @GET("user/getSetting")
    Call<ResponseWrapper<getSettingeEnt>> getSetting(@Query("key") String key);



    @GET("ride/userInprogressRide")
    Call<ResponseWrapper<ArrayList<ProgressEnt>>> getUserRideInProgress();


    @GET("ride/userCompletedRide")
    Call<ResponseWrapper<ArrayList<ProgressEnt>>> getUserRideComplete();

    @FormUrlEncoded
    @POST("user/rating")
    Call<ResponseWrapper> submitRideFeedback(@Field("ride_id") String ride_id,
                                             @Field("receiver_id") String receiver_id,
                                             @Field("rating") String rating);

    @FormUrlEncoded
    @POST("ride/rideFeedback")
    Call<ResponseWrapper> submitAppFeedback(@Field("ride_id") String ride_id,
                                            @Field("driver_id") String driver_id,
                                            @Field("comment") String comment,
                                            @Field("rating") Integer rating,
                                            @Field("type") String type
    );



    @FormUrlEncoded
    @POST("user/setUserPreference")
    Call<ResponseWrapper> setUserPreference(@Field("user_preference") String user_preference);




    /////////////////

    @FormUrlEncoded
    @POST("user/resetcode")
    Call<ResponseWrapper<UserEnt>> resetVerificationCode(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("user/verifyCode")
    Call<ResponseWrapper<VerifyForgotPassEnt>> verifyForgotPassword(@Field("verification_code") String verification_code);

    @FormUrlEncoded
    @POST("user/resetPassword")
    Call<ResponseWrapper> resetForgotPassword(@Field("user_id") String user_id,
                                              @Field("password") String password);

    @FormUrlEncoded
    @POST("user/resendForgotCode")
    Call<ResponseWrapper> resendForgotPassword(@Field("email") String email);

    @GET("user/changeLoginStatus")
    Call<ResponseWrapper> changeLoginStatus();

    @GET("ride/rideTracking")
    Call<ResponseWrapper<RideTrackEnt>> rideTracking(@Query("id") String id);

    @GET("ride/checkOnGoingRide")
    Call<ResponseWrapper<RideIdEnt>> isRideOnGoing(@Query("type") String type);

    @FormUrlEncoded
    @POST("user/walletOnOff")
    Call<ResponseWrapper> useWallet(@Field("wallet_status") Integer wallet_status);


    @GET("user/getWalletInfo")
    Call<ResponseWrapper<WalletInfoEnt>> getWalletInfo();




}