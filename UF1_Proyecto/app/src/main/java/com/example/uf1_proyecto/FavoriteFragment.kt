package com.example.uf1_proyecto

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
import com.example.uf1_proyecto.databinding.FavoriteCardLayoutBinding
import com.example.uf1_proyecto.databinding.FragmentFavoriteBinding
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        _pillboxViewModel = PillboxViewModel.getInstance(requireContext())

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
            ), binding.drawerLayout
        )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        cargarFavoritos()

        // TODO: Funcionalidad a menu de drawer layout

        return binding.root
    }

    /**
     * Carga los medicamentos favoritos en la vista
     * Si no hay medicamentos favoritos, muestra un texto indicándolo
     */
    private fun cargarFavoritos() {
        binding.favoriteLayout.removeAllViews()
        val favoritos = pillboxViewModel.getFavoritos()

        if (favoritos.isNotEmpty()) {
            favoritos.forEach { addCardView(it) }
        } else {
            binding.favoriteLayout.addView(TextView(requireContext()).apply {
                text = getString(R.string.sin_meds_favoritos)
            })
        }
    }

    /**
     * Añade un medicamento a la vista
     * @param medicamento medicamento a añadir
     */
    private fun addCardView(medicamento: Medicamento) {
        // Infla el layout de la tarjeta
        val cardViewBinding =
            FavoriteCardLayoutBinding.inflate(layoutInflater, binding.favoriteLayout, true)

        // Nombre del medicamento
        cardViewBinding.name.text = medicamento.nombre

        // Botón para abrir URL de la ficha técnica
        cardViewBinding.summaryBtn.setOnClickListener {
            if (medicamento.codNacional != null && medicamento.codNacional != -1) {
                if (!pillboxViewModel.openURL(requireContext(), medicamento.fichaTecnica)) {
                    Toast.makeText(activity, getString(R.string.abrir_url_error), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        // Botón para abrir URL del prospecto
        cardViewBinding.leafletBtn.setOnClickListener {
            if (medicamento.codNacional != null && medicamento.codNacional != -1) {
                if (!pillboxViewModel.openURL(requireContext(), medicamento.prospecto)) {
                    Toast.makeText(activity, getString(R.string.abrir_url_error), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        // Botón para añadir el medicamento a la lista de medicamentos activos
        cardViewBinding.fillBtn.setOnClickListener {
            refillMedDialog(medicamento)
        }

        // Botón para eliminar el medicamento de la lista de medicamentos favoritos
        cardViewBinding.removeBtn.setOnClickListener {
            if (pillboxViewModel.deleteFavMed(medicamento)) {
                val index = binding.favoriteLayout.indexOfChild(cardViewBinding.root)
                binding.favoriteLayout.removeView(cardViewBinding.root)
                val snackBar = Snackbar.make(
                    requireView(), getString(R.string.borrar_fav_ok), Snackbar.LENGTH_LONG
                )

                snackBar.setAction(getString(R.string.deshacer)) {
                    if (pillboxViewModel.addFavMed(medicamento)) {
                        Toast.makeText(activity, getString(R.string.reinsertar_ok), Toast.LENGTH_LONG)
                            .show()
                        binding.favoriteLayout.addView(cardViewBinding.root, index)
                    } else {
                        Toast.makeText(
                            activity, getString(R.string.reinsertar_error), Toast.LENGTH_LONG
                        ).show()
                    }
                }

                snackBar.show()
                cargarFavoritos()
            } else {
                Toast.makeText(activity, getString(R.string.borrar_fav_error), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun refillMedDialog(medicamento: Medicamento) {
        RefillMedDialog(requireContext(), medicamento, object : RefillMedDialog.OnDataEnteredListener {
            override fun onDataEntered(medicamento: Medicamento) {
                if (pillboxViewModel.addActiveMed(medicamento)) {
                    Toast.makeText(
                        activity,
                        getString(R.string.añadir_activo_ok),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.añadir_activo_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }).show()
    }

    private fun addFavMedDialog() {
        AddFavMedDialog(requireContext(), object : AddFavMedDialog.OnDataEnteredListener {
            override fun onDataEntered(medicamento: Medicamento) {
                if (pillboxViewModel.addFavMed(medicamento)) {
                    Toast.makeText(
                        activity,
                        getString(R.string.añadir_fav_ok),
                        Toast.LENGTH_LONG
                    ).show()
                    cargarFavoritos()
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.añadir_fav_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_fav_med, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addFavMed -> {
                addFavMedDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
