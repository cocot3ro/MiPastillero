package com.a23pablooc.proxectofct.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import com.a23pablooc.proxectofct.ui.view.diffutils.UserDiffUtil
import com.a23pablooc.proxectofct.ui.view.viewholders.ManageUserViewHolder
import kotlinx.coroutines.flow.StateFlow

class ManageUserRecyclerViewAdapter(
    private val context: Context,
    private var list: List<UsuarioItem>,
    private val onSaveUserFlow: StateFlow<Any>,
    private val onSaveUser: (UsuarioItem) -> Unit,
    private val onChangeDefaultFlow: StateFlow<Long>,
    private val onChangeDefault: (UsuarioItem) -> Unit,
    private val lifecycleOwner: LifecycleOwner,
    private val onDelete: (UsuarioItem) -> Unit,
    private val notifyChange: (Long, Boolean) -> Unit
) : RecyclerView.Adapter<ManageUserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageUserViewHolder {
        return ManageUserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.manage_user_profile, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ManageUserViewHolder, position: Int) {
        holder.render(
            context,
            list[position],
            onSaveUserFlow,
            onSaveUser,
            onChangeDefaultFlow,
            onChangeDefault,
            lifecycleOwner,
            onDelete,
            notifyChange
        )
    }

    fun updateData(newData: List<UsuarioItem>) {
        val diffUtil = UserDiffUtil(list, newData)
        val result = DiffUtil.calculateDiff(diffUtil)
        list = newData
        result.dispatchUpdatesTo(this)
    }
}