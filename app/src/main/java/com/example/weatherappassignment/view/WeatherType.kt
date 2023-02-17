package com.example.weatherappassignment.view

import androidx.annotation.DrawableRes
import com.example.weatherappassignment.R

sealed class WeatherType(
    @DrawableRes val iconRes: Int
) {
    object ClearSky : WeatherType(
        iconRes = R.drawable.ic_sunny
    )

    object Cloudy : WeatherType(
        iconRes = R.drawable.ic_cloudy
    )

    object Snow : WeatherType(
        iconRes = R.drawable.ic_snowy
    )

    object Rain : WeatherType(
        iconRes = R.drawable.ic_rainy
    )

    object Thunderstorm : WeatherType(
        iconRes = R.drawable.ic_thunder
    )

    companion object {
        fun fromCode(code: String?): WeatherType? {
            return when (code) {
                "01d", "01n" -> ClearSky
                "11d", "11n" -> Thunderstorm
                "09d", "09n", "10d", "10n" -> Rain
                "13d", "13n" -> Snow
                "02d", "02n", "03d", "03n", "04d", "04n" -> Cloudy
                else -> null
            }
        }
    }
}