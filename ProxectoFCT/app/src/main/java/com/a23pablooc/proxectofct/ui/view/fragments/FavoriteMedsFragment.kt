package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.FragmentFavoriteMedsBinding
import com.a23pablooc.proxectofct.databinding.NavHeaderBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.ui.view.adapters.FavoriteRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.view.states.UiState
import com.a23pablooc.proxectofct.ui.viewmodel.FavoriteMedsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteMedsFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteMedsBinding
    private val viewModel: FavoriteMedsViewModel by viewModels()
    private lateinit var navController: NavController

    private lateinit var favoriteRecyclerViewAdapter: FavoriteRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteMedsBinding.inflate(layoutInflater)

        navController = requireActivity().supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)?.findNavController()!!

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

        binding.navView.apply {
            setNavigationItemSelectedListener { item ->
                when (item.itemId) {

                    R.id.addActiveMedFragment,
                    R.id.historyFragment,
                    R.id.diaryFragment -> {
                        navController.navigate(item.itemId)
                        true
                    }

                    else -> false
                }.also {
                    if (it) binding.drawerLayout.close()
                }
            }

            NavHeaderBinding.bind(getHeaderView(0)).apply {
                userName.text = viewModel.userInfoProvider.currentUser.nombre

                ibSettings.setOnClickListener {
                    binding.drawerLayout.close()
                    navController.navigate(R.id.settingsFragment)
                }

                ibLogout.setOnClickListener {
                    binding.drawerLayout.close()
                    navController.popBackStack(R.id.usersFragment, false)
                }
            }
        }

        favoriteRecyclerViewAdapter = FavoriteRecyclerViewAdapter(
            emptyList(),
            onAdd = { onAdd(it) },
            onInfo = { onInfo(it) }
        )

        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteRecyclerViewAdapter
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

                            val data = uiState.data.map { it as MedicamentoItem }

                            favoriteRecyclerViewAdapter.updateData(data)

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

        viewModel.fetchData()

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
                    R.id.addActiveMed -> {
                        navController.navigate(R.id.addActiveMedFragment)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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