package com.a23pablooc.proxectofct.old_view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.OldActivityMainBinding
import com.a23pablooc.proxectofct.old_utils.PreferencesUtils
import com.a23pablooc.proxectofct.old_viewModel.PillboxViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: OldActivityMainBinding? = null
    private val binding get() = _binding!!

    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    private lateinit var navController: NavController

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = OldActivityMainBinding.inflate(layoutInflater)
        _pillboxViewModel = PillboxViewModel.getInstance(this)
        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        preferences = getSharedPreferences(PreferencesUtils.PREFS_NAME, MODE_PRIVATE)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            pillboxViewModel.comprobarTerminados()
        }
    }

}
