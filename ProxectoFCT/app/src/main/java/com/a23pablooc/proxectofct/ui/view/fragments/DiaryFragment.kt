package com.a23pablooc.proxectofct.ui.view.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.a23pablooc.proxectofct.databinding.FragmentDiaryBinding
import com.a23pablooc.proxectofct.ui.viewmodel.DiaryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiaryFragment : Fragment() {
    private lateinit var binding: FragmentDiaryBinding
    private val viewModel: DiaryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiaryBinding.inflate(layoutInflater)

        return binding.root
    }
}