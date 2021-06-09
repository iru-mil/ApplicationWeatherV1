package ru.geekbrains.applicationweatherv1.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorites_fragment.*
import ru.geekbrains.applicationweatherv1.R
import ru.geekbrains.applicationweatherv1.dataFactory.Weather
import ru.geekbrains.applicationweatherv1.databinding.FavoritesFragmentBinding
import ru.geekbrains.applicationweatherv1.ui.home.AppState

class FavoritesFragment : Fragment() {
    private var _binding: FavoritesFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by lazy {
        ViewModelProvider(this).get(FavoritesViewModel::class.java)
    }
    private val adapter = FavoritesFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .add(R.id.container, FavoritesDetailsFragment.newInstance(Bundle().apply {
                        putParcelable(FavoritesDetailsFragment.BUNDLE_EXTRA, weather)
                    }))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })
    private var isDataSetRus: Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favoritesRecyclerView.adapter = adapter
        binding.favoritesFAB.setOnClickListener { changeWeatherDataSet() }
        viewModel.getLiveData().observe(viewLifecycleOwner, {
            renderData(it)
        })
        viewModel.getWeatherFromLocalSourceRus()
    }

    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }

    private fun changeWeatherDataSet() =
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            favoritesFAB.setImageResource(R.drawable.ic_earth_globe)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            favoritesFAB.setImageResource(R.drawable.ic_kremlin_russia)
        }.also { isDataSetRus = !isDataSetRus }

    private fun View.showSnackBar(
        text: String,
        actionText: String,
        action: (View) -> Unit,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.favoritesLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.favoritesLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                favoritesLoadingLayout.visibility = View.GONE
                favoritesContainer.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getWeatherFromLocalSourceRus() }
                )

            }
            else -> return
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }

}