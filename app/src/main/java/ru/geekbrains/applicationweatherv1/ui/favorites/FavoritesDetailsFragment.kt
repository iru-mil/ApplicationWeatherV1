package ru.geekbrains.applicationweatherv1.ui.favorites

import android.annotation.SuppressLint
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import ru.geekbrains.applicationweatherv1.R
import ru.geekbrains.applicationweatherv1.dataFactory.Weather
import ru.geekbrains.applicationweatherv1.databinding.DetailsFragmentBinding
import ru.geekbrains.applicationweatherv1.ui.home.AppState
import ru.geekbrains.applicationweatherv1.ui.showSnackBar

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"

const val DETAILS_TEMP_EXTRA = "TEMPERATURE"
const val DETAILS_FEELS_LIKE_EXTRA = "FEELS LIKE"
const val DETAILS_WIND_SPEED_EXTRA = "WIND SPEED"
const val DETAILS_WIND_DIR_EXTRA = "WIND DIRECTION"
const val DETAILS_HUMIDITY_EXTRA = "HUMIDITY"
const val DETAILS_ICON_EXTRA = "ICON"

class FavoritesDetailsFragment : Fragment() {

    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

    private val detailsViewModel: FavoritesDetailsViewModel by lazy {
        ViewModelProvider(this).get(FavoritesDetailsViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
        detailsViewModel.detailsLiveData.observe(viewLifecycleOwner, {
            renderData(it)
        })
        detailsViewModel.getWeatherFromRemoteSource(
            weatherBundle.city.lat,
            weatherBundle.city.lon
        )
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                setWeather(appState.weatherData[0])
            }
            is AppState.Loading -> {
                binding.mainView.visibility = View.GONE
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                binding.mainView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    {
                        detailsViewModel.getWeatherFromRemoteSource(
                            weatherBundle.city.lat,
                            weatherBundle.city.lon
                        )
                    })
            }
            else -> return
        }
    }

    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    private fun setWeather(weather: Weather) {
        Glide.with(this).load("https://freepngimg.com/thumb/travel/30671-8-travel-clipart.png")
            .into(binding.imageViewHeader)
        val city = weatherBundle.city
        binding.cityName.text = city.city
        binding.temperatureValue.text = if (weather.temp > 0) {
            "+" + weather.temp.toString() + resources.getString(R.string.degree)
        } else {
            weather.temp.toString() + resources.getString(R.string.degree)
        }
        (weather.windSpeed.toString() + resources.getString(R.string.wind_units)).also {
            binding.windSpeed.text = it
        }
        binding.feelsLike.text = if ((weather.feelsLike) > 0) {
            resources.getString(R.string.feels_like_label) + "+" + weather.feelsLike.toString() + resources.getString(
                R.string.degree
            )
        } else {
            resources.getString(R.string.feels_like_label) + weather.feelsLike.toString() + resources.getString(
                R.string.degree
            )
        }
        val res: Resources = resources
        val sourceDir = res.getStringArray(R.array.source_dir)
        val sourceApp = res.getStringArray(R.array.app_dir)
        binding.windDirection.text = sourceApp[sourceDir.indexOf(weather.windDir)]
        (weather.humidity.toString() + resources.getString(R.string.percent)).also {
            binding.humidityValue.text = it
        }
        weather.icon.let {
            GlideToVectorYou.justLoadImage(
                activity,
                Uri.parse("https://yastatic.net/weather/i/icons/blueye/color/svg/${it}.svg"),
                binding.imageViewWeather
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle): FavoritesDetailsFragment {
            val fragment = FavoritesDetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}