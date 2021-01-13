package com.timelysoft.kainarapp.service.model

import android.os.Parcelable
import com.timelysoft.kainarapp.service.db.entity.BasketEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
class OrderModel(
    var type: Int? = null,
    var discount: Int? = null,
    var devicetype: Int = 1,
    var comment: String = "",
    var phone: String = "",
    var discounttype: Int? = null,
    var content: List<BasketEntity> = ArrayList(),
    var deliverat: String = "",


    var paid: Int? = null,
    var paytype: Int? = null

) : Parcelable