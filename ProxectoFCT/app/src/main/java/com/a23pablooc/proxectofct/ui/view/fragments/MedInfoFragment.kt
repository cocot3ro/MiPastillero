package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.data.network.CimaApiDefinitions
import com.a23pablooc.proxectofct.databinding.FragmentMedInfoBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.ui.view.adapters.MedInfoRecyclerViewAdapter
import com.a23pablooc.proxectofct.ui.viewmodel.MedInfoViewModel
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            med = gson.fromJson(it.getString(BundleKeys.MED_KEY), MedicamentoItem::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedInfoBinding.inflate(inflater, container, false)

        adapter = MedInfoRecyclerViewAdapter(med.principiosActivos)

        binding.toolbar.setupWithNavController(findNavController())

        updateView()

        return binding.root
    }

    private fun updateView() {
        if (CimaApiDefinitions.codNacionalPattern.matches(med.pkCodNacionalMedicamento.toString())) {
            binding.cimaMedView.visibility = View.VISIBLE
            binding.customMedView.visibility = View.GONE

            if (med.imagen.toString().isNotBlank())
                Glide.with(requireActivity()).load(med.imagen).into(binding.img)

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
        } else {
            binding.cimaMedView.visibility = View.GONE
            binding.customMedView.visibility = View.VISIBLE

            if (med.imagen.toString().isNotBlank())
                Glide.with(requireActivity()).load(med.imagen).into(binding.img2)

            binding.nombre2.text = med.nombre

            binding.btnSearch.setOnClickListener { search() }

            binding.btnHelp.setOnClickListener {
                binding.snackBar2.apply {
                    if (visibility == View.VISIBLE) {
                        handler.removeCallbacksAndMessages(null)
                        startAnimation(
                            AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out)
                        )
                        visibility = View.GONE
                    }

                    visibility = View.VISIBLE
                    startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in))

                    handler.postDelayed({
                        startAnimation(
                            AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out)
                        )
                        visibility = View.GONE
                    }, null, 5000)
                }
            }

            binding.btnOk.setOnClickListener {
                binding.snackBar.apply {
                    startAnimation(
                        AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out)
                    )
                    visibility = View.GONE
                }
            }
        }
    }

    private fun search() {
        val searchingToast = Toast.makeText(
            context,
            getString(R.string.searching),
            Toast.LENGTH_LONG
        ).also { it.show() }

        val codNacional = binding.codNacional.text.toString()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val fetchedMed = viewModel.search(codNacional)

                if (fetchedMed == null) {
                    Toast.makeText(
                        context,
                        getString(R.string.med_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                val oldCodNacional = med.pkCodNacionalMedicamento

                med.apply {
                    nombre = fetchedMed.nombre
                    laboratorio = fetchedMed.laboratorio
                    receta = fetchedMed.receta
                    prescripcion = fetchedMed.prescripcion
                    afectaConduccion = fetchedMed.afectaConduccion
                    numRegistro = fetchedMed.numRegistro
                    pkCodNacionalMedicamento = fetchedMed.pkCodNacionalMedicamento
                    imagen = fetchedMed.imagen
                    url = fetchedMed.url
                    prospecto = fetchedMed.prospecto
                    principiosActivos = fetchedMed.principiosActivos
                }

                viewModel.updateCodNacional(oldCodNacional, med)

                withContext(Dispatchers.Main) {
                    updateView()
                }

            } catch (e: IllegalArgumentException) {
                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    Toast.makeText(
                        context,
                        getString(R.string.invalid_national_drug_code),
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.btnHelp.performClick()
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    Toast.makeText(
                        context,
                        getString(R.string.connection_error),
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.btnHelp.performClick()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    Toast.makeText(
                        context,
                        getString(R.string.unknown_error),
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.btnHelp.performClick()

                    Log.e("MedInfoFragment", "Unknown error", e)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    searchingToast.cancel()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
}
