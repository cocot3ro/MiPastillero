package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.a23pablooc.proxectofct.databinding.FragmentActiveMedsBinding
import com.a23pablooc.proxectofct.ui.viewmodel.ActiveMedsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActiveMedsFragment : Fragment() {
    private lateinit var binding: FragmentActiveMedsBinding

    private val viewModel: ActiveMedsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActiveMedsBinding.inflate(layoutInflater)
        // TODO: crear la vista

        return binding.root
    }
}