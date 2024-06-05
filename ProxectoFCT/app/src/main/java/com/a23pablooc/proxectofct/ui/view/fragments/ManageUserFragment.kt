package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.a23pablooc.proxectofct.databinding.FragmentManageUserBinding
import com.a23pablooc.proxectofct.ui.view.viewholders.ManageUserViewModel

class ManageUserFragment : Fragment() {
    private lateinit var binding: FragmentManageUserBinding

    private val viewModel: ManageUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageUserBinding.inflate(layoutInflater)

        

        return binding.root
    }
}