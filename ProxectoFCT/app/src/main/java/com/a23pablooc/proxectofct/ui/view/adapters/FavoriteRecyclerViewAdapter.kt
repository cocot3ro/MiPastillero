package com.a23pablooc.proxectofct.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.ui.view.diffutils.FavoriteMedsDiffUtil
import com.a23pablooc.proxectofct.ui.view.viewholders.FavoriteMedsViewHolder

class FavoriteRecyclerViewAdapter(
    private var list: List<MedicamentoItem>,
    private val onAdd: (MedicamentoItem) -> Unit,
    private val onInfo: (MedicamentoItem) -> Unit
) : RecyclerView.Adapter<FavoriteMedsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMedsViewHolder {
        return FavoriteMedsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.favorite_med, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FavoriteMedsViewHolder, position: Int) {
        holder.render(list[position], onAdd, onInfo)
    }

    fun updateData(newData: List<MedicamentoItem>) {
        val diffUtil = FavoriteMedsDiffUtil(list, newData)
        val result = DiffUtil.calculateDiff(diffUtil)
        list = newData
        result.dispatchUpdatesTo(this)
    }
}