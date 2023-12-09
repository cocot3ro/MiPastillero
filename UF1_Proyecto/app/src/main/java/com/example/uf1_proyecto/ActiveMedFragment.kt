package com.example.uf1_proyecto

import android.os.Bundle
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

    private fun cargarActivos() {
        val activos = pillboxViewModel.getActivos()

        if (activos.isNotEmpty()) {
            binding.activeMedLayout.removeView(binding.noActiveMedsText)
            activos.forEach { addCardView(it) }
        }
    }

    private fun recargarActivos() {
        binding.activeMedLayout.removeAllViews()
        pillboxViewModel.getActivos().forEach { addCardView(it) }
    }

    private fun addCardView(medicamento: Medicamento) {
//        val inflater = LayoutInflater.from(requireContext())
//
//        val cardView =
//            inflater.inflate(
//                R.layout.active_med_card_layout,
//                binding.activeMedLayout,
//                false
//            ) as CardView

        val cardViewBindinig = ActiveMedCardLayoutBinding.inflate(layoutInflater)

        cardViewBindinig.name.text = medicamento.nombre

        cardViewBindinig.dateStart.text =
            pillboxViewModel.millisToDate(medicamento.fechaInicio!!)

        cardViewBindinig.dateEnd.text =
            pillboxViewModel.millisToDate(medicamento.fechaFin!!)

        cardViewBindinig.summaryBtn.setOnClickListener {
            if (medicamento.codNacional != -1) {
                if (!pillboxViewModel.openPDF(requireContext(), medicamento.fichaTecnica)) {
                    Toast.makeText(activity, getString(R.string.openPDFFail), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        cardViewBindinig.leafletBtn.setOnClickListener {
            if (medicamento.codNacional != -1) {
                if (!pillboxViewModel.openPDF(requireContext(), medicamento.prospecto)) {
                    Toast.makeText(activity, getString(R.string.openPDFFail), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        val favBtn = cardViewBindinig.favBtn
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

        val removeBtn = cardViewBindinig.removeBtn
        removeBtn.setOnClickListener {
            if (pillboxViewModel.deleteActiveMed(medicamento)) {
                val index = binding.activeMedLayout.indexOfChild(cardViewBindinig.root)
                binding.activeMedLayout.removeView(cardViewBindinig.root)
                val snackBar =
                    Snackbar.make(requireView(), getString(R.string.deleteOk), Snackbar.LENGTH_LONG)

                snackBar.setAction(getString(R.string.undo)) {
                    if (pillboxViewModel.addActiveMed(medicamento)) {
                        Toast.makeText(activity, getString(R.string.reinsertOK), Toast.LENGTH_LONG)
                            .show()
                        binding.activeMedLayout.addView(cardViewBindinig.root, index)
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

        medicamento.horario!!.forEach {
            val textView = TextView(requireContext())
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textView.layoutParams = layoutParams

            textView.text = pillboxViewModel.millisToHour(it)

            cardViewBindinig.scheduleLayout.addView(textView)
        }

        binding.activeMedLayout.addView(cardViewBindinig.root)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_active_med, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addActiveMed -> {
                AddMedDialog(requireContext(), object : AddMedDialog.OnDataEnteredListener {
                    override fun onDataEntered(medicamento: Medicamento) {
                        if (!medicamento.isFavorite!!) {
                            if (pillboxViewModel.addActiveMed(medicamento)) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.addActiveOk),
                                    Toast.LENGTH_LONG
                                ).show()
                                recargarActivos()
                            } else {
                                Toast.makeText(
                                    context,
                                    getString(R.string.addActiveFail),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            val addActive = pillboxViewModel.addActiveMed(medicamento)
                            val addFav = pillboxViewModel.addFavMed(medicamento)

                            if (addActive && addFav) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.addActiveOk),
                                    Toast.LENGTH_LONG
                                ).show()
                                recargarActivos()
                            } else if (addActive) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.addFavFail),
                                    Toast.LENGTH_LONG
                                ).show()
                                recargarActivos()
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
