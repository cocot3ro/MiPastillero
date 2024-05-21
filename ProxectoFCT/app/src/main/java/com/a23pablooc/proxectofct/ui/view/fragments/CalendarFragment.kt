package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.a23pablooc.proxectofct.databinding.FragmentCalendarBinding
import com.a23pablooc.proxectofct.ui.viewmodel.CalendarViewModel

class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding

    private val viewModel: CalendarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
        // TODO: crear la vista

        return binding.root
    }
}