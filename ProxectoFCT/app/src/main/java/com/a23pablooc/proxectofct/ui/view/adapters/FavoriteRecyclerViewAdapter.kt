package com.a23pablooc.proxectofct.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.domain.model.MedicamentoFavoritoItem
import com.a23pablooc.proxectofct.ui.view.diffutils.FavoriteDiffUtil
import com.a23pablooc.proxectofct.ui.view.viewholders.FavoriteViewHolder

class FavoriteRecyclerViewAdapter(
    private var list: List<MedicamentoFavoritoItem>,
    private val onAdd: (MedicamentoFavoritoItem) -> Unit,
    private val onInfo: (MedicamentoFavoritoItem) -> Unit
) : RecyclerView.Adapter<FavoriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.favorite_med, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.render(list[position], onAdd, onInfo)
    }

    fun updateData(newData: List<MedicamentoFavoritoItem>) {
        val diffUtil = FavoriteDiffUtil(list, newData)
        val result = DiffUtil.calculateDiff(diffUtil)
        list = newData
        result.dispatchUpdatesTo(this)
    }
}