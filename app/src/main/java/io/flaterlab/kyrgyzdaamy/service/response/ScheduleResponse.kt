package io.flaterlab.kyrgyzdaamy.service.response

data class ScheduleResponse(
    val restaurantId: String,
    val schedules: List<Schedule>
){
    constructor():this("", listOf())
}
