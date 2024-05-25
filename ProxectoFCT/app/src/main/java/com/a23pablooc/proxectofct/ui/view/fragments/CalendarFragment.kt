package com.a23pablooc.proxectofct.ui.view.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.FragmentCalendarBinding
import com.a23pablooc.proxectofct.ui.view.adapters.CalendarViewPagerAdapter
import com.a23pablooc.proxectofct.ui.viewmodel.CalendarViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private val viewModel: CalendarViewModel by viewModels()
    private lateinit var calendarViewPagerAdapter: CalendarViewPagerAdapter

    //TODO: Drawer menu

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(layoutInflater)

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

        calendarViewPagerAdapter =
            CalendarViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)

        binding.viewPager.apply {
            adapter = calendarViewPagerAdapter
            setCurrentItem(CalendarViewPagerAdapter.START_POSITION, false)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_toolbar_calendar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.today -> {
                        today()
                        true
                    }

                    R.id.search -> {
                        search()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun today() {
        binding.viewPager.setCurrentItem(CalendarViewPagerAdapter.START_POSITION, true)
    }

    private fun search() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val offset = viewModel.calculateOffset(year, monthOfYear, dayOfMonth)
                binding.viewPager.setCurrentItem(
                    CalendarViewPagerAdapter.START_POSITION + offset,
                    true
                )
            },

            // Establece la fecha actual como predeterminada
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }
}