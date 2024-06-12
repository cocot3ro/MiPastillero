package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.a23pablooc.proxectofct.databinding.FragmentHistoryBinding
import com.a23pablooc.proxectofct.ui.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)

//        val list = listOf<MedicamentoHistorialItem>()
//
//        list.sortedWith(
//            compareBy(
//                { it.fkMedicamentoActivo.fechaInicio },
//                { abs(it.fkMedicamentoActivo.fechaInicio.time - it.fkMedicamentoActivo.fechaFin.time) }
//            )
//        )

        return binding.root
    }
}