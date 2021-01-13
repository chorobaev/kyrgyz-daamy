package com.timelysoft.kainarapp.service.response

import com.google.gson.annotations.SerializedName
import com.timelysoft.kainarapp.service.model.HistoryMenuItems

class HistoryOrderDetailResponse (

	@SerializedName("ClientId") val clientId : Int,
	@SerializedName("ClientFirstName") val clientFirstName : String,
	@SerializedName("ClientLastName") val clientLastName : String,
	@SerializedName("PaidBonuses") val paidBonuses : Int,
	@SerializedName("PaidPrepayPoints") val paidPrepayPoints : Int,
	@SerializedName("MenuItems") val menuItems : List<HistoryMenuItems>,
	@SerializedName("RkOrderDiscounts") val rkOrderDiscounts : List<RkOrderDiscounts>,
	@SerializedName("Id") val id : Int,
	@SerializedName("RkOrderNum") val rkOrderNum : String,
	@SerializedName("RkOrderDate") val rkOrderDate : String,
	@SerializedName("OrderType") val orderType : Int,
	@SerializedName("PayType") val payType : Int,
	@SerializedName("Sum") val sum : Int,
	@SerializedName("IsCanceled") val isCanceled : Boolean
)