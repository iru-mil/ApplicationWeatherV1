package ru.geekbrains.applicationweatherv1.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.applicationweatherv1.databinding.SettingsFragmentBinding

const val IS_ADD_GREETING = "HOME WITH GREETING"

class SettingsFragment : Fragment() {

    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingsViewModel: SettingsViewModel
    private var isAddedGreeting: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        activity?.let {
            binding.checkbox.isChecked =
                it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_ADD_GREETING, true)
        }
        binding.checkbox.setOnClickListener(View.OnClickListener { saveSettings() })
    }

    fun saveSettings() {
        if (binding.checkbox.isChecked) {
            activity?.let {
                with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                    putBoolean(IS_ADD_GREETING, isAddedGreeting)
                    apply()
                }
            }
        } else {
            activity?.let {
                with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                    putBoolean(IS_ADD_GREETING, !isAddedGreeting)
                    apply()
                }
            }
        }
    }
}