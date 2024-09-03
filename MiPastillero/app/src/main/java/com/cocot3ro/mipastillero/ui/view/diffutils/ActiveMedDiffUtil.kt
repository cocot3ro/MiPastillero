package com.cocot3ro.mipastillero.ui.view.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem

class ActiveMedDiffUtil(
    private val oldList: List<MedicamentoActivoItem>,
    private val newList: List<MedicamentoActivoItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].pkMedicamentoActivo == newList[newItemPosition].pkMedicamentoActivo
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}