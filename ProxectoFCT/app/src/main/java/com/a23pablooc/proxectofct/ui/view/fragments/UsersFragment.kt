package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.a23pablooc.proxectofct.ui.view.viewholders.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val ARGS_MODE_KEY = "ARGS_MODE_KEY"

@AndroidEntryPoint
class UsersFragment : Fragment(), CreateUserFragmentDialog.OnDataEnteredListener {
    private lateinit var binding: FragmentUsersBinding
    private val viewModel: UsersViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var adapter: UserRecyclerViewAdapter

    private lateinit var mode: FragmentMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = FragmentMode.valueOf(it.getString(ARGS_MODE_KEY)!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(layoutInflater)

        navController = requireActivity().supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)?.findNavController()!!

        adapter = UserRecyclerViewAdapter(
            emptyList(),
            {
                viewModel.selectUser(it)
                navController.navigate(R.id.mainScreenFragment)
            },
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
                            adapter.updateData(uiState.data.map { it as UsuarioItem })

                            // TODO: vista para lista vacia
                            // binding.emptyListView.visibility = (uiState.data.isEmpty() ? View.VISIBLE : View.GONE)
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

    override fun onDataEntered(user: UsuarioItem) {
        viewModel.createUser(user)
    }

    private fun showAddUserDialog() {
        CreateUserFragmentDialog().show(childFragmentManager, CreateUserFragmentDialog.TAG)
    }

    companion object {
        enum class FragmentMode {
            LOGIN,
            MANAGE
        }

        @JvmStatic
        fun newInstance(mode: FragmentMode) =
            UsersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGS_MODE_KEY, mode.name)
                }
            }
    }
}