package ru.geekbrains.applicationweatherv1

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.geekbrains.applicationweatherv1.databinding.MainActivityBinding
import ru.geekbrains.applicationweatherv1.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(getLayoutInflater())
        val view = binding.getRoot()
        setContentView(view)

        binding.navView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            val itemID = item.itemId
            if (itemID == R.id.navigation_main) {

            } else if (itemID == R.id.navigation_favorite) {

            } else if (itemID == R.id.navigation_settings) {

            }
            false
        })


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}