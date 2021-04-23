package io.flaterlab.kyrgyzdaamy.service.response

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsResponse(
    @SerializedName("description") val description: String,
    @SerializedName("img_url") val img_url: String,
    @SerializedName("name") val name: String
) : Parcelable {

    constructor():this("","","")
    companion object {
        fun DocumentSnapshot.toNews(): NewsResponse? {
            return try {
                this.toObject(NewsResponse::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }
}