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
import com.a23pablooc.proxectofct.databinding.FragmentFavoriteMedsBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.ui.view.adapters.FavoriteRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.view.states.MainScreenUiState
import com.a23pablooc.proxectofct.ui.viewmodel.FavoriteMedsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteMedsFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteMedsBinding
    private val viewModel: FavoriteMedsViewModel by viewModels()
    private lateinit var favoriteRecyclerViewAdapter: FavoriteRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteMedsBinding.inflate(layoutInflater)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.calendarFragment,
                R.id.activeMedsFragment,
                R.id.favoriteMedsFragment
            ),
            binding.drawerLayout
        )

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)

        favoriteRecyclerViewAdapter = FavoriteRecyclerViewAdapter(
            emptyList(),
            onAdd = { onAdd(it) },
            onInfo = { onInfo(it) }
        )

        binding.recyclerViewFavoriteMeds.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteRecyclerViewAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_toolbar_fav_med, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.newFavMed -> {
                        newFavMed()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is MainScreenUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is MainScreenUiState.Success<*> -> {
                            binding.progressBar.visibility = View.GONE

                            favoriteRecyclerViewAdapter.updateData(uiState.data.map { it as MedicamentoItem })

                            // TODO: vista para lista vacia
                            //  ? binding.emptyListView.visibility = (uiState.data.isEmpty() ? View.VISIBLE : View.GONE)
                            Toast.makeText(context, "Empty list", Toast.LENGTH_LONG).show()
                        }

                        is MainScreenUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
                            Log.e("FavoriteMedsFragment", "Error: ${uiState.errorMessage}")
                        }
                    }
                }
            }
        }

        viewModel.fetchData()
    }

    private fun newFavMed() {
        // TODO
    }

    private fun onAdd(med: MedicamentoItem) {
        // TODO
        Toast.makeText(context, "Añadir medicamento: ${med.nombre}", Toast.LENGTH_LONG).show()
    }

    private fun onInfo(med: MedicamentoItem) {
        // TODO
        Toast.makeText(context, "Info medicamento: ${med.nombre}", Toast.LENGTH_LONG).show()
    }
}