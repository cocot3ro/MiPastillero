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
        Glide.with(binding.root)
            .load(med.medicamento.imagen)
            .into(binding.medImg)

        SpannableString(med.medicamento.nombre).apply {
            setSpan(LeadingMarginSpan.Standard(30, 0), 0, 1, 0)
        }.also {
            binding.medName.text = it
        }

        binding.btnFavBg.visibility = if (med.medicamento.esFavorito) View.VISIBLE else View.GONE

        binding.btnFav.setOnClickListener { onFav(med) }
        binding.btnAdd.setOnClickListener { onAdd(med) }
        binding.btnInfo.setOnClickListener { onInfo(med) }

        binding.dateStart.text = DateTimeUtils.formatDate(med.fechaInicio)
        binding.dateEnd.text = DateTimeUtils.formatDate(med.fechaFin)

        for (hora in med.horario) {
            binding.scheduleLayout.addView(
                TextView(binding.root.context).apply {
                    text = DateTimeUtils.formatTime(hora)
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