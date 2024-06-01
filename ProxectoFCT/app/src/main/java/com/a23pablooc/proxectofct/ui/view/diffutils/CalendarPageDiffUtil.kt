package com.a23pablooc.proxectofct.ui.view.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import java.util.Date

class CalendarPageDiffUtil(
    private val oldList: List<MedicamentoActivoItem>,
    private val newList: List<MedicamentoActivoItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = getHorario(oldList).size

    override fun getNewListSize(): Int = getHorario(newList).size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = getHorario(oldList)[oldItemPosition].time
        val new = getHorario(newList)[newItemPosition].time

        return old == new
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = getHorario(oldList)[oldItemPosition].time

        val new = getHorario(newList)[newItemPosition].time

        val oldItems = oldList.filter { it -> it.horario.any { it.time == old} }.toList()
        val newItems = newList.filter { it -> it.horario.any { it.time == new} }.toList()

        return oldItems.containsAll(newItems) && newItems.containsAll(oldItems)
    }

    private fun getHorario(list: List<MedicamentoActivoItem>): List<Date> {
        return list.map { it.horario }
            .flatten()
            .distinctBy { it.time }
            .sortedBy { it.time }
    }
}