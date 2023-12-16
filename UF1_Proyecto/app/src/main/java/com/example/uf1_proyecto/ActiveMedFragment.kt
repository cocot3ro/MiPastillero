package com.example.uf1_proyecto

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.uf1_proyecto.databinding.ActiveMedCardLayoutBinding
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

        cargarActivos()

        return view
    }

    /**
     * Recupera de la base de datos los medicamento activos.
     * Si no hay medicamentos activos, muestra un texto indicándolo.
     */
    private fun cargarActivos() {
        binding.activeMedLayout.removeAllViews()
        val activos = pillboxViewModel.getActivos()

        if (activos.isNotEmpty()) {
            activos.forEach { addCardView(it) }
        } else {
//            NoActiveMedsLayoutBinding.inflate(layoutInflater, binding.activeMedLayout, true)
            binding.activeMedLayout.addView(TextView(requireContext()).apply {
                text = getString(R.string.no_active_meds)
            })
        }
    }

    /**
     * Añade un medicamento activo a la vista.
     * @param medicamento medicamento activo a añadir
     */
    private fun addCardView(medicamento: Medicamento) {
        // Infla el layout del cardview
        val cardViewBinding =
            ActiveMedCardLayoutBinding.inflate(layoutInflater, binding.activeMedLayout, true)

        // Le pone el nombre
        cardViewBinding.name.text = medicamento.nombre

        // Fecha de inicio
        cardViewBinding.dateStart.text =
            DateTimeUtils.millisToDate(medicamento.fechaInicio!!)

        // Fecha de fin
        cardViewBinding.dateEnd.text =
            DateTimeUtils.millisToDate(medicamento.fechaFin!!)

        // Botón para abrir URL de la ficha técnica
        cardViewBinding.summaryBtn.setOnClickListener {
            if (medicamento.codNacional != -1) {
                if (!pillboxViewModel.openURL(requireContext(), medicamento.fichaTecnica)) {
                    Toast.makeText(activity, getString(R.string.openURLFail), Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(activity, getString(R.string.noURL), Toast.LENGTH_LONG)
                    .show()
            }
        }

        // Botón para abrir URL del prospecto
        cardViewBinding.leafletBtn.setOnClickListener {
            if (medicamento.codNacional != -1) {
                if (!pillboxViewModel.openURL(requireContext(), medicamento.prospecto)) {
                    Toast.makeText(activity, getString(R.string.openURLFail), Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(activity, getString(R.string.noURL), Toast.LENGTH_LONG)
                    .show()
            }
        }

        // Cambia el icono de favorito si el medicamento es favorito
        val favBtn = cardViewBinding.favBtn
        if (medicamento.isFavorite!!) {
            favBtn.setImageResource(android.R.drawable.star_big_on)
        }

        // Añade o elimina el medicamento de favoritos y cambia el icono en consecuencia
        favBtn.setOnClickListener {
            if (medicamento.isFavorite!!) {
                if (pillboxViewModel.deleteFavMed(medicamento)) {
                    favBtn.setImageResource(android.R.drawable.star_big_off)
                    Toast.makeText(activity, getString(R.string.removeFavOk), Toast.LENGTH_SHORT)
                        .show()
                    cargarActivos()
                } else {
                    Toast.makeText(activity, getString(R.string.removeFavFail), Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                if (pillboxViewModel.addFavMed(medicamento)) {
                    favBtn.setImageResource(android.R.drawable.star_big_on)
                    Toast.makeText(activity, getString(R.string.addFavOk), Toast.LENGTH_SHORT)
                        .show()
                    cargarActivos()
                } else {
                    Toast.makeText(activity, getString(R.string.addFavFail), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            medicamento.isFavorite = !medicamento.isFavorite!!
        }

        // Botón para eliminar el medicamento activo
        cardViewBinding.removeBtn.setOnClickListener {
            if (pillboxViewModel.deleteActiveMed(medicamento)) {
                val index = binding.activeMedLayout.indexOfChild(cardViewBinding.root)
                binding.activeMedLayout.removeView(cardViewBinding.root)
                val snackBar =
                    Snackbar.make(requireView(), getString(R.string.deleteOk), Snackbar.LENGTH_LONG)

                snackBar.setAction(getString(R.string.undo)) {
                    if (pillboxViewModel.addActiveMed(medicamento)) {
                        Toast.makeText(activity, getString(R.string.reinsertOK), Toast.LENGTH_LONG)
                            .show()
                        binding.activeMedLayout.addView(cardViewBinding.root, index)
                    } else {
                        Toast.makeText(
                            activity,
                            getString(R.string.reinsertFail),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                snackBar.show()
                cargarActivos()
            } else {
                Toast.makeText(activity, getString(R.string.deleteFail), Toast.LENGTH_LONG).show()
            }
        }

        // Añade los horarios del medicamento
        medicamento.horario!!.forEach {
            val textView = TextView(requireContext())
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textView.layoutParams = layoutParams
            textView.gravity = Gravity.END

            textView.text = DateTimeUtils.millisToTime(it)

            cardViewBinding.scheduleLayout.addView(textView)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_active_med, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Dialogo para añadir un nuevo medicamento
            R.id.addActiveMed -> {
                AddActiveMedDialog(
                    requireContext(),
                    object : AddActiveMedDialog.OnDataEnteredListener {
                        override fun onDataEntered(medicamento: Medicamento) {
                            if (!medicamento.isFavorite!!) {
                                if (pillboxViewModel.addActiveMed(medicamento)) {
                                    Toast.makeText(
                                        context,
                                        getString(R.string.addActiveOk),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    cargarActivos()
                                } else {
                                    Toast.makeText(
                                        context,
                                        getString(R.string.addActiveFail),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                // Si se elige guardar tambien como favorito, se añade a la lista de favoritos
                                val addActive = pillboxViewModel.addActiveMed(medicamento)
                                val addFav = pillboxViewModel.addFavMed(medicamento)

                                if (addActive && addFav) {
                                    Toast.makeText(
                                        context,
                                        getString(R.string.addActiveOk),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    cargarActivos()
                                } else if (addActive) {
                                    Toast.makeText(
                                        context,
                                        getString(R.string.addFavFail),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    cargarActivos()
                                } else {
                                    Toast.makeText(
                                        context,
                                        getString(R.string.addActiveFail),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
