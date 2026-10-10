package com.giamp72.jaecoo7config

enum class DrivingMode {
    NORMAL,
    SPORT,
    ECO,
    OFFROAD
}

data class CarSettings(
    val volume: Int = 35,
    val brightness: Int = 70,
    val climateTemp: Float = 22f,
    val drivingMode: DrivingMode = DrivingMode.NORMAL
)
