package com.a23pablooc.proxectofct.ui.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.CalendarMedGroupLayoutBinding
import com.a23pablooc.proxectofct.databinding.CalendarMedLayoutBinding
import com.a23pablooc.proxectofct.databinding.EmptyLayoutBinding
import com.a23pablooc.proxectofct.databinding.FragmentCalendarPageBinding
import com.a23pablooc.proxectofct.utils.DateTimeUtils
import com.a23pablooc.proxectofct.viewModel.PillboxViewModel

class CalendarPageFragment(val fecha: Long) :
    Fragment(), PageFragment {

    private var _binding: FragmentCalendarPageBinding? = null
    private val binding get() = _binding!!
    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    private var onDestroyListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarPageBinding.inflate(layoutInflater)
        _pillboxViewModel = PillboxViewModel.getInstance(requireContext())

        updateView()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyListener?.invoke()
    }

    fun updateView() {
        if (_binding == null || _pillboxViewModel == null) {
            return
        }

        val data = pillboxViewModel.getActivosCalendario(fecha)

        @Suppress("SetTextI18n")
        binding.calendarDay.text =
            (if (fecha == DateTimeUtils.getTodayAsMillis()) getString(R.string.hoy) + " - " else "") +
                    "${
                        DateTimeUtils.millisToDayOfWeek(
                            fecha,
                            requireContext()
                        )
                    }, ${DateTimeUtils.millisToDate(fecha)}"

        binding.calendarLayout.removeAllViews()

        if (data.isEmpty()) {
            EmptyLayoutBinding.inflate(layoutInflater, binding.calendarLayout, true).apply {
                texto.text = getString(R.string.sin_meds_en_dia)
            }
        } else {
            for (entry in data) {
                val groupBinding =
                    CalendarMedGroupLayoutBinding.inflate(
                        layoutInflater, binding.calendarLayout,
                        true
                    )

                val colores =
                    DateTimeUtils.getEstacionColor(fecha)

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
                                    medicamento.nombre!!,
                                    entry.key,
                                    fecha
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
                                    medicamento.nombre!!,
                                    entry.key,
                                    fecha
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

    override fun setOnDestroyListener(listener: () -> Unit) {
        onDestroyListener = listener
    }

}