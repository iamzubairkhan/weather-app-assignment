package com.example.weatherappassignment.view.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherappassignment.R
import com.example.weatherappassignment.view.WeatherType
import com.example.weatherappassignment.view.WeatherType.Snow
import com.example.weatherappassignment.view.theme.DeepBlue

@Composable
fun WeatherCard(
    currentTemperature: String?,
    weatherType: WeatherType?,
    currentCondition: String?,
    minTemperature: String? = null,
    humidity: String? = null,
    maxTemperature: String? = null,
    backgroundColor: Color = DeepBlue
) {
    currentTemperature?.let {
        Card(
            backgroundColor = backgroundColor,
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                weatherType?.let {
                    Image(
                        painter = painterResource(id = it.iconRes),
                        contentDescription = null,
                        modifier = Modifier.width(200.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = currentTemperature,
                    fontSize = 50.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                currentCondition?.let {
                    Text(
                        text = it,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    minTemperature?.let {
                        WeatherDataElement(
                            value = it,
                            icon = ImageVector.vectorResource(id = R.drawable.ic_min_temp),
                            iconTint = Color.White,
                            textStyle = TextStyle(color = Color.White)
                        )
                    }
                    humidity?.let {
                        WeatherDataElement(
                            value = it,
                            icon = ImageVector.vectorResource(id = R.drawable.ic_drop),
                            iconTint = Color.White,
                            textStyle = TextStyle(color = Color.White)
                        )
                    }
                    maxTemperature?.let {
                        WeatherDataElement(
                            value = it,
                            icon = ImageVector.vectorResource(id = R.drawable.ic_max_temp),
                            iconTint = Color.White,
                            textStyle = TextStyle(color = Color.White)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewWeatherCardDetails() {
    WeatherCard(
        currentTemperature = "5C",
        weatherType = Snow,
        currentCondition = "Snow",
        minTemperature = "-3",
        maxTemperature = "10",
        humidity = "80%",
    )
}

@Preview
@Composable
fun PreviewWeatherCardSummary() {
    WeatherCard(
        currentTemperature = "5C",
        weatherType = Snow,
        currentCondition = "Snow"
    )
}
