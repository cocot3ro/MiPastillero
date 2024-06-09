package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.databinding.UserProfileBinding
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = UserProfileBinding.bind(view)

    fun render(
        user: UsuarioItem,
        onSelect: (UsuarioItem) -> Unit,
        onChangeDefaultFlow: StateFlow<Long>,
        lifecycleOwner: LifecycleOwner
    ) {
        binding.root.setOnClickListener { onSelect.invoke(user) }
        binding.tvUserName.text = user.nombre

        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                onChangeDefaultFlow.collect { pk ->
                    val visibility = if (user.pkUsuario == pk) View.VISIBLE else View.GONE
                    binding.favFrame.visibility = visibility
                }
            }
        }
    }
}