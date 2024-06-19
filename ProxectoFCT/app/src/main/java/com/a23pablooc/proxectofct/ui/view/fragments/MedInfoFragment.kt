package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.a23pablooc.proxectofct.databinding.FragmentMedInfoBinding
import com.a23pablooc.proxectofct.ui.viewmodel.MedInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MedInfoFragment : Fragment() {

    private lateinit var binding: FragmentMedInfoBinding
    private val viewModel: MedInfoViewModel by viewModels()

    object BundleKeys {
        const val MED_KEY = "med_key"
        const val ACTIVE_MED_KEY = "active_med_key"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedInfoBinding.inflate(inflater, container, false)

        binding.toolbar.setupWithNavController(findNavController())

        return binding.root
    }
}