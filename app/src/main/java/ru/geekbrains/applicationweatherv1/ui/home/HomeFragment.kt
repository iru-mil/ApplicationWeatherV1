package ru.geekbrains.applicationweatherv1.ui.home

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import ru.geekbrains.applicationweatherv1.R
import ru.geekbrains.applicationweatherv1.dataFactory.Weather
import ru.geekbrains.applicationweatherv1.dataFactory.WeatherDTO
import ru.geekbrains.applicationweatherv1.databinding.HomeFragmentBinding

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

            temperatureValue.text = if ((weatherDTO.fact?.temp)!! > 0) {
                "+" + weatherDTO.fact.temp.toString() + resources.getString(R.string.degree)
            } else {
                weatherDTO.fact.temp.toString() + resources.getString(R.string.degree)
            }
            (weatherDTO.fact.wind_speed.toString() + resources.getString(R.string.wind_units)).also {
                windSpeed.text = it
            }
            feelsLike.text = if ((weatherDTO.fact.feels_like)!! > 0) {
                resources.getString(R.string.feels_like_label) + "+" + weatherDTO.fact.feels_like.toString() + resources.getString(
                    R.string.degree
                )
            } else {
                resources.getString(R.string.feels_like_label) + weatherDTO.fact.feels_like.toString() + resources.getString(
                    R.string.degree
                )
            }
            windDirection.text = when (weatherDTO.fact.wind_dir) {
                "nw" -> {
                    imageViewDirection.setImageResource(R.drawable.ic_up_left_icon); "СЗ"
                }
                "n" -> {
                    imageViewDirection.setImageResource(R.drawable.ic_up_icon); "С"
                }
                "ne" -> {
                    imageViewDirection.setImageResource(R.drawable.ic_up_right_icon); "СВ"
                }
                "e" -> {
                    imageViewDirection.setImageResource(R.drawable.ic_right_icon); "В"
                }
                "se" -> {
                    imageViewDirection.setImageResource(R.drawable.ic_down_right_icon); "ЮВ"
                }
                "s" -> {
                    imageViewDirection.setImageResource(R.drawable.ic_down_icon); "Ю"
                }
                "sw" -> {
                    imageViewDirection.setImageResource(R.drawable.ic_down_left_icon); "ЮЗ"
                }
                "w" -> {
                    imageViewDirection.setImageResource(R.drawable.ic_left_icon); "З"
                }
                "c" -> {
                    "штиль"
                }
                else -> {
                    ""
                }
            }
            (weatherDTO.fact.humidity.toString() + resources.getString(R.string.percent)).also {
                humidityValue.text = it
            }
            weatherDTO.fact.icon?.let {
                GlideToVectorYou.justLoadImage(
                    activity,
                    Uri.parse("https://yastatic.net/weather/i/icons/blueye/color/svg/${it}.svg"),
                    imageViewWeather
                )
            }
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