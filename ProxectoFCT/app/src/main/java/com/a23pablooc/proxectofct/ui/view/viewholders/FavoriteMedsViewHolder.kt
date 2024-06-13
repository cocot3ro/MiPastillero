package com.a23pablooc.proxectofct.ui.view.viewholders

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
        binding.name.text = med.nombre

        if (med.imagen.toString().isNotBlank()) {
            Glide.with(binding.root.context)
                .load(med.imagen)
                .into(binding.img)
        }

        binding.addBtn.setOnClickListener { onAdd(med) }

        binding.infoBtn.setOnClickListener { onInfo(med) }
    }
}