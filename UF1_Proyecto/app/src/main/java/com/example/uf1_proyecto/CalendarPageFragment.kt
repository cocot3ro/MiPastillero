package com.example.uf1_proyecto

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.uf1_proyecto.databinding.CalendarMedGroupLayoutBinding
import com.example.uf1_proyecto.databinding.CalendarMedLayoutBinding
import com.example.uf1_proyecto.databinding.EmptyLayoutBinding
import com.example.uf1_proyecto.databinding.FragmentCalendarPageBinding

class CalendarPageFragment(private val pair: Pair<Long, Map<Long, List<Medicamento>>>) :
    Fragment() {

    private var _binding: FragmentCalendarPageBinding? = null
    private val binding get() = _binding!!
    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarPageBinding.inflate(inflater, container, false)
        _pillboxViewModel = PillboxViewModel.getInstance(requireContext())

        updateView(pair)

        return binding.root
    }

    private fun updateView(pair: Pair<Long, Map<Long, List<Medicamento>>>) {
        @Suppress("SetTextI18n")
        binding.calendarDay.text = "${
            DateTimeUtils.millisToDayOfWeek(
                pair.first,
                requireContext()
            )
        } - ${DateTimeUtils.millisToDate(pair.first)}"

        binding.calendarLayout.removeAllViews()

        val map = pair.second

        if (map.isEmpty()) {
            EmptyLayoutBinding.inflate(layoutInflater, binding.calendarLayout, true).apply {
                texto.text = getString(R.string.sin_meds_en_dia)
            }
        } else {
            for (entry in map) {
                val groupBinding =
                    CalendarMedGroupLayoutBinding.inflate(
                        layoutInflater, binding.calendarLayout,
                        true
                    )

                val colores =
                    pillboxViewModel.getEstacionColor(pair.first)

                groupBinding.cuerpo.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        colores.second
                    )
                )

                groupBinding.hora.text = DateTimeUtils.millisToTime(entry.key)
                groupBinding.hora.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        colores.first
                    )
                )

                for (medicamento in entry.value) {
                    val calendarEntryBinding = CalendarMedLayoutBinding.inflate(
                        layoutInflater, groupBinding.calendarMedsLayout, true
                    )

                    calendarEntryBinding.nombre.text = medicamento.nombre

                    if (medicamento.imagen != null && medicamento.imagen.isNotEmpty()) {
                        val bitmap = BitmapFactory.decodeByteArray(
                            medicamento.imagen,
                            0,
                            medicamento.imagen.size
                        )
                        calendarEntryBinding.img.setImageBitmap(bitmap)
                    } else {
                        calendarEntryBinding.img.setImageResource(R.mipmap.no_image_available)
                    }

                    if (medicamento.seHaTomado!!) {
                        calendarEntryBinding.btnToma.setImageResource(android.R.drawable.checkbox_on_background)
                    }

                    calendarEntryBinding.btnToma.setOnClickListener {
                        if (medicamento.seHaTomado!!) {
                            if (pillboxViewModel.desmarcarToma(
                                    medicamento,
                                    entry.key,
                                    pair.first
                                )
                            ) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.desmarcar_ok),
                                    Toast.LENGTH_LONG
                                ).show()
                                medicamento.seHaTomado = false
                                calendarEntryBinding.btnToma.setImageResource(android.R.drawable.checkbox_off_background)
                            } else {
                                Toast.makeText(
                                    context,
                                    getString(R.string.desmarcar_error), Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            if (pillboxViewModel.marcarToma(
                                    medicamento,
                                    entry.key,
                                    pair.first
                                )
                            ) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.marcar_ok), Toast.LENGTH_LONG
                                ).show()
                                medicamento.seHaTomado = true
                                calendarEntryBinding.btnToma.setImageResource(android.R.drawable.checkbox_on_background)
                            } else {
                                Toast.makeText(
                                    context,
                                    getString(R.string.marcar_error), Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }
}