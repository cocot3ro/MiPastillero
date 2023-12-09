package com.example.uf1_proyecto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
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
            ), binding.drawerLayout
        )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        // Recupera los medicamentos favoritos de la base de datos y los añade a la vista
        val favoritos = pillboxViewModel.getFavoritos()
        if (favoritos.isNotEmpty()) {
            // Si hay medicamentos favoritos, elimina el texto de "no hay medicamentos favoritos"
            binding.favoriteLayout.removeView(binding.noFavMedsText)
            favoritos.forEach { addCardView(it) }
        }

        return view
    }

    /**
     * Añade un medicamento a la vista
     * @param medicamento medicamento a añadir
     */
    private fun addCardView(medicamento: Medicamento) {
        // Infla el layout de la tarjeta
        val cardViewBinding = FavoriteCardLayoutBinding.inflate(layoutInflater)

        // Nombre del medicamento
        cardViewBinding.name.text = medicamento.nombre

        // Botón para abrir PDF de la ficha técnica
        cardViewBinding.summaryBtn.setOnClickListener {
            if (medicamento.codNacional != -1) {
                if (!pillboxViewModel.openPDF(requireContext(), medicamento.fichaTecnica)) {
                    Toast.makeText(activity, getString(R.string.openPDFFail), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        // Botón para abrir PDF del prospecto
        cardViewBinding.leafletBtn.setOnClickListener {
            if (medicamento.codNacional != -1) {
                if (!pillboxViewModel.openPDF(requireContext(), medicamento.prospecto)) {
                    Toast.makeText(activity, getString(R.string.openPDFFail), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        // Botón para añadir el medicamento a la lista de medicamentos activos
        cardViewBinding.fillBtn.setOnClickListener {
            // TODO: add fav med dialog
        }

        // Botón para eliminar el medicamento de la lista de medicamentos favoritos
        cardViewBinding.removeBtn.setOnClickListener {
            if (pillboxViewModel.deleteFavMed(medicamento)) {
                val index = binding.favoriteLayout.indexOfChild(cardViewBinding.root)
                binding.favoriteLayout.removeView(cardViewBinding.root)
                val snackBar = Snackbar.make(
                    requireView(), getString(R.string.removeFavOk), Snackbar.LENGTH_LONG
                )

                snackBar.setAction(getString(R.string.undo)) {
                    if (pillboxViewModel.addFavMed(medicamento)) {
                        Toast.makeText(activity, getString(R.string.reinsertOK), Toast.LENGTH_LONG)
                            .show()
                        binding.favoriteLayout.addView(cardViewBinding.root, index)
                    } else {
                        Toast.makeText(
                            activity, getString(R.string.reinsertFail), Toast.LENGTH_LONG
                        ).show()
                    }
                }

                snackBar.show()
            } else {
                Toast.makeText(activity, getString(R.string.removeFavFail), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // Añade la tarjeta a la vista
        binding.favoriteLayout.addView(cardViewBinding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // TODO: cambiar menu
        inflater.inflate(R.menu.menu_toolbar_active_med, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
