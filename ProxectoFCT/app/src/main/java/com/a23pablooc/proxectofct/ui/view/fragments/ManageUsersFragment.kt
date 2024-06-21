package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.FragmentManageUsersBinding
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import com.a23pablooc.proxectofct.ui.view.adapters.ManageUserRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.view.dialogs.CreateUserDialogFragment
import com.a23pablooc.proxectofct.ui.view.dialogs.DeleteUserDialogFragment
import com.a23pablooc.proxectofct.ui.view.states.UiState
import com.a23pablooc.proxectofct.ui.viewmodel.ManageUsersViewModel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManageUsersFragment : Fragment(), CreateUserDialogFragment.OnDataEnteredListener,
    DeleteUserDialogFragment.OnUserDeletedListener {
    private lateinit var binding: FragmentManageUsersBinding
    private val viewModel: ManageUsersViewModel by viewModels()

    private lateinit var adapter: ManageUserRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageUsersBinding.inflate(layoutInflater)

        binding.toolbar.setupWithNavController(findNavController())

        lifecycleScope.launch(Dispatchers.Main) {
            adapter = ManageUserRecyclerViewAdapter(
                context = requireContext(),
                list = emptyList(),
                onSaveUserFlow = viewModel.onSaveChangesFlow,
                onSaveUser = { viewModel.saveUser(it) },
                onChangeDefaultFlow = viewModel.onChangeDefaultUserFlow,
                onChangeDefault = { viewModel.changeDefaultUser(it.pkUsuario) },
                lifecycleOwner = viewLifecycleOwner,
                onDelete = { showDeleteUserDialog(it) },
                notifyChange = { userId, hasChanged ->
                    viewModel.registerChange(userId, hasChanged)

                    binding.fabSave.apply {
                        if (viewModel.hasChanges) show()
                        else hide()
                    }
                }
            )

            binding.rvUsers.apply {
                adapter = this@ManageUsersFragment.adapter
                layoutManager = FlexboxLayoutManager(context).apply {
                    justifyContent = JustifyContent.CENTER
                    alignItems = AlignItems.CENTER
                    flexDirection = FlexDirection.ROW
                    flexWrap = FlexWrap.WRAP
                }
            }

            binding.fabSave.setOnClickListener {
                viewModel.triggerSave()

                Toast.makeText(context, getString(R.string.changes_saved), Toast.LENGTH_SHORT).show()

                binding.fabSave.hide()
            }

            binding.fabAddUser.setOnClickListener {
                CreateUserDialogFragment().show(childFragmentManager, CreateUserDialogFragment.TAG)
            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success<*> -> {
                            binding.progressBar.visibility = View.GONE
                            val data = uiState.data.map { it as UsuarioItem }

                            binding.emptyDataView.visibility =
                                if (data.isEmpty()) View.VISIBLE else View.GONE

                            adapter.updateData(data)
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        viewModel.fetchData(requireContext())

        viewModel.trigger()

        return binding.root
    }

    override fun onDataEntered(userName: String, isDefault: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val defaultUserPk = viewModel.createUser(UsuarioItem(nombre = userName))
            if (isDefault) viewModel.changeDefaultUser(defaultUserPk)
        }
    }

    override fun onUserDeleted(usuario: UsuarioItem) {
        viewModel.deleteUser(usuario)
    }

    private fun showDeleteUserDialog(usuario: UsuarioItem) {
        DeleteUserDialogFragment(usuario).show(childFragmentManager, DeleteUserDialogFragment.TAG)
    }
}