package com.a23pablooc.proxectofct.old_view

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.TextView
import com.a23pablooc.proxectofct.old_model.Medicamento
import com.a23pablooc.proxectofct.old_viewModel.PillboxViewModel
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.OldDialogInfoBinding
import com.a23pablooc.proxectofct.databinding.OldHyperlinkLayoutBinding

class InfoDialog(
    private val context: Context,
    private val medicamento: Medicamento,
) {

    private var _binding: OldDialogInfoBinding? =
        OldDialogInfoBinding.inflate(LayoutInflater.from(context))
    private val binding get() = _binding!!

    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    private val alertDialog: AlertDialog = AlertDialog.Builder(context)
        .setView(binding.root)
        .setTitle(context.getString(R.string.info_medicamento))
        .setNeutralButton(context.getString(R.string.cerrar), null)
        .create()

    init {
        _pillboxViewModel = PillboxViewModel.getInstance(context)

        medicamento.nombre.let { binding.nombre.text = it }
        if (medicamento.imagen != null && medicamento.imagen.isNotEmpty()) {
            binding.img.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    medicamento.imagen,
                    0,
                    medicamento.imagen.size
                )
            )
        } else {
            binding.img.setImageResource(R.mipmap.no_image_available)
        }

        if (!medicamento.url.isNullOrBlank()) {
            OldHyperlinkLayoutBinding.inflate(
                LayoutInflater.from(context),
                binding.infoLayout,
                true
            ).apply {
                nombreHiperenlace.text = context.getString(R.string.ver_en_web)
                val listener = OnClickListener {
                    pillboxViewModel.openURL(context, medicamento.url)
                }
                nombreHiperenlace.setOnClickListener(listener)
                abrirHiperenlace.setOnClickListener(listener)
            }
        }

        if (!medicamento.fichaTecnica.isNullOrBlank()) {
            OldHyperlinkLayoutBinding.inflate(
                LayoutInflater.from(context),
                binding.infoLayout,
                true
            ).apply {
                nombreHiperenlace.text = context.getString(R.string.ficha_tecnica)
                val listener = OnClickListener {
                    pillboxViewModel.openURL(context, medicamento.fichaTecnica)
                }
                nombreHiperenlace.setOnClickListener(listener)
                abrirHiperenlace.setOnClickListener(listener)
            }
        }

        if (!medicamento.prospecto.isNullOrBlank()) {
            OldHyperlinkLayoutBinding.inflate(
                LayoutInflater.from(context),
                binding.infoLayout,
                true
            ).apply {
                nombreHiperenlace.text = context.getString(R.string.prospecto)
                val listener = OnClickListener {
                    pillboxViewModel.openURL(context, medicamento.prospecto)
                }
                nombreHiperenlace.setOnClickListener(listener)
                abrirHiperenlace.setOnClickListener(listener)
            }
        }

        if (!medicamento.numRegistro.isNullOrBlank()) {
            val textView = TextView(context)

            @Suppress("SetTextI18n")
            textView.text =
                "${context.getString(R.string.numero_registro)} ${medicamento.numRegistro}"

            binding.infoLayout.addView(textView)
        }

        if (medicamento.codNacional != null && medicamento.codNacional != -1) {
            val textView = TextView(context)

            @Suppress("SetTextI18n")
            textView.text =
                "${context.getString(R.string.codigo_nacional)} ${medicamento.codNacional}"

            binding.infoLayout.addView(textView)
        }

        if (!medicamento.laboratorio.isNullOrBlank()) {
            val textView = TextView(context)

            @Suppress("SetTextI18n")
            textView.text =
                "${context.getString(R.string.laboratorio)} ${medicamento.laboratorio}"

            binding.infoLayout.addView(textView)
        }

        if (!medicamento.dosis.isNullOrBlank()) {
            val textView = TextView(context)

            @Suppress("SetTextI18n")
            textView.text =
                "${context.getString(R.string.dosis)} ${medicamento.dosis}"

            binding.infoLayout.addView(textView)
        }

        if (!medicamento.receta.isNullOrBlank()) {
            val textView = TextView(context)

            @Suppress("SetTextI18n")
            textView.text = "${medicamento.receta}"

            binding.infoLayout.addView(textView)
        }

        if (!medicamento.principiosActivos.isNullOrEmpty()) {
            val textView = TextView(context)

            @Suppress("SetTextI18n")
            textView.text = context.getString(R.string.principios_activos)

            binding.infoLayout.addView(textView)

            for (principioActivo in medicamento.principiosActivos) {
                val textViewPrincipioActivo = TextView(context)

                @Suppress("SetTextI18n")
                textViewPrincipioActivo.text = "\t$principioActivo"

                binding.infoLayout.addView(textViewPrincipioActivo)
            }
        }

    }

    fun show() {
        alertDialog.show()
    }

}