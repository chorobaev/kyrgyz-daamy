package com.timelysoft.kainarapp.service


import com.timelysoft.kainarapp.service.model.OrderCreateModel
import com.timelysoft.kainarapp.service.model.OrderModel
import com.timelysoft.kainarapp.service.response.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {


    /*
    @GET("menu/{id}")
    suspend fun menu(@Path("id") restaurantId: String): Response<MenuResponse>

    @GET("menu/{id}/version")
    suspend fun menuVersion(@Path("id") restaurantId: String): Response<MenuVersionResponse>

    @POST("order/validate/{id}")
    suspend fun orderValidate(
        @Path("id") restaurantId: String,
        @Body orderModel: OrderModel
    ): Response<CommonResponse<OrderResponse>>

    @POST("order/{id}")
    suspend fun orderCreate(
        @Path("id") restaurantId: String,
        @Body orderModel: OrderCreateModel
    ): Response<CommonResponse<Int>>

    @POST("order/online/{id}")
    suspend fun orderCreateCard(
        @Path("id") restaurantId: String,
        @Body orderModel: OrderCreateModel
    ): Response<OrderOnlinePayResponse>



    @GET("restaurant/public/bygroupid/{restaurantgroupid}")
    suspend fun restaurantsСRM(@Path("restaurantgroupid") id: Int = AppPreferences.group()): Response<CommonResponse<List<CityAndStreetResponse>>>

    @GET("restaurantgroup/public/{restaurantgroupid}")
    suspend fun restaurants(@Path("restaurantgroupid") restaurantgroupid: Int = AppPreferences.group()): Response<CommonResponse<ArrayList<RestaurantResponse>>>



    //restaurant id
    @GET("restaurant/public/{restaurantid}")
    suspend fun restaurant(@Path("restaurantid") restaurantid: Int = AppPreferences.restaurant): Response<CommonResponse<ArrayList<RestaurantResponse>>>

    @GET("info/{restaurantgroupid}/{restaurantid}")
    suspend fun newsAll(
        @Path("restaurantgroupid") restaurantgroupid: Int = AppPreferences.group(),
        @Path("restaurantid") restaurantid: Int = 0
    ): Response<CommonResponse<ArrayList<NewsResponseNew>>>

    @GET("info/{restaurantgroupid}/{restaurantid}")
    suspend fun newsRestaurant(
        @Path("restaurantid") restaurantid: Int,
        @Path("restaurantgroupid") restaurantgroupid: Int = AppPreferences.group()
    ): Response<CommonResponse<ArrayList<NewsResponseNew>>>

    @GET("restaurantgroup/{restaurantgroupid}/contractoffer")
    suspend fun warning(@Path("restaurantgroupid") restaurantgroupid: Int = AppPreferences.group()): Response<CommonResponse<String>>



    @GET("dictionary/{restaurantglobalid}/cities")
    suspend fun cities(@Path("restaurantglobalid") restaurantgroupid: String = AppPreferences.globalId): Response<CommonResponse<List<CityAndStreetResponse>>>

    @GET("dictionary/{restaurantglobalid}/cities/{cityid}/streets")
    suspend fun streets(
        @Path("cityid") streetsId: Int,
        @Path("restaurantglobalid") restaurantgroupid: String = AppPreferences.globalId
    ): Response<CommonResponse<List<CityAndStreetResponse>>>



    @GET("order/{restaurantglobalid}/state/{orderid}")
    suspend fun checkLastOrder(
        @Path("restaurantglobalid") restaurantgroupid: String = AppPreferences.lastOrderRestaurantId, @Path(
            "orderid"
        ) cityId: String = AppPreferences.lastOrderId
    ): Response<CommonResponse<OrderResponse>>

    @GET("order/state/payment/{paymentid}")
    suspend fun checkLastOrderDemir(@Path("paymentid") paymentid: String = AppPreferences.lastOrderId): Response<CommonResponse<OrderResponse>>

    @GET("order/check/{paymentid}/pdf")
    suspend fun downloadCheck(@Path("paymentid") invoiceId: String): Response<ResponseBody>


    @GET("dictionary/orderstatuses")
    suspend fun orderStatuses(): Response<ArrayList<OrderType>>



     */
}