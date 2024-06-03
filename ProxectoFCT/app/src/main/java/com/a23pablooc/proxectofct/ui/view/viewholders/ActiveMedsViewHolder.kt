package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatDate
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatTime
import com.a23pablooc.proxectofct.databinding.ActiveMedBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.bumptech.glide.Glide

class ActiveMedsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ActiveMedBinding.bind(view)

    fun render(
        med: MedicamentoActivoItem,
        onFav: (MedicamentoActivoItem) -> Unit,
        onInfo: (MedicamentoActivoItem) -> Unit,
        onAdd: (MedicamentoActivoItem) -> Unit
    ) {
        if (med.fkMedicamento.imagen.isNotBlank()) {
            Glide.with(binding.root)
                .load(med.fkMedicamento.imagen)
                .override(400, 400)
                .into(binding.medImg)
        }

        binding.medName.text = med.fkMedicamento.nombre

        binding.btnFavBg.visibility = if (med.fkMedicamento.esFavorito) View.VISIBLE else View.GONE

        binding.btnFav.setOnClickListener { onFav(med) }
        binding.btnAdd.setOnClickListener { onAdd(med) }
        binding.btnInfo.setOnClickListener { onInfo(med) }

        binding.dateStart.text = med.fechaInicio.formatDate()
        binding.dateEnd.text = med.fechaFin.formatDate()

        for (hora in med.horario) {
            binding.scheduleLayout.addView(
                TextView(binding.root.context).apply {
                    text = hora.formatTime()
                    gravity = Gravity.END
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            )
        }
    }
}