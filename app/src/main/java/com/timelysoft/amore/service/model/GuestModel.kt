package com.timelysoft.amore.service.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class GuestModel(
    var fname: String,
    var mname: String,
    var sname: String,
    var addressinfo: AddressInfoModel?,
    var phone: String

): Parcelable