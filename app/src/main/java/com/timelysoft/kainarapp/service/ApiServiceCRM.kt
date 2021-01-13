package com.timelysoft.kainarapp.service

import com.timelysoft.kainarapp.cachingRetrofit.Cacheable
import com.timelysoft.kainarapp.service.model.AuthModel
import com.timelysoft.kainarapp.service.model.FirebaseTokenModel
import com.timelysoft.kainarapp.service.model.RegisterModel
import com.timelysoft.kainarapp.service.response.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceCRM {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("Token")
    fun refreshToken(@FieldMap params: Map<String, String>): Call<AuthModel>

    @PUT("FirebaseTokens")
    suspend fun sendFirebaseToken(@Body model: FirebaseTokenModel): Response<Unit>

    @POST("ForMobiles/Clients")
    suspend fun register(@Body register: RegisterModel): Response<Any>

    @POST("ForMobiles/Clients/SendSms/{phone}")
    suspend fun sendSms(@Path("phone") phone: String): Response<Any>

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("Token")
    suspend fun auth(@FieldMap params: Map<String, String>): Response<AuthResponse>

//    @GET("ForMobiles/Restaurants")
//    suspend fun restaurants(): Response<List<CityAndStreetResponse>>

    @Cacheable
    @GET("ForMobiles/Clients/Detail")
    suspend fun profile(): Response<ProfilessResponse>

    @Cacheable
    @GET("ForMobiles/Clients/Orders")
    suspend fun historyOrder(
        @Query("filter.page") page: Int,
        @Query("filter.pageSize") pageSize: Int,
        @Query("filter.adminRestaurantId") restaurantId: Int
    ): Response<HistoryOrdersResponse>

    @Cacheable
    @GET("ForMobiles/Clients/Detail/{id}/Accounts")
    suspend fun profileAccount(@Path("id") id: Int): Response<ProfileAccountResponse>

    @Cacheable
    @GET("ForMobiles/Clients/Orders/{id}")
    suspend fun historyOrderDetail(@Path("id") id: Int): Response<HistoryOrderDetailResponse>

    @Cacheable
    @GET("ForMobiles/Clients/Addresses")
    suspend fun listAddresses(): Response<ArrayList<ListAddressesResponse>>




}