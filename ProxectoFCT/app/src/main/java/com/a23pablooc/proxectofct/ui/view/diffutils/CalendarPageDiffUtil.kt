package com.a23pablooc.proxectofct.ui.view.diffutils

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatTime
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import java.util.Date

class CalendarPageDiffUtil(
    private val oldList: List<MedicamentoActivoItem>,
    private val newList: List<MedicamentoActivoItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return getHorario(oldList, "getOldListSize(oldList)").size
    }

    override fun getNewListSize(): Int {
        return getHorario(newList, "getNewListSize(newList)").size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = getHorario(oldList, "areItemsTheSame(oldList)")[oldItemPosition].time
        val new = getHorario(newList, "areItemsTheSame(newList)")[newItemPosition].time

        return old == new
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = getHorario(oldList, "areContentsTheSame(oldList)")[oldItemPosition].time
        val new = getHorario(newList, "areContentsTheSame(newList)")[newItemPosition].time

        val oldItems = oldList.filter { it -> it.horario.any { it.time == old } }.toList()
        val newItems = newList.filter { it -> it.horario.any { it.time == new } }.toList()

        if (oldItems.size != newItems.size) return false

        for (i in oldItems.indices) {
            if (oldItems[i] != newItems[i]) return false
        }

        return true
    }

    private fun getHorario(list: List<MedicamentoActivoItem>, tag: String): List<Date> {
        return list.map { it.horario }
            .flatten()
            .distinctBy { it.time }
            .sortedBy { it.time }.also {
                Log.wtf(
                    "CalendarPageDiffUtil",
                    "$tag -> getHorario: ${it.map { d -> d.formatTime() }}"
                )
            }
    }
}