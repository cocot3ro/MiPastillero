package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.FragmentMedInfoBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.ui.view.adapters.MedInfoRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.viewmodel.MedInfoViewModel
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MedInfoFragment : Fragment() {

    @Inject
    lateinit var gson: Gson

    private lateinit var binding: FragmentMedInfoBinding
    private val viewModel: MedInfoViewModel by viewModels()

    private lateinit var adapter: MedInfoRecyclerViewAdapter

    lateinit var med: MedicamentoItem

    object BundleKeys {
        const val MED_KEY = "med_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            med = gson.fromJson(it.getString(BundleKeys.MED_KEY), MedicamentoItem::class.java)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(BundleKeys.MED_KEY, gson.toJson(med, MedicamentoItem::class.java))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedInfoBinding.inflate(inflater, container, false)

        adapter = MedInfoRecyclerViewAdapter(med.principiosActivos)

        binding.toolbar.setupWithNavController(findNavController())

        Glide.with(requireContext()).load(med.imagen).into(binding.img)

        binding.nombre.text = med.nombre

        binding.lab.text = med.laboratorio

        binding.ibReceta.apply {
            visibility = if (med.receta) View.VISIBLE else View.GONE

            if (visibility == View.VISIBLE) {
                setOnClickListener {
                    Toast.makeText(context, med.prescripcion, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.ibConduccion.apply {

            visibility = if (med.afectaConduccion) View.VISIBLE else View.GONE

            if (visibility == View.VISIBLE) {
                setOnClickListener {

                    binding.snackBar.apply {
                        if (visibility == View.VISIBLE) {
                            handler.removeCallbacksAndMessages(null)
                            startAnimation(
                                AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out)
                            )
                            visibility = View.GONE
                        }

                        visibility = View.VISIBLE
                        startAnimation(
                            AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in)
                        )

                        handler.postDelayed({
                            startAnimation(
                                AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out)
                            )
                            visibility = View.GONE
                        }, null, 5000)
                    }
                }
            }
        }

        binding.btnSee.setOnClickListener {
            viewModel.openUrl(requireContext(), med.prospecto)
            binding.snackBar.apply {
                startAnimation(
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out)
                )
                visibility = View.GONE
            }
        }

        binding.nregistro.text = med.numRegistro

        binding.cn.text = med.pkCodNacionalMedicamento.toString()

        binding.ibWeb.setOnClickListener {
            viewModel.openUrl(
                requireContext(),
                med.url
            )
        }

        binding.ibProspecto.setOnClickListener {
            viewModel.openUrl(
                requireContext(),
                med.prospecto
            )
        }

        binding.rvPrincipiosActivos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MedInfoRecyclerViewAdapter(med.principiosActivos)
        }

        return binding.root
    }
}