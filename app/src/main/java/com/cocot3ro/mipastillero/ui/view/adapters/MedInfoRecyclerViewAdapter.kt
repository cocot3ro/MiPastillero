package com.cocot3ro.mipastillero.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.ui.view.diffutils.MedInfoDiffUtil
import com.cocot3ro.mipastillero.ui.view.viewholders.MedInfoViewHolder

class MedInfoRecyclerViewAdapter(
    private var list: List<String>
) : RecyclerView.Adapter<MedInfoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedInfoViewHolder {
        return MedInfoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.principio_activo_rv_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MedInfoViewHolder, position: Int) {
        holder.render(list[position])
    }

    fun updateData(newData: List<String>) {
        val diffUtil = MedInfoDiffUtil(list, newData)
        val result = DiffUtil.calculateDiff(diffUtil)
        list = newData
        result.dispatchUpdatesTo(this)
    }

}