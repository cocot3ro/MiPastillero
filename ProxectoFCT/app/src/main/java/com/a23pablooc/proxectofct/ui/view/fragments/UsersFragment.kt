package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.FragmentUsersBinding
import com.a23pablooc.proxectofct.ui.view.viewholders.UsersViewModel

class UsersFragment : Fragment() {
    private lateinit var binding: FragmentUsersBinding
    private val viewModel: UsersViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(layoutInflater)

        navController =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                ?.findNavController()!!

        binding.btnLogin.setOnClickListener {
            navController.navigate(R.id.mainScreenFragment)
        }

        return binding.root
    }
}