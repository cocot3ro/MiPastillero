package com.a23pablooc.proxectofct.ui.view.viewholders

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatDate
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatTime
import com.a23pablooc.proxectofct.databinding.CalendarMedBinding
import com.a23pablooc.proxectofct.databinding.CalendarMedGroupBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.bumptech.glide.Glide
import java.util.Date

class CalendarPageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = CalendarMedGroupBinding.bind(view)

    private var i = 0

    fun render(
        dia: Date,
        hora: Date,
        meds: List<MedicamentoActivoItem>,
        onClick: (MedicamentoActivoItem, Date, Date) -> Unit
    ) {
        val str = "${hora.formatTime()} $i"
        binding.hour.text = str
        i++

        Log.v(
            "CalendarPageViewHolder",
            "rendering dia: ${dia.formatDate()}, hora: ${hora.formatTime()} -> ${meds.size} meds"
        )

        Log.v("CalendarPageViewHolder", "${meds.size} meds: $meds")

        for (med in meds) {
            Log.v(
                "CalendarPageViewHolder",
                "Rendering dia: ${dia.formatDate()}, hora: ${hora.formatTime()}, med: $med"
            )

            CalendarMedBinding.inflate(
                LayoutInflater.from(itemView.context), binding.calendarMedsLayout, true
            ).apply {
                medName.text = med.fkMedicamento.nombre

                btnTake.apply {
                    isChecked = med.tomas[dia]?.get(hora) ?: false
                    setOnClickListener {
                        onClick(med, dia, hora)
                    }
                }

                if (med.fkMedicamento.imagen.toString().isNotBlank()) {
                    // Espera 1 milisegundo para cargar la imagen para que primero se muestre la imagen por defecto
                    // y luego la imagen que se carga
                    // Esto se hace para evitar 'misteriosos' problemas de carga de la imagen
                    // porque se redimensiona a un tamaño no apropiado
                    Handler(Looper.getMainLooper()).postDelayed({
                        Glide.with(root.context)
                            .load(med.fkMedicamento.imagen)
                            .into(medImg)
                    }, 1)
                }
            }
        }
    }
}