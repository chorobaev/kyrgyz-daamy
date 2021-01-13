package com.timelysoft.kainarapp.service.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class OrderCreateModel(
    val guest: GuestModel,
    val order: OrderModel
): Parcelable