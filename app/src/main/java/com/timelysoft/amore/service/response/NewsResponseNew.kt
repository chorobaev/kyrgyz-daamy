package com.timelysoft.amore.service.response

import android.os.Parcelable
import com.timelysoft.amore.service.model.DiscountInfoImage
import kotlinx.android.parcel.Parcelize

@Parcelize
class NewsResponseNew(
    val baseFileUrl: String,
    val boldDescription: String,
    val description: String,
    val endDateUtc: String,
    val id: Int,
    val prefix: String,
    val infoImages: List<DiscountInfoImage>,
    val isShowOnTop: Boolean,
    val name: String,
    val restaurantGroupId: Int,
    val restaurantId: Int,
    val startDateUtc: String,
    val type: Int
): Parcelable