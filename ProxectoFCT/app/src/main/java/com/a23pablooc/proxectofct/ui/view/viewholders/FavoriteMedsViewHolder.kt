package com.a23pablooc.proxectofct.ui.view.viewholders

import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.databinding.FavoriteMedBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem

class FavoriteMedsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = FavoriteMedBinding.bind(view)

    fun render(
        med: MedicamentoItem,
        onAdd: (MedicamentoItem) -> Unit,
        onInfo: (MedicamentoItem) -> Unit
    ) {
        binding.name.text = med.nombre

        val bitMap = BitmapFactory.decodeByteArray(
            med.imagen,
            0,
            med.imagen.size
        )

        binding.img.setImageBitmap(bitMap)

        binding.addBtn.setOnClickListener {
            onAdd(med)
        }

        binding.infoBtn.setOnClickListener {
            onInfo(med)
        }
    }
}