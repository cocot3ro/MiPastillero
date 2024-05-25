package com.a23pablooc.proxectofct.ui.view.viewholders

import android.text.SpannableString
import android.text.style.LeadingMarginSpan
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.databinding.FavoriteMedBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.bumptech.glide.Glide

class FavoriteMedsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = FavoriteMedBinding.bind(view)

    fun render(
        med: MedicamentoItem,
        onAdd: (MedicamentoItem) -> Unit,
        onInfo: (MedicamentoItem) -> Unit
    ) {

        //TODO: Ajustar indentado
        binding.name.text = SpannableString(med.nombre).apply {
            setSpan(LeadingMarginSpan.Standard(30, 0), 0, 1, 0)
        }

        Glide.with(binding.root.context)
            .load(med.imagen)
            .into(binding.img)

        binding.addBtn.setOnClickListener {
            onAdd(med)
        }

        binding.infoBtn.setOnClickListener {
            onInfo(med)
        }
    }
}