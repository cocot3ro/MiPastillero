package com.a23pablooc.proxectofct.ui.view.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.core.DateTimeUtils.zero
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import com.a23pablooc.proxectofct.databinding.FragmentCalendarBinding
import com.a23pablooc.proxectofct.ui.view.adapters.CalendarViewPagerAdapter
import com.a23pablooc.proxectofct.ui.viewmodel.CalendarViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private val viewModel: CalendarViewModel by viewModels()
    private lateinit var calendarViewPagerAdapter: CalendarViewPagerAdapter
    private lateinit var navController: NavController

    private var datePickerDialog: DatePickerDialog? = null

    companion object BundleKeys {
        private const val SHOWING_DATE_PICKER_DIALOG = "showingDatePickerDialog"
        private const val DATE_PICKER_DIALOG_DATE = "datePickerDialogDate"
    }

    //TODO: Drawer menu

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(layoutInflater)
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

        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.usersFragment -> {
                    navController.popBackStack(item.itemId, false)
                    true
                }

                R.id.manageUsersFragment -> {
                    navController.navigate(item.itemId)
                    true
                }

                else -> false
            }.also {
                if (it) binding.drawerLayout.close()
            }
        }

        calendarViewPagerAdapter = CalendarViewPagerAdapter(this)

        binding.viewPager.apply {
            adapter = calendarViewPagerAdapter
            offscreenPageLimit = 1
            setCurrentItem(CalendarViewPagerAdapter.START_POSITION, false)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MenuHost).addMenuProvider(object : MenuProvider {
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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            if (it.getBoolean(SHOWING_DATE_PICKER_DIALOG)) {
                val date = Date(it.getLong(DATE_PICKER_DIALOG_DATE))
                search(date)
            }
        }

        val d = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2024)
            set(Calendar.MONTH, 2)
            set(Calendar.DAY_OF_MONTH, 7)
        }.time

        search(d)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SHOWING_DATE_PICKER_DIALOG, datePickerDialog?.isShowing ?: false)
        val date = datePickerDialog?.let {
            Calendar.getInstance().apply {
                time = time.zero()
                set(Calendar.YEAR, datePickerDialog?.datePicker?.year ?: 0)
                set(Calendar.MONTH, datePickerDialog?.datePicker?.month ?: 0)
                set(Calendar.DAY_OF_MONTH, datePickerDialog?.datePicker?.dayOfMonth ?: 0)
            }.time
        }
        outState.putLong(DATE_PICKER_DIALOG_DATE, date?.time ?: 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        datePickerDialog = datePickerDialog?.let {
            it.dismiss()
            null
        }
    }

    private fun today() {
        binding.viewPager.setCurrentItem(CalendarViewPagerAdapter.START_POSITION, true)
    }

    private fun search(date: Date? = null) {
        val calendar = Calendar.getInstance().apply {
            time = date ?: Calendar.getInstance().time
            time = time.zeroTime()
        }

        datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val offset = viewModel.calculateOffset(year, monthOfYear, dayOfMonth)
                binding.viewPager.setCurrentItem(
                    CalendarViewPagerAdapter.START_POSITION + offset,
                    true
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).also {
            it.show()
        }
    }
}