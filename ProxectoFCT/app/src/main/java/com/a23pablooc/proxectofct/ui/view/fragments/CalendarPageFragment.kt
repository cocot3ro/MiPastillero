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
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatDate
import com.a23pablooc.proxectofct.core.DateTimeUtils.getDayName
import com.a23pablooc.proxectofct.databinding.FragmentCalendarPageBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.ui.view.adapters.CalendarPageMedGroupRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.view.states.UiState
import com.a23pablooc.proxectofct.ui.viewmodel.CalendarPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class CalendarPageFragment : Fragment() {
    private lateinit var binding: FragmentCalendarPageBinding
    private val viewModel: CalendarPageViewModel by viewModels()
    private lateinit var adapter: CalendarPageMedGroupRecyclerViewAdapter

    private lateinit var date: Date

    private object BundleKeys {
        const val ARGS_DATE_KEY = "ARG_DATE_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date = Date(it.getLong(BundleKeys.ARGS_DATE_KEY))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarPageBinding.inflate(layoutInflater)

        adapter = CalendarPageMedGroupRecyclerViewAdapter(
            dia = date,
            list = emptyList(),
            onMarcarToma = { med, dia, hora ->
                try {
                    viewModel.marcarToma(med, dia, hora)
                } catch (e: IllegalArgumentException) {
                    Toast.makeText(
                        context,
                        getString(R.string.only_today),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )

        binding.rvCalendarPage.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@CalendarPageFragment.adapter
        }

        val txt = "${date.getDayName(requireContext())} - ${date.formatDate()}"
        binding.calendarDay.text = txt

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success<*> -> {
                            binding.progressBar.visibility = View.GONE

                            val data = uiState.data.map { it as MedicamentoActivoItem }

                            adapter.updateData(data)

                            binding.emptyDataView.visibility =
                                if (data.isEmpty()) View.VISIBLE else View.GONE
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        viewModel.fetchData(requireContext(), date)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(date: Date) =
            CalendarPageFragment().apply {
                arguments = Bundle().apply {
                    putLong(BundleKeys.ARGS_DATE_KEY, date.time)
                }
            }
    }
}