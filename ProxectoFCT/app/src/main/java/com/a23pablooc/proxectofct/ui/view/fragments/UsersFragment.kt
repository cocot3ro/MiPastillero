package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.FragmentUsersBinding
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import com.a23pablooc.proxectofct.ui.view.adapters.UserRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.view.dialogs.CreateUserFragmentDialog
import com.a23pablooc.proxectofct.ui.view.states.UiState
import com.a23pablooc.proxectofct.ui.viewmodel.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val ARGS_MODE_KEY = "ARGS_MODE_KEY"

@AndroidEntryPoint
class UsersFragment : Fragment(), CreateUserFragmentDialog.OnDataEnteredListener {
    private lateinit var binding: FragmentUsersBinding
    private val viewModel: UsersViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var adapter: UserRecyclerViewAdapter

    private var fragmentMode: FragmentMode = FragmentMode.LOGIN

    private val onSelectUser: (UsuarioItem) -> Unit = {
        viewModel.selectUser(it)
        navController.navigate(R.id.mainScreenFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentMode =
                FragmentMode.valueOf(it.getString(ARGS_MODE_KEY) ?: FragmentMode.LOGIN.name)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(layoutInflater)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        navController = requireActivity().supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)?.findNavController()!!

        adapter = UserRecyclerViewAdapter(
            emptyList(),
            onSelectUser,
            viewModel.manageFlow,
        )

        binding.rvUsers.apply {
            adapter = this@UsersFragment.adapter
            layoutManager = GridLayoutManager(context, 2)
        }

        binding.fabAddUser.setOnClickListener {
            showAddUserDialog()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success<*> -> {
                            binding.progressBar.visibility = View.GONE

                            when {
                                fragmentMode == FragmentMode.LOGIN && uiState.data.size == 1 -> {
                                    val user = uiState.data.first() as UsuarioItem
                                    onSelectUser(user)
                                }

                                fragmentMode == FragmentMode.LOGIN && viewModel.hasDefaultUser() -> {
                                    val user = uiState.data.map { it as UsuarioItem }
                                        .first { it.pkUsuario == viewModel.defaultUserId() }
                                    onSelectUser(user)
                                }

                                else -> {
                                    val data = uiState.data.map { it as UsuarioItem }
                                    adapter.updateData(data)
                                }
                            }
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_LONG).show()
                            Log.e("UsersFragment", uiState.errorMessage, uiState.exception)
                        }
                    }
                }
            }
        }

        viewModel.fetchData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MenuHost).addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_toolbar_users, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.manage_users -> {
                            true
                        }

                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    override fun onDataEntered(user: UsuarioItem) {
        viewModel.createUser(user)
    }

    private fun showAddUserDialog() {
        CreateUserFragmentDialog().show(childFragmentManager, CreateUserFragmentDialog.TAG)
    }

    companion object {
        @JvmStatic
        fun newInstance(mode: FragmentMode) =
            UsersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGS_MODE_KEY, mode.name)
                }
            }
    }

    enum class FragmentMode {
        LOGIN,
        MANAGE
    }
}