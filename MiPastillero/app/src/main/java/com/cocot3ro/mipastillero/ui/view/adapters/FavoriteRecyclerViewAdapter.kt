package com.cocot3ro.mipastillero.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.domain.model.MedicamentoItem
import com.cocot3ro.mipastillero.ui.view.diffutils.FavoriteMedsDiffUtil
import com.cocot3ro.mipastillero.ui.view.viewholders.FavoriteMedsViewHolder

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