package com.a23pablooc.proxectofct.ui.view.viewholders

import android.graphics.BitmapFactory
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.databinding.FavoriteMedBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoFavoritoItem

class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = FavoriteMedBinding.bind(view)

    fun render(
        med: MedicamentoFavoritoItem,
        onAdd: (MedicamentoFavoritoItem) -> Unit,
        onInfo: (MedicamentoFavoritoItem) -> Unit
    ) {
        binding.name.text = med.medicamento.nombre

        val bitMap = BitmapFactory.decodeByteArray(
            med.medicamento.imagen,
            0,
            med.medicamento.imagen.size
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