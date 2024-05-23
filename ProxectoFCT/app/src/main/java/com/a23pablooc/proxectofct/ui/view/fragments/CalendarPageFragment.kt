package com.a23pablooc.proxectofct.ui.view.fragments

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
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
import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.databinding.FragmentCalendarPageBinding
import com.a23pablooc.proxectofct.ui.view.adapters.CalendarRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.view.states.CalendarPageUiState
import com.a23pablooc.proxectofct.ui.viewmodel.CalendarPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalendarPageFragment : Fragment() {
    private lateinit var binding: FragmentCalendarPageBinding

    private val viewModel: CalendarPageViewModel by viewModels<CalendarPageViewModel>()

    private lateinit var calendarRecyclerViewAdapter: CalendarRecyclerViewAdapter

    companion object {
        const val ARGS_DATE_KEY = "DATE_KEY"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarPageBinding.inflate(layoutInflater)

        calendarRecyclerViewAdapter = CalendarRecyclerViewAdapter(emptyList())

        binding.recyclerViewCalendarPage.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = calendarRecyclerViewAdapter
        }

        val date = Calendar.getInstance().apply {
            timeInMillis = requireArguments().getLong(ARGS_DATE_KEY)
        }.time

        binding.calendarDay.text = "${
            DateTimeUtils.getDayName(
                requireContext(),
                date
            )
        } - ${DateTimeUtils.formatDate(date)}"

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is CalendarPageUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is CalendarPageUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            calendarRecyclerViewAdapter.updateData(uiState.data)

                            // TODO: vista para lista vacia
                            // binding.emptyListView.visibility = (uiState.data.isEmpty() ? View.VISIBLE : View.GONE)
                        }

                        is CalendarPageUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
                            Log.e("CalendarPageFragment", "Error: ${uiState.errorMessage}")
                            Log.e("CalendarPageFragment", "Error: ${uiState.error}")
                            //TODO: save error message in a log file
                        }
                    }
                }
            }
        }

        viewModel.fetchData(date)

        return binding.root
    }
}