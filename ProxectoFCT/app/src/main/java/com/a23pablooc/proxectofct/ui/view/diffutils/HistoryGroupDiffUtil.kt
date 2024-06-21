package com.a23pablooc.proxectofct.ui.view.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem

class HistoryGroupDiffUtil(
    private val oldList: List<MedicamentoActivoItem>,
    private val newList: List<MedicamentoActivoItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = getList(oldList).size

    override fun getNewListSize(): Int = getList(newList).size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return getList(oldList)[oldItemPosition].pkMedicamentoActivo == getList(newList)[newItemPosition].pkMedicamentoActivo
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return getList(oldList)[oldItemPosition] == getList(newList)[newItemPosition]
    }

    private fun getList(list: List<MedicamentoActivoItem>): List<MedicamentoActivoItem> {
        return list.distinctBy { it.fkMedicamento.pkCodNacionalMedicamento }
    }
}