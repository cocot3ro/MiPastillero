package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.View
import android.widget.Toast
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
    private var toast: Toast? = null
    private var firstTime = true

    fun render(
        usuarioItem: UsuarioItem,
        onSaveUserFlow: StateFlow<Unit>,
        onSaveUser: (UsuarioItem) -> Unit,
        onChangeDefaultFlow: StateFlow<Long>,
        onChangeDefault: (UsuarioItem) -> Unit,
        lifecycleOwner: LifecycleOwner,
        onDelete: (UsuarioItem) -> Unit
    ) {
        originalName = usuarioItem.nombre

        binding.createUserLayout.etUserName.setText(usuarioItem.nombre)

        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                onSaveUserFlow.collect {
                    if (binding.createUserLayout.etUserName.text.toString().isBlank()) {
                        // TODO: hardcoded string
                        binding.createUserLayout.etUserName.error = "Campo obligatorio."
                        return@collect
                    }

                    val name = binding.createUserLayout.etUserName.text.toString()

                    if (originalName != name) {
                        val copy = usuarioItem.copy(nombre = name)
                        onSaveUser(copy)
                        originalName = copy.nombre
                    }
                }
            }
        }

        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                onChangeDefaultFlow.collect { pk ->
                    val visibility = if (usuarioItem.pkUsuario == pk) View.VISIBLE else View.GONE
                    binding.createUserLayout.ivFavBg.visibility = visibility

                    toast?.cancel()

                    if (!firstTime && usuarioItem.pkUsuario == pk) {
                        toast = Toast.makeText(
                            binding.root.context,
                            "${usuarioItem.nombre} es ahora el usuario por defecto",
                            Toast.LENGTH_SHORT
                        ).also { it.show() }
                    }

                    firstTime = false
                }
            }
        }

        binding.createUserLayout.favFrame.setOnClickListener {
            lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                onChangeDefault(usuarioItem)
            }
        }

        binding.ibDelete.setOnClickListener {
            onDelete(usuarioItem)
        }
    }
}