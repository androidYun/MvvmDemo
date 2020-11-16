package com.xhs.expertdiagnose.ui.bean

data class NLocationModel(
    val actualSign: String = "",
    val direction: String = "",
    val eventAddress: String = "",
    val eventType: String = "",
    val lat: String = "",
    val lon: String = "",
    val phoneNum: String = "",
    val plateNumber: String = "",
    val speed: String = "",
    val stationCode: String = "",
    val stationId: String = "",
    val stationName: String = "",
    val taskCode: String = "",
    val toStationId: String = "",
    val toStationName: String = "",
    val userName: String = "",
    val vehicleCode: String = "",
    val vehicleState: String = "",
    val vehicleStateCode: Int = 0
)