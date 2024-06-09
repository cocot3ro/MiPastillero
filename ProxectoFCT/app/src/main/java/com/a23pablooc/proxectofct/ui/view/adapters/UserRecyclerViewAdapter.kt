package com.a23pablooc.proxectofct.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import com.a23pablooc.proxectofct.ui.view.diffutils.UserDiffUtil
import com.a23pablooc.proxectofct.ui.view.viewholders.UserViewHolder
import kotlinx.coroutines.flow.StateFlow

class UserRecyclerViewAdapter(
    private var list: List<UsuarioItem>,
    private val onSelect: (UsuarioItem) -> Unit,
    private val onChangeDefaultFlow: StateFlow<Long>,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_profile, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.render(list[position], onSelect, onChangeDefaultFlow, lifecycleOwner)
    }

    fun updateData(newData: List<UsuarioItem>) {
        val diffUtil = UserDiffUtil(list, newData)
        val result = DiffUtil.calculateDiff(diffUtil)
        list = newData
        result.dispatchUpdatesTo(this)
    }
}