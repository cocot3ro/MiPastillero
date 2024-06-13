package com.a23pablooc.proxectofct.ui.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.ui.view.diffutils.HistoryGroupDiffUtil
import com.a23pablooc.proxectofct.ui.view.viewholders.HistoryGroupViewHolder

class HistoryGroupRecyclerViewAdapter(
    private var list: List<MedicamentoActivoItem>,
    private val onInfo: (MedicamentoActivoItem) -> Unit
) : RecyclerView.Adapter<HistoryGroupViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryGroupViewHolder {
        return HistoryGroupViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.med_history_group, parent, false)
        )
    }

    override fun getItemCount(): Int = getList(list).size

    override fun onBindViewHolder(holder: HistoryGroupViewHolder, position: Int) {
        val med = getList(list)[position]
        holder.render(
            list.filter { it.pkMedicamentoActivo == med.pkMedicamentoActivo },
            onInfo
        )
    }

    fun updateData(newData: List<MedicamentoActivoItem>) {
        val diffUtil = HistoryGroupDiffUtil(list, newData)
        val result = DiffUtil.calculateDiff(diffUtil)
        list = newData
        result.dispatchUpdatesTo(this)
    }

    private fun getList(list: List<MedicamentoActivoItem>): List<MedicamentoActivoItem> {
        return list.distinctBy { it.fkMedicamento.pkCodNacionalMedicamento }
    }
}
