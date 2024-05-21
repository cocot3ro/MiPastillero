package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.a23pablooc.proxectofct.databinding.FragmentFavoriteMedsBinding
import com.a23pablooc.proxectofct.ui.viewmodel.FavoriteMedsViewModel

class FavoriteMedsFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteMedsBinding

    private val viewModel: FavoriteMedsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteMedsBinding.inflate(layoutInflater)
        // TODO: crear la vista

        return binding.root
    }
}