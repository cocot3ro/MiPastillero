package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.databinding.UserProfileBinding
import com.a23pablooc.proxectofct.domain.model.UsuarioItem

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = UserProfileBinding.bind(view)

    fun render(user: UsuarioItem, onSelect: (UsuarioItem) -> Unit) {
        binding.root.setOnClickListener { onSelect(user) }
        binding.tvUserName.text = user.nombre
    }

}