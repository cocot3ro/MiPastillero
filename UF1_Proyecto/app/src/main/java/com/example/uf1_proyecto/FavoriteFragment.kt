package com.example.uf1_proyecto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.uf1_proyecto.databinding.FragmentFavoriteBinding
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
            ),
            binding.drawerLayout
        )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        val favoritos = pillboxViewModel.getFavoritos()

        if (favoritos.isNotEmpty()) {
            binding.favoriteLayout.removeView(binding.noFavMedsText)
            favoritos.forEach { addCardView(it) }
        }

        return view
    }

    private fun addCardView(medicamento: Medicamento) {
        val inflater = LayoutInflater.from(requireContext())

        val cardView =
            inflater.inflate(
                R.layout.favorite_card_layout,
                binding.favoriteLayout,
                false
            ) as CardView

        cardView.findViewById<TextView>(R.id.name).text = medicamento.nombre

        cardView.findViewById<ImageButton>(R.id.summary_btn)
            .setOnClickListener {
                if (medicamento.codNacional != -1) {
                    pillboxViewModel.openPDF(
                        requireContext(),
                        medicamento.fichaTecnica
                    )
                }
            }

        cardView.findViewById<ImageButton>(R.id.leaflet_btn)
            .setOnClickListener {
                if (medicamento.codNacional != -1) {
                    pillboxViewModel.openPDF(
                        requireContext(),
                        medicamento.prospecto
                    )
                }
            }

        val fillBtn = cardView.findViewById<ImageButton>(R.id.fill_btn)
        fillBtn.setOnClickListener {
            // TODO: add med fragment
        }

        val removeBtn = cardView.findViewById<ImageButton>(R.id.remove_btn)
        removeBtn.setOnClickListener {
            if (pillboxViewModel.deleteFavMed(medicamento)) {
                val index = binding.favoriteLayout.indexOfChild(cardView)
                binding.favoriteLayout.removeView(cardView)
                val snackBar = Snackbar.make(requireView(), getString(R.string.removeFavOk), Snackbar.LENGTH_LONG)

                snackBar.setAction(getString(R.string.undo)) {
                    if (pillboxViewModel.addFavMed(medicamento)) {
                        Toast.makeText(activity, getString(R.string.reinsertOK), Toast.LENGTH_LONG)
                            .show()
                        binding.favoriteLayout.addView(cardView, index)
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
                Toast.makeText(activity, getString(R.string.removeFavFail), Toast.LENGTH_SHORT).show()
            }
        }



        binding.favoriteLayout.addView(cardView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_import_export, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
