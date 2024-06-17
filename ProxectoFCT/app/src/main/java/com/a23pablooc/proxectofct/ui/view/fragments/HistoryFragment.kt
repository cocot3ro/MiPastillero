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
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.a23pablooc.proxectofct.databinding.FragmentHistoryBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.ui.view.adapters.HistoryGroupRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.view.states.UiState
import com.a23pablooc.proxectofct.ui.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by viewModels()

    private lateinit var historyGroupRecyclerViewAdapter: HistoryGroupRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)

        binding.toolbar.setupWithNavController(findNavController())

        historyGroupRecyclerViewAdapter = HistoryGroupRecyclerViewAdapter(
            emptyList(),
            onInfo = {
                // TODO
            }
        )

        binding.rv.apply {
            adapter = historyGroupRecyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success<*> -> {
                            binding.progressBar.visibility = View.GONE

                            historyGroupRecyclerViewAdapter.updateData(uiState.data.map { it as MedicamentoActivoItem })

                            // TODO: vista para lista vacia
                            //  ? binding.emptyListView.visibility = (uiState.data.isEmpty() ? View.VISIBLE : View.GONE)
                            // Toast.makeText(context, "Empty list", Toast.LENGTH_SHORT).show()
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        viewModel.fetchData()

        return binding.root
    }
}