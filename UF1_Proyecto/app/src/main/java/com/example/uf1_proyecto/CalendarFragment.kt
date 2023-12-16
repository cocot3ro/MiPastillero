package com.example.uf1_proyecto

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.uf1_proyecto.databinding.CalendarMedGroupLayoutBinding
import com.example.uf1_proyecto.databinding.CalendarMedLayoutBinding
import com.example.uf1_proyecto.databinding.FragmentCalendarBinding
import java.util.Calendar

class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var pillboxViewModel: PillboxViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        pillboxViewModel = PillboxViewModel.getInstance(requireContext())

        val view = binding.root

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        setHasOptionsMenu(true)

        val navHostFragment =
            (activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.calendarFragment,
                R.id.activeMedFragment,
                R.id.favoriteFragment,
                R.id.diaryFragment
            ), binding.drawerLayout
        )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        val navigationView = binding.navView
        navigationView.setupWithNavController(navController)

        binding.prevDay.setOnClickListener {
            pillboxViewModel.calendarPrevDay()
            updateView()
        }

        binding.nextDay.setOnClickListener {
            pillboxViewModel.calendarNextDay()
            updateView()
        }

        updateView()

        return view
    }

    private fun updateView() {
        @Suppress("SetTextI18n")
        binding.calendarDay.text = "${
            DateTimeUtils.millisToDayOfWeek(
                pillboxViewModel.getCalendarCurrDate(),
                requireContext()
            )
        } - ${DateTimeUtils.millisToDate(pillboxViewModel.getCalendarCurrDate())}"

        binding.calendarLayout.removeAllViews()

        val map = pillboxViewModel.getActivosCalendario(pillboxViewModel.getCalendarCurrDate())

        if (map.isEmpty()) {
            binding.calendarLayout.addView(
                TextView(requireContext()).apply {
                    text = getString(R.string.no_meds_for_this_day)
                }
            )
        } else {
            for (entry in map) {
                val groupBinding =
                    CalendarMedGroupLayoutBinding.inflate(
                        layoutInflater,
                        binding.calendarLayout,
                        true
                    )

                groupBinding.hora.text = DateTimeUtils.millisToTime(entry.key)

                for (med in entry.value) {
                    val calendarEntryBinding = CalendarMedLayoutBinding.inflate(
                        layoutInflater, groupBinding.calendarMedsLayout, true
                    )

                    calendarEntryBinding.nombre.text = med.nombre

                    if (med.seHaTomado!!) {
                        calendarEntryBinding.btn.setImageResource(android.R.drawable.checkbox_on_background)
                    }

                    calendarEntryBinding.btn.setOnClickListener {
                        if (med.seHaTomado!!) {
                            if (pillboxViewModel.desmarcarToma(
                                    med,
                                    entry.key,
                                    pillboxViewModel.getCalendarCurrDate()
                                )
                            ) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.medUnmarkOK),
                                    Toast.LENGTH_LONG
                                ).show()
                                med.seHaTomado = false
                                calendarEntryBinding.btn.setImageResource(android.R.drawable.checkbox_off_background)
                            } else {
                                Toast.makeText(
                                    context,
                                    getString(R.string.medUnmarkFail), Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            if (pillboxViewModel.marcarToma(
                                    med,
                                    entry.key,
                                    pillboxViewModel.getCalendarCurrDate()
                                )
                            ) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.medMarkOK), Toast.LENGTH_LONG
                                ).show()
                                med.seHaTomado = true
                                calendarEntryBinding.btn.setImageResource(android.R.drawable.checkbox_on_background)
                            } else {
                                Toast.makeText(
                                    context,
                                    getString(R.string.medMarkFail), Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun search() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                pillboxViewModel.setCalendarCurrDate(
                    DateTimeUtils.createDate(
                        year, monthOfYear, dayOfMonth
                    )
                )
                updateView()
            },
            // Establece la fecha actual como predeterminada
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun today() {
        pillboxViewModel.setCalendarCurrDate(DateTimeUtils.getTodayAsMillis())
        updateView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_calendar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                search()
                true
            }

            R.id.today -> {
                today()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
