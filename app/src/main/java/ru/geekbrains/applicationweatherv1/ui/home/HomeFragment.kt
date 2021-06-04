package ru.geekbrains.applicationweatherv1.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.applicationweatherv1.dataFactory.Weather
import ru.geekbrains.applicationweatherv1.dataFactory.WeatherDTO
import ru.geekbrains.applicationweatherv1.databinding.HomeFragmentBinding

const val WEATHER_API_KEY = "c0eaea4c-4426-490c-a7f7-17416be29ac7"

class HomeFragment : Fragment() {


    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather
    private val onLoadListener: WeatherLoader.WeatherLoaderListener =
        object : WeatherLoader.WeatherLoaderListener {

            override fun onLoaded(weatherDTO: WeatherDTO) {
                displayWeather(weatherDTO)
            }

            override fun onFailed(throwable: Throwable) {
            }
        }
    private val homeViewModel: HomeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        val loader = WeatherLoader(
            onLoadListener, weatherBundle.city.lat,
            weatherBundle.city.lon
        )
        loader.loadWeather()
    }

    private fun displayWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE
            val city = weatherBundle.city
            cityName.text = city.city
            temperatureValue.text = weatherDTO.fact?.temp.toString()
            feelsLike.text = weatherDTO.fact?.feels_like.toString()
            windSpeed.text = weatherDTO.fact?.wind_speed.toString()
            windDirection.text = weatherDTO.fact?.wind_dir
            humidityValue.text = weatherDTO.fact?.humidity.toString()
        }
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle): HomeFragment {
            val fragment = HomeFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}