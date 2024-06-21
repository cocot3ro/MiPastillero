package com.a23pablooc.proxectofct.ui.view.viewholders

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.R
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
        context: Context,
        user: UsuarioItem,
        onSaveUserFlow: StateFlow<Any>,
        onSaveUser: (UsuarioItem) -> Unit,
        onChangeDefaultFlow: StateFlow<Long>,
        onChangeDefault: (UsuarioItem) -> Unit,
        lifecycleOwner: LifecycleOwner,
        onDelete: (UsuarioItem) -> Unit,
        notifyChange: (Long, Boolean) -> Unit
    ) {
        originalName = user.nombre

        binding.createUserLayout.etUserName.apply {
            setText(user.nombre)
            addTextChangedListener(
                afterTextChanged = {
                    notifyChange(user.pkUsuario, originalName != it.toString())
                }
            )
        }

        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                onSaveUserFlow.collect {
                    if (it is Unit) return@collect

                    val name = binding.createUserLayout.etUserName.text.toString()

                    if (name.isBlank()) {
                        binding.createUserLayout.etUserName.error = context.getString(R.string.required_field)
                        return@collect
                    }

                    if (originalName != name) {
                        val copy = user.copy(nombre = name)
                        onSaveUser(copy)
                        originalName = copy.nombre

                        binding.createUserLayout.etUserName.setText(copy.nombre)
                    }
                }
            }
        }

        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                onChangeDefaultFlow.collect { pk ->
                    val visibility = if (user.pkUsuario == pk) View.VISIBLE else View.GONE
                    binding.createUserLayout.ivFavBg.visibility = visibility

                    toast?.cancel()

                    if (!firstTime && user.pkUsuario == pk) {
                        toast = Toast.makeText(
                            binding.root.context,
                            String.format(context.getString(R.string.is_now_default_user), user.nombre),
                            Toast.LENGTH_SHORT
                        ).also { it.show() }
                    }

                    firstTime = false
                }
            }
        }

        binding.createUserLayout.favFrame.setOnClickListener {
            lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                onChangeDefault(user)
            }
        }

        binding.ibDelete.setOnClickListener {
            onDelete(user)
        }
    }
}