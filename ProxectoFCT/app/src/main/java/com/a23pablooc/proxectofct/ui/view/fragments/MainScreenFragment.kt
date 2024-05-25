package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.FragmentMainScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(layoutInflater)

        val navController =
            childFragmentManager.findFragmentById(R.id.nested_host_fragment)?.findNavController()!!

        binding.bottomNavigation.setupWithNavController(navController)

        return binding.root
    }
}