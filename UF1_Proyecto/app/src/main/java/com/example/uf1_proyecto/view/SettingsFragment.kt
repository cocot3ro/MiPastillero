package com.example.uf1_proyecto.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
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
import com.example.uf1_proyecto.R
import com.example.uf1_proyecto.databinding.FragmentSettingsBinding
import com.example.uf1_proyecto.utils.PreferencesUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        navController =
            ((activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        preferences = requireContext().getSharedPreferences(PreferencesUtils.PREFS_NAME, 0)

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

    fun foo() {
        val alarmManager =
            requireContext().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

        val intent = Intent(requireContext(), BroadcastReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)
    }
}
