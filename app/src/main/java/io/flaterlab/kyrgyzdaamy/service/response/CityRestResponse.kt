package io.flaterlab.kyrgyzdaamy.service.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CityRestResponse(
        @SerializedName("id",alternate = ["Id","crmId"])
        @Expose
        var id: Int = 0,

        @SerializedName("name",alternate = ["Name"])
        @Expose
        var name: String = ""
){
        override fun toString(): String {
                return name
        }
}