package ru.geekbrains.applicationweatherv1.ui.favorites

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import ru.geekbrains.applicationweatherv1.R
import ru.geekbrains.applicationweatherv1.dataFactory.FactDTO
import ru.geekbrains.applicationweatherv1.databinding.DetailsFragmentBinding
import ru.geekbrains.applicationweatherv1.dataFactory.Weather
import ru.geekbrains.applicationweatherv1.dataFactory.WeatherDTO

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

private const val TEMP_INVALID = -100
private const val FEELS_LIKE_INVALID = -100
private const val WIND_SPEED_INVALID = -100
private const val HUMIDITY_INVALID = -100
private const val PROCESS_ERROR = "Обработка ошибки"

class FavoritesDetailsFragment : Fragment() {

    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

    private val loadResultsReceiver: BroadcastReceiver = object :
        BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_DATA_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_SUCCESS_EXTRA -> renderData(
                    WeatherDTO(
                        FactDTO(
                            intent.getIntExtra(DETAILS_TEMP_EXTRA, TEMP_INVALID),
                            intent.getIntExtra(DETAILS_FEELS_LIKE_EXTRA, FEELS_LIKE_INVALID),
                            intent.getIntExtra(DETAILS_WIND_SPEED_EXTRA, WIND_SPEED_INVALID),
                            intent.getStringExtra(DETAILS_WIND_DIR_EXTRA),
                            intent.getIntExtra(DETAILS_HUMIDITY_EXTRA, HUMIDITY_INVALID),
                            intent.getStringExtra(DETAILS_ICON_EXTRA)
                        )
                    )
                )
                else -> TODO(PROCESS_ERROR)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(
                    loadResultsReceiver,
                    IntentFilter(DETAILS_INTENT_FILTER)
                )
        }
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
        super.onDestroy()
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
        getWeather()
    }

    private fun getWeather() {
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        context?.let {
            it.startService(Intent(it, FavoritesDetailsService::class.java).apply {
                putExtra(
                    LATITUDE_EXTRA,
                    weatherBundle.city.lat
                )
                putExtra(
                    LONGITUDE_EXTRA,
                    weatherBundle.city.lon
                )
            })
        }
    }

    private fun renderData(weatherDTO: WeatherDTO) {
        binding.mainView.visibility = View.VISIBLE
        binding.loadingLayout.visibility = View.GONE
        val fact = weatherDTO.fact
        val temp = fact!!.temp
        val feelsLike = fact.feels_like
        val windSpeed = fact.wind_speed
        val windDir = fact.wind_dir
        val humidity = fact.humidity
        val icon = fact.icon
        if (temp == TEMP_INVALID || feelsLike == FEELS_LIKE_INVALID || windSpeed == WIND_SPEED_INVALID || windDir
            == null || humidity == HUMIDITY_INVALID || icon == null
        ) {
            TODO(PROCESS_ERROR)
        } else {
            val city = weatherBundle.city
            binding.cityName.text = city.city
            binding.temperatureValue.text = if (temp!! > 0) {
                "+" + temp.toString() + resources.getString(R.string.degree)
            } else {
                temp.toString() + resources.getString(R.string.degree)
            }
            (windSpeed.toString() + resources.getString(R.string.wind_units)).also {
                binding.windSpeed.text = it
            }
            binding.feelsLike.text = if ((feelsLike)!! > 0) {
                resources.getString(R.string.feels_like_label) + "+" + feelsLike.toString() + resources.getString(
                    R.string.degree
                )
            } else {
                resources.getString(R.string.feels_like_label) + feelsLike.toString() + resources.getString(
                    R.string.degree
                )
            }
            binding.windDirection.text = when (windDir) {
                "nw" -> {
                    binding.imageViewDirection.setImageResource(R.drawable.ic_up_left_icon); "СЗ"
                }
                "n" -> {
                    binding.imageViewDirection.setImageResource(R.drawable.ic_up_icon); "С"
                }
                "ne" -> {
                    binding.imageViewDirection.setImageResource(R.drawable.ic_up_right_icon); "СВ"
                }
                "e" -> {
                    binding.imageViewDirection.setImageResource(R.drawable.ic_right_icon); "В"
                }
                "se" -> {
                    binding.imageViewDirection.setImageResource(R.drawable.ic_down_right_icon); "ЮВ"
                }
                "s" -> {
                    binding.imageViewDirection.setImageResource(R.drawable.ic_down_icon); "Ю"
                }
                "sw" -> {
                    binding.imageViewDirection.setImageResource(R.drawable.ic_down_left_icon); "ЮЗ"
                }
                "w" -> {
                    binding.imageViewDirection.setImageResource(R.drawable.ic_left_icon); "З"
                }
                "c" -> {
                    "штиль"
                }
                else -> {
                    ""
                }
            }
            (humidity.toString() + resources.getString(R.string.percent)).also {
                binding.humidityValue.text = it
            }
            icon.let {
                GlideToVectorYou.justLoadImage(
                    activity,
                    Uri.parse("https://yastatic.net/weather/i/icons/blueye/color/svg/${it}.svg"),
                    binding.imageViewWeather
                )
            }
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