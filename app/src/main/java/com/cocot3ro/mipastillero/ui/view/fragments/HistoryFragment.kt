package com.cocot3ro.mipastillero.ui.view.fragments

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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.databinding.FragmentHistoryBinding
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.domain.model.MedicamentoItem
import com.cocot3ro.mipastillero.ui.view.adapters.HistoryGroupRecyclerViewAdapter
import com.cocot3ro.mipastillero.ui.view.states.UiState
import com.cocot3ro.mipastillero.ui.viewmodel.HistoryViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    @Inject
    lateinit var gson: Gson

    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by viewModels()

    private lateinit var historyGroupRecyclerViewAdapter: HistoryGroupRecyclerViewAdapter

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)

        binding.toolbar.setupWithNavController(navController)

        historyGroupRecyclerViewAdapter = HistoryGroupRecyclerViewAdapter(
            list = emptyList(),
            onInfo = {
                navController.navigate(R.id.medInfoFragment, Bundle().apply {
                    putString(MedInfoFragment.BundleKeys.MED_KEY, gson.toJson(it, MedicamentoItem::class.java))
                })
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

                            val data = uiState.data.map { it as MedicamentoActivoItem }

                            binding.emptyDataView.visibility =
                                if (data.isEmpty()) View.VISIBLE else View.GONE

                            historyGroupRecyclerViewAdapter.updateData(data)
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        viewModel.fetchData(requireContext())

        return binding.root
    }
}