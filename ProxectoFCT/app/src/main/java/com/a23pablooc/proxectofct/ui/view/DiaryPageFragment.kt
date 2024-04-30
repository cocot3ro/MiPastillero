package com.a23pablooc.proxectofct.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.DiaryEditorBinding
import com.a23pablooc.proxectofct.databinding.DiaryRendererBinding
import com.a23pablooc.proxectofct.databinding.FragmentDiaryPageBinding
import com.a23pablooc.proxectofct.utils.DateTimeUtils
import com.a23pablooc.proxectofct.viewModel.PillboxViewModel

class DiaryPageFragment(private var fecha: Long) : Fragment(), PageFragment {

    private var _binding: FragmentDiaryPageBinding? = null
    private val binding get() = _binding!!
    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!
    private var onDestroyListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiaryPageBinding.inflate(inflater, container, false)
        _pillboxViewModel = PillboxViewModel.getInstance(requireContext())

        initRenderer()

        return binding.root
    }

    private fun initRenderer() {
        val data = pillboxViewModel.getDiaryText(fecha)

        // Infla el layout del renderer
        DiaryRendererBinding.inflate(layoutInflater, binding.diaryLayout, true).apply {

            // Establece la fecha actual
            @Suppress("SetTextI18n")
            diaryDate.text =
                (if (fecha == DateTimeUtils.getTodayAsMillis()) getString(R.string.hoy) + " - " else "") +
                        "${DateTimeUtils.millisToDayOfWeek(fecha, requireContext())}, ${
                            DateTimeUtils.millisToDate(
                                fecha
                            )
                        }"

            // Establece el texto de la entrada de la agenda
            diaryText.text = data

            // Botón para cambiar al editor
            diaryEditButton.setOnClickListener {
                changeToEditor()
            }
        }
    }

    private fun initEditor() {
        val data = pillboxViewModel.getDiaryText(fecha)

        // Infla el layout del editor
        DiaryEditorBinding.inflate(layoutInflater, binding.diaryLayout, true).apply {
            // Establece la fecha actual
            @Suppress("SetTextI18n")
            diaryDate.text =
                (if (fecha == DateTimeUtils.getTodayAsMillis()) getString(R.string.hoy) + " - " else "") +
                        "${DateTimeUtils.millisToDayOfWeek(fecha, requireContext())}, ${
                            DateTimeUtils.millisToDate(
                                fecha
                            )
                        }"

            // Establece el texto de la entrada de la agenda
            diaryText.setText(data)

            // Botón para cambiar al renderer
            diaryDeleteButton.setOnClickListener {
                changeToRenderer()
            }

            // Botón para guardar la entrada de la agenda
            diarySaveButton.setOnClickListener {
                val text = diaryText.text.toString()
                if (text != data) {
                    if (pillboxViewModel.insertIntoAgenda(fecha, text)) {
                        changeToRenderer()
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.guardar_agenda_ok),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        changeToRenderer()
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.guardar_agenda_error),
                            Toast.LENGTH_LONG
                        ).show()
                    }
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
    fun changeToRenderer() {
        binding.diaryLayout.removeAllViews()
        initRenderer()
    }

    override fun setOnDestroyListener(listener: () -> Unit) {
        onDestroyListener = listener
    }

}
