package com.timelysoft.amore.service.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProfilessResponse (
    @SerializedName("Id")
    @Expose
    var id: String = "",

    @SerializedName("DisplayClientId")
    @Expose
    var displayClientId: Int = 0,

    @SerializedName("FirstName")
    @Expose
    var firstName: String = "",

    @SerializedName("LastName")
    @Expose
    var lastName: String = "",

    @SerializedName("Birthday")
    @Expose
    var birthday: String = "",

    @SerializedName("Email")
    @Expose
    var email: String = "",

    @SerializedName("EmailConfirmed")
    @Expose
    var emailConfirmed: Boolean = false,

    @SerializedName("PhoneNumber")
    @Expose
    var phoneNumber: String = "",

    @SerializedName("PhoneNumberConfirmed")
    @Expose
    var phoneNumberConfirmed: Boolean = false,

    @SerializedName("RegistrationDateUtc")
    @Expose
    var registrationDateUtc: String = "",

    @SerializedName("Sex")
    @Expose
    var sex: Int = 0,

    @SerializedName("SexName")
    @Expose
    var sexName: String = "",

    @SerializedName("IsBlocked")
    @Expose
    var isBlocked: Boolean = false,

    @SerializedName("BlockReason")
    @Expose
    var blockReason: Any = "",

    @SerializedName("BonusPhysicalCardCode")
    @Expose
    var bonusPhysicalCardCode: Int = 0,

    @SerializedName("DiscountPhysicalCardCode")
    @Expose
    var discountPhysicalCardCode: Int = 0,

    @SerializedName("DiscountId")
    @Expose
    var discountId: Int = 0,

    @SerializedName("DiscountValue")
    @Expose
    var discountValue: Int = 0,

    @SerializedName("DiscountName")
    @Expose
    var discountName: String = "",

    @SerializedName("Comment")
    @Expose
    var comment: String = ""
)