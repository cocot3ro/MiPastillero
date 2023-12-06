package com.example.uf1_proyecto

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import androidx.navigation.ui.AppBarConfiguration
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

        // TODO: Borrar esto
        binding.fabAdd2.setOnClickListener {
            pillboxViewModel.ejemplosActivos()
        }

        cargarActivos()

        binding.fabAdd.setOnClickListener {
            AddMedDialog(requireContext(), object : AddMedDialog.OnDataEnteredListener {
                // TODO: Cambiar de Medicamento? a Medicamento
                override fun onDataEntered(medicamento: Medicamento?) {
                    if (medicamento == null) {
                        Log.i("DataInputDialog", "Datos ingresados: null")
                        return
                    }
                    Log.i("DataInputDialog","Datos ingresados: codNacional = ${medicamento.codNacional}, nombre = ${medicamento.nombre}, fechaInicio = ${medicamento.fechaInicio}, fechaFin = ${medicamento.fechaFin}, horario = ${medicamento.horario}, fichaTecnica = ${medicamento.fichaTecnica}, prospecto = ${medicamento.prospecto}")
                }

            }).show()
        }

        return view
    }

    private fun cargarActivos() {
        val activos = pillboxViewModel.getActivos()

        if (activos.isNotEmpty()) {
            binding.activeMedLayout.removeView(binding.noActiveMedsText)
            activos.forEach { addCardView(it) }
        }
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
            pillboxViewModel.millisToDate(medicamento.fechaInicio!!)

        cardView.findViewById<TextView>(R.id.date_end).text =
            pillboxViewModel.millisToDate(medicamento.fechaFin!!)

        cardView.findViewById<ImageButton>(R.id.summary_btn).setOnClickListener {
            if (medicamento.codNacional != -1) {
                if (!pillboxViewModel.openPDF(requireContext(), medicamento.fichaTecnica)) {
                    Toast.makeText(activity, getString(R.string.openPDFFail), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        cardView.findViewById<ImageButton>(R.id.leaflet_btn).setOnClickListener {
            if (medicamento.codNacional != -1) {
                if (!pillboxViewModel.openPDF(requireContext(), medicamento.prospecto)) {
                    Toast.makeText(activity, getString(R.string.openPDFFail), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        val favBtn = cardView.findViewById<ImageButton>(R.id.fav_btn)
        if (medicamento.isFavorite!!) {
            favBtn.setImageResource(android.R.drawable.star_big_on)
        }

        favBtn.setOnClickListener {
            if (medicamento.isFavorite!!) {
                if (pillboxViewModel.deleteFavMed(medicamento)) {
                    favBtn.setImageResource(android.R.drawable.star_big_off)
                    Toast.makeText(activity, getString(R.string.removeFavOk), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(activity, getString(R.string.removeFavFail), Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                if (pillboxViewModel.addFavMed(medicamento)) {
                    favBtn.setImageResource(android.R.drawable.star_big_on)
                    Toast.makeText(activity, getString(R.string.addFavOk), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(activity, getString(R.string.addFavFail), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            medicamento.isFavorite = !medicamento.isFavorite!!
        }

        val removeBtn = cardView.findViewById<ImageButton>(R.id.remove_btn)
        removeBtn.setOnClickListener {
            if (pillboxViewModel.deleteActiveMed(medicamento)) {
                val index = binding.activeMedLayout.indexOfChild(cardView)
                binding.activeMedLayout.removeView(cardView)
                val snackBar =
                    Snackbar.make(requireView(), getString(R.string.deleteOk), Snackbar.LENGTH_LONG)

                snackBar.setAction(getString(R.string.undo)) {
                    if (pillboxViewModel.addActiveMed(medicamento)) {
                        Toast.makeText(activity, getString(R.string.reinsertOK), Toast.LENGTH_LONG)
                            .show()
                        binding.activeMedLayout.addView(cardView, index)
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

        medicamento.horario!!.forEach {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_import_export, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.importar -> {
                // TODO: importar de json
                Toast.makeText(activity, "IMPORT", Toast.LENGTH_LONG).show()
                true
            }

            // TODO: exportar a json
            R.id.exportar -> {
                Toast.makeText(activity, "EXPORT", Toast.LENGTH_LONG).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
