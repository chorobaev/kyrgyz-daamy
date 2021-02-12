package com.timelysoft.amore.service.response


class RestaurantInfoResponse(

        val files: List<String>,
        val cityId : Int,
        val globalId: String,
        val id: Int,
        val name: String,
        val prefix: String,
        val onlinePaymentSupported: Boolean,
        val restaurantDetail: RestaurantDetailResponse?,
        val restaurantGroupId: Int,
        val setting: Any,
        val socialNetworks: List<SocialNetworkResponse>,
        val userId: String
)