package com.example.uf1_proyecto.view

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.uf1_proyecto.R
import com.example.uf1_proyecto.databinding.ActivityMainBinding
import com.example.uf1_proyecto.utils.PreferencesUtils
import com.example.uf1_proyecto.viewModel.PillboxViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // TODO:
    //  1. recetas
    //  2. bookmarks en agenda. un botón en la toolbar para ver los dias marcados y poder seleccionar uno para ir a el (funcion search())

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    private lateinit var navController: NavController

    private lateinit var preferences: SharedPreferences

    init {
        Log.d("MainActivity", "init")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        _pillboxViewModel = PillboxViewModel.getInstance(this)
        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        preferences = getSharedPreferences(PreferencesUtils.PREFS_NAME, 0)

        setContentView(binding.root)

        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.IO) {
            pillboxViewModel.comprobarTerminados()
        }
    }

}
