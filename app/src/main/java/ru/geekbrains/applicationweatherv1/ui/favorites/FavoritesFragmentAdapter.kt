package ru.geekbrains.applicationweatherv1.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.geekbrains.applicationweatherv1.R
import ru.geekbrains.applicationweatherv1.dataFactory.Weather

class FavoritesFragmentAdapter(private var onItemViewClickListener: FavoritesFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<FavoritesFragmentAdapter.MainViewHolder>() {
    private var weatherData: List<Weather> = listOf()

    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.favorites_fragment_recycler_item, parent, false) as
                    View
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            itemView.apply {
                findViewById<TextView>(R.id.recyclerItemTextView).text =
                    weather.city.city
                setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(weather)
                }
            }
        }
    }
}