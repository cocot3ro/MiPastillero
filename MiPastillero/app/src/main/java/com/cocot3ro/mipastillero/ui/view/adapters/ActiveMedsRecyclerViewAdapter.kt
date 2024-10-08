package com.cocot3ro.mipastillero.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.ui.view.diffutils.ActiveMedDiffUtil
import com.cocot3ro.mipastillero.ui.view.viewholders.ActiveMedsViewHolder

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
