package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.core.DataStoreManager
import com.a23pablooc.proxectofct.databinding.FragmentUsersBinding
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import com.a23pablooc.proxectofct.ui.view.adapters.UserRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.view.dialogs.CreateUserFragmentDialog
import com.a23pablooc.proxectofct.ui.view.states.UiState
import com.a23pablooc.proxectofct.ui.viewmodel.UsersViewModel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : Fragment(), CreateUserFragmentDialog.OnDataEnteredListener {
    private lateinit var binding: FragmentUsersBinding
    private val viewModel: UsersViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var adapter: UserRecyclerViewAdapter

    private var firstTime: Boolean = true
    private val firstTimeKey = "firstTime"

    private val onSelectUser: (UsuarioItem) -> Unit = {
        viewModel.selectUser(it)
        navController.navigate(R.id.mainScreenFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            firstTime = savedInstanceState.getBoolean(firstTimeKey, true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(firstTimeKey, firstTime)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(layoutInflater)

        navController = requireActivity().supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)?.findNavController()!!

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            adapter = UserRecyclerViewAdapter(
                emptyList(),
                onSelectUser,
                viewModel.onChangeDefaultUserFlow,
                viewLifecycleOwner
            )

            binding.rvUsers.apply {
                adapter = this@UsersFragment.adapter
                layoutManager = FlexboxLayoutManager(context).apply {
                    justifyContent = JustifyContent.CENTER
                    alignItems = AlignItems.CENTER
                    flexDirection = FlexDirection.ROW
                    flexWrap = FlexWrap.WRAP
                }
            }

            binding.fabAddUser.setOnClickListener { showAddUserDialog() }

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success<*> -> {
                            val data = uiState.data.map { it as UsuarioItem }

                            binding.emptyDataView.visibility =
                                if (data.isEmpty()) View.VISIBLE else View.GONE

                            adapter.updateData(data)

                            binding.progressBar.visibility = View.GONE

                            when {
                                firstTime && uiState.data.size == 1 -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                    val user = uiState.data.first() as UsuarioItem
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        onSelectUser.invoke(user)
                                    }, 500)
                                }

                                firstTime && viewModel.getDefaultUserId() != DataStoreManager.Defaults.DEFAULT_USER_ID -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                    val defaultUser = viewModel.getDefaultUserId()
                                    val user = uiState.data.map { it as UsuarioItem }
                                        .first { it.pkUsuario == defaultUser }
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        onSelectUser.invoke(user)
                                    }, 500)
                                }
                            }

                            firstTime = false
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

        viewModel.trigger()

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
                            navController.navigate(R.id.manageUsersFragment)
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

    override fun onDataEntered(userName: String, isDefault: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val defaultUserPk = viewModel.createUser(UsuarioItem(nombre = userName))
            if (isDefault) viewModel.changeDefaultUser(defaultUserPk)
        }
    }

    private fun showAddUserDialog() {
        CreateUserFragmentDialog().show(childFragmentManager, CreateUserFragmentDialog.TAG)
    }
}