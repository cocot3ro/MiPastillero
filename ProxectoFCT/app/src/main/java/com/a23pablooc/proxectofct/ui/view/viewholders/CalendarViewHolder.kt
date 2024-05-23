package com.a23pablooc.proxectofct.ui.view.viewholders

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.CalendarMedBinding
import com.a23pablooc.proxectofct.databinding.CalendarMedGroupBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoCalendarioItem

class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = CalendarMedGroupBinding.bind(view)

    fun render(
        meds: List<MedicamentoCalendarioItem>,
        onClick: (MedicamentoCalendarioItem) -> Unit
    ) {
        // TODO: colores?

        // binding.body.setBackgroundColor()

        binding.hour.text = meds[0].hora.toString()

        for (med in meds) {
            CalendarMedBinding.inflate(LayoutInflater.from(itemView.context), binding.body, true)
                .apply {
                    medImg.setImageBitmap(
                        BitmapFactory.decodeByteArray(
                            med.medicamento.imagen,
                            0,
                            med.medicamento.imagen.size
                        )
                    )

                    medName.text = med.medicamento.nombre

                    if (med.seHaTomado) {
                        btnTake.setImageResource(R.drawable.check_box_on)
                    } else {
                        btnTake.setImageResource(R.drawable.check_box_off)
                    }

                    btnTake.setOnClickListener {
                        onClick(med)
                    }
                }
        }
    }
}