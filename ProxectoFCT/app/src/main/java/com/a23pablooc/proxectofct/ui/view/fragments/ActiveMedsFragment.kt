package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.FragmentActiveMedsBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.ui.view.adapters.ActiveMedsRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.view.states.MainScreenUiState
import com.a23pablooc.proxectofct.ui.viewmodel.ActiveMedsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ActiveMedsFragment : Fragment() {
    private lateinit var binding: FragmentActiveMedsBinding
    private val viewModel: ActiveMedsViewModel by viewModels()
    private lateinit var activeRecyclerViewAdapter: ActiveMedsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActiveMedsBinding.inflate(layoutInflater)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.calendarFragment,
                R.id.activeMedsFragment,
                R.id.favoriteMedsFragment
            ),
            binding.drawerLayout
        )

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)

        activeRecyclerViewAdapter = ActiveMedsRecyclerViewAdapter(
            emptyList(),
            {
                // TODO: onFavClick
            }, {
                // TODO: onInfoClick
            }, {
                // TODO: onAddClick
            }
        )

        binding.recyclerViewActiveMeds.apply {
            adapter = activeRecyclerViewAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is MainScreenUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is MainScreenUiState.Success<*> -> {
                            binding.progressBar.visibility = View.GONE
                            activeRecyclerViewAdapter.updateData(uiState.data.map { it as MedicamentoActivoItem })

                            // TODO: vista para lista vacia
                            // binding.emptyListView.visibility = (uiState.data.isEmpty() ? View.VISIBLE : View.GONE)
                        }

                        is MainScreenUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
                            Log.e(
                                "ActiveMedsFragment::OnCreateView",
                                "Error: ${uiState.errorMessage}"
                            )
                        }
                    }
                }
            }
        }

        viewModel.fetchData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_toolbar_active_med, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.addActiveMed -> {
                        addActiveMed()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun addActiveMed() {
        // TODO
    }
}