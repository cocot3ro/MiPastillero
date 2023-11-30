package com.example.uf1_proyecto

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.uf1_proyecto.databinding.FragmentActiveMedBinding
import com.google.android.material.snackbar.Snackbar

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

        val activos = pillboxViewModel.getActivos()

        if (activos.isEmpty()) {
            binding.noActiveMedsText.text = getString(R.string.no_active_meds)
        } else {
            binding.activeMedLayout.removeView(binding.noActiveMedsText)
            activos.forEach { addCardView(it) }
        }

        return view
    }

    private fun addCardView(medicamento: Medicamento) {
        val inflater = LayoutInflater.from(requireContext())

        val cardView =
            inflater.inflate(
                R.layout.active_med_card_layout,
                binding.activeMedLayout,
                false
            ) as CardView

        cardView.findViewById<TextView>(R.id.name).text = medicamento.nombre

        cardView.findViewById<TextView>(R.id.date_start).text =
            pillboxViewModel.millisToDate(medicamento.fechaInicio)

        cardView.findViewById<TextView>(R.id.date_end).text =
            pillboxViewModel.millisToDate(medicamento.fechaFin)

        cardView.findViewById<ImageButton>(R.id.summary_btn)
            .setOnClickListener { openPDF(medicamento.fichaTecnica) }

        cardView.findViewById<ImageButton>(R.id.leaflet_btn)
            .setOnClickListener { openPDF(medicamento.prospecto) }

        val favBtn = cardView.findViewById<ImageButton>(R.id.btnFav)
        if (medicamento.isFavorite) {
            favBtn.setImageResource(android.R.drawable.star_big_on)
        }

        // TODO: Cambiar icono solo si se completa eliminacion o insercion
        favBtn.setOnClickListener {
            if (medicamento.isFavorite) {
                pillboxViewModel.deleteFavMed(medicamento)
                favBtn.setImageResource(android.R.drawable.star_big_off)
            } else {
                pillboxViewModel.addFavMed(medicamento)
                favBtn.setImageResource(android.R.drawable.star_big_on)
            }

            medicamento.isFavorite = !medicamento.isFavorite
        }

        val removeBtn = cardView.findViewById<ImageButton>(R.id.btnRemove)
        removeBtn.setOnClickListener {

            if (pillboxViewModel.deleteActiveMed(medicamento)) {

                binding.activeMedLayout.removeView(cardView)
                val snackBar =
                    Snackbar.make(requireView(), getString(R.string.deleteOk), Snackbar.LENGTH_LONG)

                snackBar.setAction(getString(R.string.undo)) {
                    if (pillboxViewModel.addActiveMed(medicamento)) {
                        Toast.makeText(activity, getString(R.string.reinsertOK), Toast.LENGTH_LONG)
                            .show()
                        binding.activeMedLayout.addView(cardView)
                    } else {
                        Toast.makeText(
                            activity,
                            getString(R.string.reinsertFail),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                snackBar.show()
            } else {
                Toast.makeText(activity, getString(R.string.deleteFail), Toast.LENGTH_LONG).show()
            }
        }

        val scheduleLayout = cardView.findViewById<LinearLayout>(R.id.schedule_layout)

        medicamento.horario.forEach {
            val textView = TextView(requireContext())
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textView.layoutParams = layoutParams

            textView.text = pillboxViewModel.millisToHour(it)

            scheduleLayout.addView(textView)
        }

        binding.activeMedLayout.addView(cardView)
    }

    private fun openPDF(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        requireContext().startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_import_export, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}