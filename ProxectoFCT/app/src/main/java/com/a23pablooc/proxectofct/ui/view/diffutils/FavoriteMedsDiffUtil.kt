package com.a23pablooc.proxectofct.ui.view.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem

class FavoriteMedsDiffUtil(
    private val oldList: List<MedicamentoItem>,
    private val newList: List<MedicamentoItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}