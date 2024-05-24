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
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.FragmentFavoriteMedsBinding
import com.a23pablooc.proxectofct.ui.view.adapters.FavoriteRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.view.states.FavoriteFragmentUiState
import com.a23pablooc.proxectofct.ui.viewmodel.FavoriteMedsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteMedsFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteMedsBinding

    private val viewModel: FavoriteMedsViewModel by viewModels()
    private lateinit var navController: NavController

    private lateinit var favoriteRecyclerViewAdapter: FavoriteRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteMedsBinding.inflate(layoutInflater)

        navController = ((requireActivity().supportFragmentManager)
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.calendarFragment,
                R.id.activeMedsFragment,
                R.id.favoriteMedsFragment
            ),
            binding.drawerLayout
        )

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)

        favoriteRecyclerViewAdapter = FavoriteRecyclerViewAdapter(
            emptyList(),
            {
                // TODO
            }, {
                // TODO
            }
        )

        binding.recyclerViewFavoriteMeds.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteRecyclerViewAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is FavoriteFragmentUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is FavoriteFragmentUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            favoriteRecyclerViewAdapter.updateData(uiState.data)

                            // TODO: vista para lista vacia
                            //  ? binding.emptyListView.visibility = (uiState.data.isEmpty() ? View.VISIBLE : View.GONE)
                        }

                        is FavoriteFragmentUiState.Error -> {
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

        viewModel.fetchData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
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
    }

    private fun newFavMed() {
        // TODO
    }
}