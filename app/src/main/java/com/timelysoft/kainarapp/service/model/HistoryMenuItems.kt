package com.timelysoft.kainarapp.service.model

import com.google.gson.annotations.SerializedName

 class HistoryMenuItems (
	@SerializedName("Id") val id : Int,
	@SerializedName("Name") val name : String,
	@SerializedName("Quantity") val quantity : Int,
	@SerializedName("LineType") val lineType : Int,
	@SerializedName("ChildMenuItems") val childMenuItems : List<Any>,
	@SerializedName("Sum") val sum : Double
)