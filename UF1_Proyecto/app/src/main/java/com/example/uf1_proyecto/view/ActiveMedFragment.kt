package com.example.uf1_proyecto.view

import android.graphics.BitmapFactory
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
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.uf1_proyecto.utils.DateTimeUtils
import com.example.uf1_proyecto.model.Medicamento
import com.example.uf1_proyecto.viewModel.PillboxViewModel
import com.example.uf1_proyecto.R
import com.example.uf1_proyecto.databinding.ActiveMedCardLayoutBinding
import com.example.uf1_proyecto.databinding.EmptyLayoutBinding
import com.example.uf1_proyecto.databinding.FragmentActiveMedBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class ActiveMedFragment : Fragment() {
    private var _binding: FragmentActiveMedBinding? = null
    private val binding get() = _binding!!

    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActiveMedBinding.inflate(inflater, container, false)
        _pillboxViewModel = PillboxViewModel.getInstance(requireContext())
        navController = ((activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        val view = binding.root

        setHasOptionsMenu(true)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.calendarFragment,
                R.id.activeMedFragment,
                R.id.favoriteFragment,
                R.id.diaryFragment
            ),
            binding.drawerLayout
        )

        binding.navView.setupWithNavController(navController)

        binding.toolbar.setOnMenuItemClickListener { menuItem -> menuItemSelected(menuItem) }

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            menuItemSelected(menuItem)
        }

        updateView()

        return view
    }

    /**
     * Recupera de la base de datos los medicamento activos.
     * Si no hay medicamentos activos, muestra un texto indicándolo.
     */
    private fun updateView() {
        binding.activeMedLayout.removeAllViews()
        val activos = pillboxViewModel.getActivos()

        if (activos.isNotEmpty()) {
            activos.forEach { addCardView(it) }
        } else {
            EmptyLayoutBinding.inflate(layoutInflater, binding.activeMedLayout, true).apply {
                texto.text = getString(R.string.sin_meds_activos)
            }
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

        cardViewBinding.cuerpo.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                DateTimeUtils.getEstacionColor(medicamento.fechaInicio!!).second
            )
        )

        cardViewBinding.infoBtn.setOnClickListener {
            InfoDialog(requireContext(), medicamento).show()
        }

        // Fecha de inicio
        cardViewBinding.dateStart.text =
            DateTimeUtils.millisToDate(medicamento.fechaInicio)

        // Fecha de fin
        cardViewBinding.dateEnd.text =
            DateTimeUtils.millisToDate(medicamento.fechaFin!!)

        if (medicamento.imagen != null && medicamento.imagen.isNotEmpty()) {
            val bitmap =
                BitmapFactory.decodeByteArray(medicamento.imagen, 0, medicamento.imagen.size)
            cardViewBinding.img.setImageBitmap(bitmap)
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
                    Toast.makeText(activity, getString(R.string.borrar_fav_ok), Toast.LENGTH_SHORT)
                        .show()
                    updateView()
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.borrar_fav_error),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                if (pillboxViewModel.addFavMed(medicamento)) {
                    favBtn.setImageResource(android.R.drawable.star_big_on)
                    Toast.makeText(activity, getString(R.string.añadir_fav_ok), Toast.LENGTH_SHORT)
                        .show()
                    updateView()
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.añadir_fav_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            medicamento.isFavorite = !medicamento.isFavorite!!
        }

        cardViewBinding.refillBtn.setOnClickListener {
            RefillMedDialog(
                requireContext(),
                medicamento,
                object : RefillMedDialog.OnDataEnteredListener {
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

        // Botón para eliminar el medicamento activo
        cardViewBinding.removeBtn.setOnClickListener {
            if (pillboxViewModel.deleteActiveMed(medicamento)) {
                val index = binding.activeMedLayout.indexOfChild(cardViewBinding.root)
                binding.activeMedLayout.removeView(cardViewBinding.root)
                val snackBar =
                    Snackbar.make(
                        requireView(),
                        getString(R.string.borrar_ok),
                        Snackbar.LENGTH_LONG
                    )

                snackBar.setAction(getString(R.string.deshacer)) {
                    if (pillboxViewModel.addActiveMed(medicamento)) {
                        Toast.makeText(
                            activity,
                            getString(R.string.reinsertar_ok),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        binding.activeMedLayout.addView(cardViewBinding.root, index)
                    } else {
                        Toast.makeText(
                            activity,
                            getString(R.string.reinsertar_error),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                snackBar.show()
                updateView()
            } else {
                Toast.makeText(activity, getString(R.string.borrar_error), Toast.LENGTH_LONG).show()
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

    private fun addMedDialog() {
        AddActiveMedDialog(
            requireContext(),
            object : AddActiveMedDialog.OnDataEnteredListener {
                override fun onDataEntered(medicamento: Medicamento) {
                    if (!medicamento.isFavorite!!) {
                        if (pillboxViewModel.addActiveMed(medicamento)) {
                            Toast.makeText(
                                context,
                                getString(R.string.añadir_activo_ok),
                                Toast.LENGTH_LONG
                            ).show()
                            updateView()
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.añadir_activo_error),
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
                                getString(R.string.añadir_activo_ok),
                                Toast.LENGTH_LONG
                            ).show()
                            updateView()
                        } else if (addActive) {
                            Toast.makeText(
                                context,
                                getString(R.string.añadir_fav_error),
                                Toast.LENGTH_LONG
                            ).show()
                            updateView()
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.añadir_activo_error),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_active_med, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun menuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.addActiveMed -> {
                addMedDialog()
                true
            }

            R.id.diaryFragment, R.id.historyFragment, R.id.settingsFragment -> {
                navController.navigate(menuItem.itemId)
                true
            }

            else -> false
        }
    }

}
