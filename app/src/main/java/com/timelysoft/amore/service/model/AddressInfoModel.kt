package com.timelysoft.amore.service.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class AddressInfoModel(
    val id: Int? = null,
    val cityId: Int? = null,
    val streetId: Int? = null,
    val building: String="1",
    val appartaments: String="1",
    val comment: String=""
//    val entry: String,
//    val entryCode: String,
//    val floor: String,
//    val isPrimary: Boolean

) : Parcelable