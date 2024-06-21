package com.a23pablooc.proxectofct.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.ui.view.diffutils.MedInfoDiffUtil
import com.a23pablooc.proxectofct.ui.view.viewholders.MedInfoViewHolder

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