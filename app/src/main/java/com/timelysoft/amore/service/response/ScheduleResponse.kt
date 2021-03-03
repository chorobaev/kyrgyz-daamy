package com.timelysoft.amore.service.response

data class ScheduleResponse(
    val restaurantId: String,
    val schedules: List<Schedule>
)