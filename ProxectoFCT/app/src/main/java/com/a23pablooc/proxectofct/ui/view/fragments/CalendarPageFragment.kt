package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.a23pablooc.proxectofct.databinding.FragmentCalendarPageBinding
import com.a23pablooc.proxectofct.ui.view.adapters.CalendarAdapter
import com.a23pablooc.proxectofct.ui.view.states.CalendarPageUiState
import com.a23pablooc.proxectofct.ui.viewmodel.CalendarPageViewModel
import kotlinx.coroutines.launch

class CalendarPageFragment : Fragment() {
    private lateinit var binding: FragmentCalendarPageBinding

    private val viewModel: CalendarPageViewModel by viewModels()

    private lateinit var calendarAdapter: CalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarPageBinding.inflate(layoutInflater)

        calendarAdapter = CalendarAdapter(emptyList())

        binding.recyclerViewCalendarPage.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = calendarAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is CalendarPageUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is CalendarPageUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            calendarAdapter.updateData(uiState.data)
                        }

                        is CalendarPageUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
                            //TODO: save error message in a log file
                            //TODO: save error message in a database
                        }
                    }
                }
            }
        }

        return binding.root
    }
}