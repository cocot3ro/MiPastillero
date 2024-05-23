package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.FragmentMainScreenBinding
import com.a23pablooc.proxectofct.ui.viewmodel.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private val viewModel: MainScreenViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)

        navController =
            ((requireActivity().supportFragmentManager).findFragmentById(R.id.global_nav_host_fragment) as NavHostFragment)
                .navController

        binding.bottomNavigation.setupWithNavController(navController)

        return binding.root
    }
}