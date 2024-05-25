package com.a23pablooc.proxectofct.old.old_view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.a23pablooc.proxectofct.old.old_utils.DateTimeUtils
import com.a23pablooc.proxectofct.old.old_model.Medicamento
import com.a23pablooc.proxectofct.old.old_viewModel.PillboxViewModel
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.OldFragmentCalendarBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

@Deprecated("old")
class CalendarFragment : Fragment() {
    private var _binding: OldFragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var pillboxViewModel: PillboxViewModel
    private lateinit var navController: NavController
    private lateinit var pagerAdapter: CalendarPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = OldFragmentCalendarBinding.inflate(layoutInflater)
        pillboxViewModel = PillboxViewModel.getInstance(requireContext())
        navController =
            ((activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.old_nav_host_fragment) as NavHostFragment).navController
        pagerAdapter = CalendarPagerAdapter.getInstance(requireActivity())
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        setHasOptionsMenu(true)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.VISIBLE

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.calendarFragment,
                R.id.activeMedFragment,
                R.id.favoriteFragment,
                R.id.diaryFragment
            ),
            binding.drawerLayout
        )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.toolbar.setOnMenuItemClickListener { menuItem -> menuItemSelected(menuItem) }

        binding.navView.setupWithNavController(navController)
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            menuItemSelected(menuItem)
        }

        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.setCurrentItem(CalendarPagerAdapter.START_POSITION, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        pagerAdapter.reload()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_calendar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun today() {
        binding.viewPager.setCurrentItem(CalendarPagerAdapter.START_POSITION, true)
    }

    private fun search() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val date = DateTimeUtils.createDate(
                    year, monthOfYear, dayOfMonth
                )

                binding.viewPager.setCurrentItem(pagerAdapter.search(date), true)
            },

            // Establece la fecha actual como predeterminada
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    fun reload() {
        pagerAdapter.reload()
    }

    private fun addMedDialog() {
        AddActiveMedDialog(
            requireContext(),
            object : AddActiveMedDialog.OnDataEnteredListener {
                override fun onDataEntered(medicamento: Medicamento) {
                    if (!medicamento.isFavorite!!) {
                        if (pillboxViewModel.addActiveMed(requireContext(), medicamento)) {
                            Toast.makeText(
                                context,
                                getString(R.string.añadir_activo_ok),
                                Toast.LENGTH_LONG
                            ).show()
                            reload()
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.añadir_activo_error),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        // Si se elige guardar tambien como favorito, se añade a la lista de favoritos
                        val addActive = pillboxViewModel.addActiveMed(requireContext(), medicamento)
                        val addFav = pillboxViewModel.addFavMed(medicamento)

                        if (addActive && addFav) {
                            Toast.makeText(
                                context,
                                getString(R.string.añadir_activo_ok),
                                Toast.LENGTH_LONG
                            ).show()
                            reload()
                        } else if (addActive) {
                            Toast.makeText(
                                context,
                                getString(R.string.añadir_fav_error),
                                Toast.LENGTH_LONG
                            ).show()
                            reload()
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.añadir_activo_error),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }).show()
    }

    private fun menuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.search -> {
                search()
                true
            }

            R.id.today -> {
                today()
                true
            }

            R.id.addActiveMed -> {
                addMedDialog()
                true
            }

            R.id.diaryFragment, R.id.historyFragment, R.id.settingsFragment -> {
                navController.navigate(menuItem.itemId)
                true
            }

            else -> false
        }
    }

}
