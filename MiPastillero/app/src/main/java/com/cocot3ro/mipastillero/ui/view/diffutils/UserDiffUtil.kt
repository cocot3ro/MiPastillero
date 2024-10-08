package com.cocot3ro.mipastillero.ui.view.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.cocot3ro.mipastillero.domain.model.UsuarioItem

class UserDiffUtil(
    private val oldList: List<UsuarioItem>,
    private val newList: List<UsuarioItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].pkUsuario == newList[newItemPosition].pkUsuario
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}