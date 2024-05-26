package com.a23pablooc.proxectofct.ui.view.viewholders

import android.text.SpannableString
import android.text.style.LeadingMarginSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.core.DateTimeUtils
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
        if (med.medicamento.imagen.isNotEmpty()) {
            Glide.with(binding.root)
                .load(med.medicamento.imagen)
                .into(binding.medImg)
        }

        //TODO: Ajustar indentado
        binding.medName.text = SpannableString(med.medicamento.nombre).apply {
            setSpan(LeadingMarginSpan.Standard(30, 0), 0, 1, 0)
        }

        binding.btnFavBg.visibility = if (med.medicamento.esFavorito) View.VISIBLE else View.GONE

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