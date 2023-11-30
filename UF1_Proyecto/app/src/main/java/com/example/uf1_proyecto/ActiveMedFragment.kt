package com.example.uf1_proyecto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.uf1_proyecto.databinding.FragmentActiveMedBinding

class ActiveMedFragment : Fragment() {
    private var _binding: FragmentActiveMedBinding? = null
    private val binding get() = _binding!!

    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActiveMedBinding.inflate(inflater, container, false)
        _pillboxViewModel = PillboxViewModel.getInstance(requireContext())

        val view = binding.root

        setHasOptionsMenu(true)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        val navHostFragment =
            (activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.toolbar.setupWithNavController(navController)

//        pillboxViewModel.ejemplosActivos()

        pillboxViewModel.getActivosHoy().forEach { addCardView(it) }

        return view
    }

    private fun addCardView(medicamento: Medicamento) {
        val activeMedLayout: LinearLayout = binding.activeMedLayout

        val inflater = LayoutInflater.from(requireContext())

        val cardLayout =
            inflater.inflate(R.layout.active_med_card_layout, activeMedLayout, false) as CardView

        cardLayout.findViewById<TextView>(R.id.name).text = medicamento.nombre

        cardLayout.findViewById<TextView>(R.id.date_start).text =
            pillboxViewModel.millisToDate(medicamento.fechaInicio)

        cardLayout.findViewById<TextView>(R.id.date_end).text =
            pillboxViewModel.millisToDate(medicamento.fechaFin)

        activeMedLayout.addView(cardLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_import_export, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}