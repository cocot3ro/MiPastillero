package com.example.uf1_proyecto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.uf1_proyecto.databinding.EmptyLayoutBinding
import com.example.uf1_proyecto.databinding.FragmentHistoryBinding
import com.example.uf1_proyecto.databinding.HistoryMedGroupLayoutBinding
import com.example.uf1_proyecto.databinding.HistoryMedLayoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null

    private val binding get() = _binding!!

    private var _pillboxViewModel: PillboxViewModel? = null

    private val pillboxViewModel get() = _pillboxViewModel!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        _pillboxViewModel = PillboxViewModel.getInstance(requireContext())
        navController =
            ((activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.GONE

        val builder = AppBarConfiguration.Builder(navController.graph)
        val appBarConfiguration = builder.build()
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.toolbar.setOnMenuItemClickListener { menuItem -> menuItemSelected(menuItem) }

        cargarHistorial()

        return binding.root
    }

    private fun cargarHistorial() {
        val historial = pillboxViewModel.getHistorial()

        if (historial.isEmpty()) {
            EmptyLayoutBinding.inflate(layoutInflater, binding.historyLayout, true).apply {
                texto.text = getString(R.string.historial_vacio)
            }
        } else {
            for (entry in historial) {
                val groupBinding = HistoryMedGroupLayoutBinding.inflate(
                    layoutInflater,
                    binding.historyLayout,
                    true
                )

                @Suppress("SetTextI18n")
                groupBinding.mes.text = "${
                    DateTimeUtils.millisToYear(
                        entry.key,
                        requireContext()
                    )
                } - ${DateTimeUtils.millisToMonth(entry.key, requireContext())}"

                val colores = pillboxViewModel.getEstacionColor(entry.key)

                groupBinding.mes.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        colores.first
                    )
                )

                groupBinding.cuerpo.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        colores.second
                    )
                )

                for (med in entry.value) {
                    val historyEntryBinding = HistoryMedLayoutBinding.inflate(
                        layoutInflater,
                        groupBinding.historyMedsLayout,
                        true
                    )

                    historyEntryBinding.nombre.text = med.nombre

                    historyEntryBinding.dateStart.text =
                        DateTimeUtils.millisToDate(med.fechaInicio!!)

                    historyEntryBinding.dateEnd.text = DateTimeUtils.millisToDate(med.fechaFin!!)

                    for (hora in med.horario!!) {
                        historyEntryBinding.scheduleLayout.addView(
                            TextView(requireContext()).apply {
                                text = DateTimeUtils.millisToTime(hora)
                            }
                        )
                    }
                }

            }
        }
    }

    private fun menuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.calendarFragment, R.id.activeMedFragment, R.id.favoriteFragment,  R.id.diaryFragment -> {
                navController.navigate(menuItem.itemId)
                true
            }

            else -> false
        }
    }
}