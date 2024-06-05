package com.a23pablooc.proxectofct.ui.view.fragments

import android.annotation.SuppressLint
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
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatDate
import com.a23pablooc.proxectofct.core.DateTimeUtils.getDayName
import com.a23pablooc.proxectofct.databinding.FragmentCalendarPageBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.ui.view.adapters.CalendarRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.view.states.UiState
import com.a23pablooc.proxectofct.ui.viewmodel.CalendarPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Date

private const val ARGS_DATE_KEY = "ARG_DATE_KEY"

@AndroidEntryPoint
class CalendarPageFragment : Fragment() {
    private lateinit var binding: FragmentCalendarPageBinding
    private val viewModel: CalendarPageViewModel by viewModels<CalendarPageViewModel>()
    private lateinit var calendarRecyclerViewAdapter: CalendarRecyclerViewAdapter

    private lateinit var date: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date = Date(it.getLong(ARGS_DATE_KEY))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarPageBinding.inflate(layoutInflater)

        calendarRecyclerViewAdapter =
            CalendarRecyclerViewAdapter(date, emptyList()) { med, dia, hora ->
                viewModel.marcarToma(med, dia, hora)
            }

        binding.rvCalendarPage.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = calendarRecyclerViewAdapter
        }

        binding.calendarDay.text = "${date.getDayName(requireContext())} - ${date.formatDate()}"

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success<*> -> {
                            binding.progressBar.visibility = View.GONE
                            calendarRecyclerViewAdapter.updateData(uiState.data.map { it as MedicamentoActivoItem })
                            // TODO: vista para lista vacia
                            // binding.emptyListView.visibility = (uiState.data.isEmpty() ? View.VISIBLE : View.GONE)
                            // Toast.makeText(context, "Empty list", Toast.LENGTH_SHORT).show()
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
                            Log.e("CalendarPageFragment", uiState.errorMessage, uiState.exception)
                        }
                    }
                }
            }
        }

        viewModel.fetchData(date)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(date: Date) =
            CalendarPageFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARGS_DATE_KEY, date.time)
                }
            }
    }
}