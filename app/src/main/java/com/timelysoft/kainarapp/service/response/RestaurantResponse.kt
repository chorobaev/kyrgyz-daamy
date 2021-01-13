package com.timelysoft.kainarapp.service.response

import com.timelysoft.kainarapp.service.model.DiscountInfoImage

class RestaurantResponse(
    val id: Int,
    val name: String,
    val userId: String,
    val restaurants: List<RestaurantInfoResponse>,
    val bankInfo: Any,
    val globalId: String,
    val restaurantDetail: RestaurantDetailResponse,
    val onlinePaymentSupported : Boolean,
    val restaurantGroupId: Int,
    val crmId: Int,
    val setting: Any,
    val socialNetworks: List<SocialNetworkResponse>
)