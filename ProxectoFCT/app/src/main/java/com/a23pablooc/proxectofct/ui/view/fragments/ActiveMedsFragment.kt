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
import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.databinding.FragmentActiveMedsBinding
import com.a23pablooc.proxectofct.databinding.NavHeaderBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.ui.view.adapters.ActiveMedsRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.view.states.UiState
import com.a23pablooc.proxectofct.ui.viewmodel.ActiveMedsViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ActiveMedsFragment : Fragment() {

    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var userInfoProvider: UserInfoProvider

    private lateinit var binding: FragmentActiveMedsBinding
    private val viewModel: ActiveMedsViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var activeRecyclerViewAdapter: ActiveMedsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActiveMedsBinding.inflate(layoutInflater)

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
                userName.text = userInfoProvider.currentUser.nombre

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

        activeRecyclerViewAdapter = ActiveMedsRecyclerViewAdapter(
            emptyList(),
            onFav = {
                viewModel.toggleFavMed(it.fkMedicamento)
            },
            onInfo = {
                navController.navigate(R.id.medInfoFragment, Bundle().apply {
//                    viewModel.gson
                })
            }
        )

        binding.rvActiveMeds.apply {
            adapter = activeRecyclerViewAdapter
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

                            activeRecyclerViewAdapter.updateData(data)

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
        (activity as MenuHost).addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_toolbar_active_med, menu)
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
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }
}