package io.flaterlab.kyrgyzdaamy.service.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class RobokassaCredentialsData(
    val Description: String,
    val InvoiceID: Long,
    val IsTest: String,
    val MerchantLogin: String,
    val OutSum: String,
    val SignatureValue: String
) : Parcelable

