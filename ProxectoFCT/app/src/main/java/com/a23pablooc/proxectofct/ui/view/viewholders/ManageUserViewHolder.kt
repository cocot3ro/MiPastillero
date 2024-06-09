package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.databinding.ManageUserProfileBinding
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ManageUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ManageUserProfileBinding.bind(view)

    private lateinit var originalName: String

    fun render(
        usuarioItem: UsuarioItem,
        onSaveChangesFlow: StateFlow<Unit>,
        onSaveChanges: (UsuarioItem) -> Unit,
        onChangeDefaultFlow: StateFlow<Long>,
        onChangeDefault: (Long) -> Unit,
        lifecycleOwner: LifecycleOwner,
        onDelete: (UsuarioItem) -> Unit
    ) {
        originalName = usuarioItem.nombre

        binding.createUserLayout.etUserName.setText(usuarioItem.nombre)

        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                onSaveChangesFlow.collect {
                    if (binding.createUserLayout.etUserName.text.toString().isBlank()) {
                        // TODO: hardcoded string
                        binding.createUserLayout.etUserName.error = "Campo obligatorio. Debe contener al menos una letra"
                        return@collect
                    }

                    if (originalName != binding.createUserLayout.etUserName.text.toString()) {
                        val copy = usuarioItem.copy(nombre = binding.createUserLayout.etUserName.text.toString())
                        onSaveChanges(copy)
                        originalName = copy.nombre
                    }
                }
            }
        }

        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                onChangeDefaultFlow.collect { pk ->
                    val visibility = if (usuarioItem.pkUsuario == pk) View.VISIBLE else View.GONE
                    binding.createUserLayout.btnFavBg.visibility = visibility
                }
            }
        }

        binding.createUserLayout.favFrame.setOnClickListener {
            lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                onChangeDefault(usuarioItem.pkUsuario)
            }
        }

        binding.ibDelete.setOnClickListener {
            onDelete(usuarioItem)
        }
    }
}