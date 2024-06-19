package com.a23pablooc.proxectofct.ui.view.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a23pablooc.proxectofct.databinding.FragmentReuseMedBinding
import com.a23pablooc.proxectofct.ui.viewmodel.ReuseMedViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReuseMedFragment : Fragment() {

    @Inject
    lateinit var gson: Gson

    private lateinit var binding: FragmentReuseMedBinding
    private val viewModel: ReuseMedViewModel by viewModels()

    object BundleKeys {
        const val MED_KEY = "med_key"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReuseMedBinding.inflate(layoutInflater)

        return binding.root
    }
}