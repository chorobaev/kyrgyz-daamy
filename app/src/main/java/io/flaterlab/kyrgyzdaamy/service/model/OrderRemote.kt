package io.flaterlab.kyrgyzdaamy.service.model


class OrderRemote(
    val firstName: String,
    val lastName:String,
    val phoneNumber: String,
    val payment_type :String,
    val deliveryType:String,
    val street:String,
    val homeNumber: Int?,
    val apartmentNumber: Int?,
    val apartmentFloor :Int?,
    val comment: String?,
    val date:String,
    val time:String
)