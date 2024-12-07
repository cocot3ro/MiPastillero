package com.cocot3ro.mipastillero.ui.view.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.core.DataStoreManager
import com.cocot3ro.mipastillero.databinding.FragmentUsersBinding
import com.cocot3ro.mipastillero.domain.model.UsuarioItem
import com.cocot3ro.mipastillero.ui.view.adapters.UserRecyclerViewAdapter
import com.cocot3ro.mipastillero.ui.view.dialogs.CreateUserDialogFragment
import com.cocot3ro.mipastillero.ui.view.states.UiState
import com.cocot3ro.mipastillero.ui.viewmodel.UsersViewModel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : Fragment(), CreateUserDialogFragment.OnDataEnteredListener {
    private lateinit var binding: FragmentUsersBinding
    private val viewModel: UsersViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var adapter: UserRecyclerViewAdapter

    private var firstTime: Boolean = true

    private object BundleKeys {
        const val FIRST_TIME_KEY = "firstTime"
    }

    private val onSelectUser: (UsuarioItem) -> Unit = {
        viewModel.selectUser(it)
        navController.navigate(R.id.mainScreenFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            firstTime = savedInstanceState.getBoolean(BundleKeys.FIRST_TIME_KEY, true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(BundleKeys.FIRST_TIME_KEY, firstTime)
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

        if (!firstTime) binding.loadingView.visibility = View.GONE

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

                            val defaultUser = viewModel.getDefaultUserId()

                            val user: UsuarioItem? = when {
                                firstTime && uiState.data.size == 1 -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                    uiState.data.first() as UsuarioItem
                                }

                                firstTime && defaultUser != DataStoreManager.Defaults.DEFAULT_USER_ID -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                    uiState.data.map { it as UsuarioItem }
                                        .first { it.pkUsuario == defaultUser }
                                }

                                else -> null
                            }

                            firstTime = false

                            Handler(Looper.getMainLooper()).postDelayed({
                                if (user != null) {
                                    onSelectUser.invoke(user)
                                } else {
                                    binding.loadingView.visibility = View.GONE
                                    binding.fabAddUser.visibility = View.VISIBLE
                                }
                            }, 1000)
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
        CreateUserDialogFragment().show(childFragmentManager, CreateUserDialogFragment.TAG)
    }
}