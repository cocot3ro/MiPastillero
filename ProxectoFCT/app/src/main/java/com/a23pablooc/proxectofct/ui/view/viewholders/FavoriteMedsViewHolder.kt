package com.a23pablooc.proxectofct.ui.view.viewholders

import android.os.Handler
import android.os.Looper
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
            // Espera 1 milisegundo para cargar la imagen para que primero se muestre la imagen por defecto
            // y luego la imagen que se carga
            // Esto se hace para evitar 'misteriosos' problemas de carga de la imagen
            // porque se redimensiona a un tamaño no apropiado
            Handler(Looper.getMainLooper()).postDelayed({
                Glide.with(binding.root.context)
                    .load(med.imagen)
                    .into(binding.img)
            }, 100)
        }

        binding.addBtn.setOnClickListener {
            onAdd(med)
        }

        binding.infoBtn.setOnClickListener {
            onInfo(med)
        }
    }
}