package com.example.weatherappassignment.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherappassignment.databinding.ActivityWeatherBinding
import com.example.weatherappassignment.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {

    private val viewModel by viewModels<WeatherViewModel>()
    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this

        with(binding) {
            vm = viewModel
            searchButton.setOnClickListener {
                it?.hideKeyboard(baseContext)
                viewModel.getWeatherData(enterCityName.text.toString())
            }
        }
    }
}