package com.timelysoft.amore.service

import com.timelysoft.amore.cachingRetrofit.Cacheable
import com.timelysoft.amore.service.model.*
import com.timelysoft.amore.service.response.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServiceMod {



    @GET("Schedule/{restaurantId}")
    suspend fun getSchedules(
        @Path("restaurantId") restaurantId: String = AppPreferences.idOfRestaurant()
    ):Response<BaseResponse<ScheduleResponse>>
    @Cacheable
    @GET("restaurants/{restaurantId}/public")
    suspend fun getRestaurantById(
        @Path("restaurantId") restaurantId: String? = AppPreferences.restaurant
    ): Response<BaseResponse<RestaurantResponse>>

    @Cacheable
    @GET("restaurants/{restaurantId}/infos")
    suspend fun getRestaurantInfo(
        @Path("restaurantId") restaurantId: String? = AppPreferences.restaurant
    ): Response<BaseResponse<List<NewsResponse>>>

    @Cacheable
    @GET("restaurants/{restaurantId}/menu/categories")
    suspend fun getCategoriesForRestaurant(@Path("restaurantId") restaurantId: String): Response<BaseResponse<CategoriesResponse>>

    @Cacheable
    @GET("restaurants/menu/categories/{id}/items")
    suspend fun getItemsByCategory(
        @Path("id") categoryId: String
    ): Response<BaseResponse<CategoryItemResponse>>


    @GET("payments/robokassa/{restaurantId}/creadentials/{orderId}")
    suspend fun getPaymentLink(
        @Path("restaurantId") restaurantId: String,
        @Path("orderId") orderId: String
    ) : Response<BaseResponse<RobokassaResponse>>

    @POST("auth/login")
    suspend fun getAccessToken(
        @Body authBody: AuthBody
    ):Response<BaseResponse<AccessToken>>


    @Cacheable
    @GET("groups/{restaurantGroupId}/infos")
    suspend fun getRestraurantGroupInfo(
        @Path("restaurantGroupId") restaurantgroupid: String = AppPreferences.group()
    ): Response<BaseResponse<List<NewsResponse>>>

    @Cacheable
    @GET("restaurants/{restaurantId}/menu/version")
    suspend fun getMenuVersionForRes(
        @Path("restaurantId") restaurantId: String? = AppPreferences.restaurant
    ): Response<BaseResponse<String>>

    @Cacheable
    @GET("dictionaries/{restaurantId}/cities")
    suspend fun getCitiesForRes(@Path("restaurantId") restaurantId: String? = AppPreferences.restaurant): Response<BaseResponse<List<CityRestResponse>>>

    @Cacheable
    @GET("dictionaries/{restaurantId}/cities/{cityId}/streets")
    suspend fun getStreetsForCities(
        @Path("cityId") cityId: Int,
        @Path("restaurantId") restaurantId: String = AppPreferences.restaurant
    ): Response<BaseResponse<List<StreetResponse>>>

    @Cacheable
    @POST("orders/{restaurantId}")
    suspend fun createOrder(
        @Path("restaurantId") restaurantId: String?,
        @Body createOrder: CreateOrder
    ): Response<BaseResponse<String>>

    @POST("orders/{restaurantId}/validate/full")
    suspend fun validateOrder(
        @Path("restaurantId") restaurantId: String,
        @Body orderValidate: ValidateOrder
    ): Response<BaseResponse<OrderValidateResponse>>


    @GET("groups/{restaurantGroupId}/restaurants/public")
    suspend fun restaurants(@Path("restaurantGroupId") id: String = AppPreferences.group()): Response<BaseResponse<List<RestaurantResponse>>>

    @GET("Schedule/{restaurantId}/IsOpen")
    suspend fun isOpen(@Path("restaurantId") id: String = AppPreferences.restaurant):Response<Boolean>

    @Cacheable
    @GET("groups/{restaurantGroupId}/contractoffer")
    suspend fun warning(
        @Path("restaurantGroupId") id: String = AppPreferences.group()
    ): Response<BaseResponse<String>>

    @Cacheable
    @GET("payments/exception/{token}")
    suspend fun getException(
        @Path("token") token: String
    ): Response<BaseResponse<String>>

}