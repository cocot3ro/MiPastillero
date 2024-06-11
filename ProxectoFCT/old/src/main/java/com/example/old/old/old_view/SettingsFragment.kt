package com.example.old.old.old_view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.old.R
import com.example.old.databinding.OldFragmentSettingsBinding
import com.example.old.old.old_utils.PreferencesUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

@Deprecated("old")
class SettingsFragment : Fragment() {

    private var _binding: OldFragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OldFragmentSettingsBinding.inflate(inflater, container, false)
        navController =
            ((activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.old_nav_host_fragment) as NavHostFragment).navController
        preferences = requireContext().getSharedPreferences(PreferencesUtils.PREFS_NAME, AppCompatActivity.MODE_PRIVATE)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.GONE

        val builder = AppBarConfiguration.Builder(navController.graph)
        val appBarConfiguration = builder.build()
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        loadSettings()

        binding.fabSaveSettings.setOnClickListener {
            saveSettings()
        }

        return binding.root
    }

    private fun loadSettings() {
        binding.switchNotifications.isChecked =
            preferences.getBoolean(PreferencesUtils.KEYS.NOTIFICATIONS, true)
    }

    private fun saveSettings() {
        val editor = preferences.edit()
        editor.putBoolean(
            PreferencesUtils.KEYS.NOTIFICATIONS,
            binding.switchNotifications.isChecked
        )
        editor.apply()

        Toast.makeText(
            requireContext(),
            getString(R.string.ajustes_guardados),
            Toast.LENGTH_SHORT
        ).show()
    }
}
