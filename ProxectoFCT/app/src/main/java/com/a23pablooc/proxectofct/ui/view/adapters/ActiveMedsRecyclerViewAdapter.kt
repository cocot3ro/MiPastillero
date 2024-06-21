package com.a23pablooc.proxectofct.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.ui.view.diffutils.ActiveMedDiffUtil
import com.a23pablooc.proxectofct.ui.view.viewholders.ActiveMedsViewHolder

class ActiveMedsRecyclerViewAdapter(
    private var list: List<MedicamentoActivoItem>,
    private val onFav: (MedicamentoActivoItem) -> Unit,
    private val onInfo: (MedicamentoActivoItem) -> Unit
) : RecyclerView.Adapter<ActiveMedsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveMedsViewHolder {
        return ActiveMedsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.active_med, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ActiveMedsViewHolder, position: Int) {
        holder.render(list[position], onFav, onInfo)
    }

    fun updateData(newData: List<MedicamentoActivoItem>) {
        val diffUtil = ActiveMedDiffUtil(list, newData)
        val result = DiffUtil.calculateDiff(diffUtil)
        list = newData
        result.dispatchUpdatesTo(this)
    }

}
