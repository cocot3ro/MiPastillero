package com.example.uf1_proyecto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.uf1_proyecto.databinding.FragmentHistoryBinding
import com.example.uf1_proyecto.databinding.HistoryMedGroupLayoutBinding
import com.example.uf1_proyecto.databinding.HistoryMedLayoutBinding

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null

    private val binding get() = _binding!!

    private var _pillboxViewModel: PillboxViewModel? = null

    private val pillboxViewModel get() = _pillboxViewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        _pillboxViewModel = PillboxViewModel.getInstance(requireContext())

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        val navHostFragment =
            (activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val builder = AppBarConfiguration.Builder(navController.graph)
        val appBarConfiguration = builder.build()
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        cargarHistorial()

        return binding.root
    }

    private fun cargarHistorial() {
        val historial = pillboxViewModel.getHistorial()

        if (historial.isEmpty()) {
            binding.historyLayout.addView(
                TextView(requireContext()).apply {
                    text = getString(R.string.historial_vacio)
                }
            )
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
}