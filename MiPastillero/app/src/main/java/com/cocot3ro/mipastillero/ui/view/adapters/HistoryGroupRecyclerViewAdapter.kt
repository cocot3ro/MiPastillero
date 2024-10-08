package com.cocot3ro.mipastillero.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.domain.model.MedicamentoItem
import com.cocot3ro.mipastillero.ui.view.diffutils.HistoryGroupDiffUtil
import com.cocot3ro.mipastillero.ui.view.viewholders.HistoryGroupViewHolder

class HistoryGroupRecyclerViewAdapter(
    private var list: List<MedicamentoActivoItem>,
    private val onInfo: (MedicamentoItem) -> Unit
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
