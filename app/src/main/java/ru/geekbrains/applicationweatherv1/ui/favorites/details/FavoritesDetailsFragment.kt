package ru.geekbrains.applicationweatherv1.ui.favorites.details

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
import ru.geekbrains.applicationweatherv1.dataFactory.City
import ru.geekbrains.applicationweatherv1.dataFactory.Weather
import ru.geekbrains.applicationweatherv1.databinding.DetailsFragmentBinding
import ru.geekbrains.applicationweatherv1.dataFactory.app.AppState
import ru.geekbrains.applicationweatherv1.utils.showSnackBar

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
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                setWeather(appState.weatherData[0])
            }
            is AppState.Loading -> {
                binding.mainView.visibility = View.GONE
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainView.visibility = View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
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
        saveCity(city, weather)
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
        val sourceDirArray = res.getStringArray(R.array.source_dir)
        val appDirArray = res.getStringArray(R.array.app_dir)
        binding.windDirection.text = appDirArray[sourceDirArray.indexOf(weather.windDir)]
        val iconDirArray = res.getStringArray(R.array.icon_dir)
        val resourceID = this.getResources().getIdentifier(
            iconDirArray[sourceDirArray.indexOf(weather.windDir)],
            "drawable", getActivity()?.getPackageName()
        )
        binding.imageViewDirection.setImageResource(resourceID)
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

    private fun saveCity(
        city: City,
        weather: Weather
    ) {
        detailsViewModel.saveCityToDB(
            Weather(
                city,
                weather.temp,
                weather.feelsLike,
                weather.icon,
                weather.windSpeed,
                weather.windDir,
                weather.humidity
            )
        )
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