package com.cocot3ro.mipastillero.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.ui.view.diffutils.HistoryDiffUtil
import com.cocot3ro.mipastillero.ui.view.viewholders.HistoryViewHolder

class HistoryRecyclerViewAdapter(
    private var list: List<MedicamentoActivoItem>
) : RecyclerView.Adapter<HistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.med_history, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.render(list[position])
    }

    fun updateData(newData: List<MedicamentoActivoItem>) {
        val diffUtil = HistoryDiffUtil(list, newData)
        val result = DiffUtil.calculateDiff(diffUtil)
        list = newData
        result.dispatchUpdatesTo(this)
    }
}