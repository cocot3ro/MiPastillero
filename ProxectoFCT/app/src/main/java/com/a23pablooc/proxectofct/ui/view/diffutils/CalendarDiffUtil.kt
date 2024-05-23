package com.a23pablooc.proxectofct.ui.view.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.a23pablooc.proxectofct.domain.model.MedicamentoCalendarioItem

class CalendarDiffUtil(
    private val oldList: List<MedicamentoCalendarioItem>,
    private val newList: List<MedicamentoCalendarioItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}