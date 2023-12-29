package com.example.uf1_proyecto

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.uf1_proyecto.databinding.DiaryEditorBinding
import com.example.uf1_proyecto.databinding.DiaryRendererBinding
import com.example.uf1_proyecto.databinding.FragmentDiaryBinding
import java.util.Calendar

class DiaryFragment : Fragment() {
    private var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!

    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        _pillboxViewModel = PillboxViewModel.getInstance(requireContext())
        val view = binding.root

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        setHasOptionsMenu(true)

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

        binding.toolbar.setOnMenuItemClickListener { menuItem -> menuItemSelected(menuItem) }

        binding.navView.setupWithNavController(navController)

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            menuItemSelected(menuItem)
        }

        // Muestra la entrada de la agenda del día anterior
        binding.fabPrev.setOnClickListener {
            pillboxViewModel.diaryPrevDay()
            changeToRenderer()
        }

        // Muestra la entrada de la agenda del día siguiente
        binding.fabNext.setOnClickListener {
            pillboxViewModel.diaryNextDay()
            changeToRenderer()
        }

        // Por defecto, muestra el renderer de la entrada de la agenda del día actual
        initRenderer()

        return view
    }

    /**
     * Inicializa la vista de renderer de la agenda
     */
    private fun initRenderer() {
        // Infla el layout del renderer
        val rendererBinding = DiaryRendererBinding.inflate(layoutInflater, binding.diaryLayout, true)

        // Establece la fecha actual
        @Suppress("SetTextI18n")
        rendererBinding.diaryDate.text =
            "${DateTimeUtils.getTodayAsDayOfWeek(requireContext())} - ${
                DateTimeUtils.millisToDate(
                    pillboxViewModel.getDiaryCurrDate().first
                )
            }"

        // Establece el texto de la entrada de la agenda
        rendererBinding.diaryText.text = pillboxViewModel.getDiaryCurrDate().second

        // Botón para cambiar al editor
        rendererBinding.diaryEditButton.setOnClickListener {
            changeToEditor()
        }
    }

    /**
     * Inicializa la vista de editor de la agenda
     */
    private fun initEditor() {
        // Infla el layout del editor
        val editorBinding = DiaryEditorBinding.inflate(layoutInflater, binding.diaryLayout, true)

        // Establece la fecha actual
        @Suppress("SetTextI18n")
        editorBinding.diaryDate.text =
            "${DateTimeUtils.getTodayAsDayOfWeek(requireContext())} - ${
                DateTimeUtils.millisToDate(
                    pillboxViewModel.getDiaryCurrDate().first
                )
            }"

        // Establece el texto de la entrada de la agenda
        editorBinding.diaryText.setText(pillboxViewModel.getDiaryCurrDate().second)

        // Botón para cambiar al renderer
        editorBinding.diaryDeleteButton.setOnClickListener {
            changeToRenderer()
        }

        // Botón para guardar la entrada de la agenda
        editorBinding.diarySaveButton.setOnClickListener {
            val text = editorBinding.diaryText.text.toString()
            if (text != pillboxViewModel.getDiaryCurrDate().second) {
                if (pillboxViewModel.insertIntoAgenda(text)) {
                    changeToRenderer()
                    Toast.makeText(
                        requireContext(), getString(R.string.guardar_agenda_ok), Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(), getString(R.string.guardar_agenda_error), Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    /**
     * Cambia la vista de la agenda a la de renderer
     */
    private fun changeToEditor() {
        binding.diaryLayout.removeAllViews()
        initEditor()
    }

    /**
     * Cambia la vista de la agenda a la de editor
     */
    private fun changeToRenderer() {
        binding.diaryLayout.removeAllViews()
        initRenderer()
    }

    private fun search() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                pillboxViewModel.setDiaryCurrDate(
                    DateTimeUtils.createDate(
                        year, monthOfYear, dayOfMonth
                    )
                )
                changeToRenderer()
            },
            // Establece la fecha actual como predeterminada
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun today() {
        pillboxViewModel.setDiaryCurrDate(DateTimeUtils.getTodayAsMillis())
        changeToRenderer()
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
                        } else if (addActive) {
                            Toast.makeText(
                                context,
                                getString(R.string.añadir_fav_error),
                                Toast.LENGTH_LONG
                            ).show()
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
        inflater.inflate(R.menu.menu_toolbar_diary, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun menuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.search -> {
                search()
                true
            }

            R.id.today -> {
                today()
                true
            }

            R.id.addActiveMed -> {
                addMedDialog()
                true
            }

            R.id.historyFragment -> {
                NavHostFragment.findNavController(this)
                    .navigate(R.id.action_diaryFragment_to_historyFragment)
                true
            }

            else -> false
        }
    }

}