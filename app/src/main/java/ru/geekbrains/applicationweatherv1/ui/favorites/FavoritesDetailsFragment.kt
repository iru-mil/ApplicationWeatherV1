package ru.geekbrains.applicationweatherv1.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.geekbrains.applicationweatherv1.databinding.DetailsFragmentBinding
import ru.geekbrains.applicationweatherv1.dataFactory.Weather

class FavoritesDetailsFragment : Fragment() {

    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?.let { weather ->
            weather.city.also { city ->
                binding.cityName.text = city.city
                binding.temperatureValue.text = weather.temp.toString()
                binding.feelsLike.append(weather.feelsLike.toString())
                binding.windSpeed.text = weather.windSpeed.toString()
                binding.windDirection.text = weather.windDir
                binding.humidityValue.text = weather.humidity.toString()
            }
        }
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