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
        val rendererBinding = DiaryRendererBinding.inflate(layoutInflater)

        // Establece la fecha actual
        @Suppress("SetTextI18n")
        rendererBinding.diaryDate.text =
            "${pillboxViewModel.getTodayAsDayOfWeek(requireContext())} - ${
                pillboxViewModel.millisToDate(
                    pillboxViewModel.getDiaryCurrDate()
                )
            }"

        // Establece el texto de la entrada de la agenda
        rendererBinding.diaryText.text = pillboxViewModel.getDiaryText()

        // Botón para cambiar al editor
        rendererBinding.diaryEditButton.setOnClickListener {
            changeToEditor()
        }

        // Añade el layout del renderer a la vista
        binding.diaryLayout.addView(rendererBinding.root)
    }

    /**
     * Inicializa la vista de editor de la agenda
     */
    private fun initEditor() {
        // Infla el layout del editor
        val editorBinding = DiaryEditorBinding.inflate(layoutInflater)

        // Establece la fecha actual
        @Suppress("SetTextI18n")
        editorBinding.diaryDate.text =
            "${pillboxViewModel.getTodayAsDayOfWeek(requireContext())} - ${
                pillboxViewModel.millisToDate(
                    pillboxViewModel.getDiaryCurrDate()
                )
            }"

        // Establece el texto de la entrada de la agenda
        editorBinding.diaryText.setText(pillboxViewModel.getDiaryText())

        // Botón para cambiar al renderer
        editorBinding.diaryDeleteButton.setOnClickListener {
            changeToRenderer()
        }

        // Botón para guardar la entrada de la agenda
        editorBinding.diarySaveButton.setOnClickListener {
            val text = editorBinding.diaryText.text.toString()
            if (text != pillboxViewModel.getDiaryText()) {
                if (pillboxViewModel.insertIntoAgenda(text)) {
                    changeToRenderer()
                    Toast.makeText(
                        requireContext(), getString(R.string.saveDiaryOk), Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(), getString(R.string.saveDiaryFail), Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // Añade el layout del editor a la vista
        binding.diaryLayout.addView(editorBinding.root)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_diary, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Abre un dialogo para seleccionar una fecha y mostrar la entrada de la agenda de ese día
            R.id.search -> {
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, year, monthOfYear, dayOfMonth ->
                        pillboxViewModel.setDiaryCurrDate(
                            pillboxViewModel.createDate(
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
                true
            }

            // Muestra la entrada de la agenda del día actual
            R.id.today -> {
                pillboxViewModel.setDiaryCurrDate(pillboxViewModel.getTodayAsMillis())
                changeToRenderer()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}