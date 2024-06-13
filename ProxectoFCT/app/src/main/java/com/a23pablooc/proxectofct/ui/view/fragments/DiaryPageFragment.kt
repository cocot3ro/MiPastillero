package com.a23pablooc.proxectofct.ui.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatDate
import com.a23pablooc.proxectofct.core.DateTimeUtils.getDayName
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import com.a23pablooc.proxectofct.databinding.FragmentDiaryPageBinding
import com.a23pablooc.proxectofct.domain.model.AgendaItem
import com.a23pablooc.proxectofct.ui.view.states.UiState
import com.a23pablooc.proxectofct.ui.viewmodel.DiaryPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class DiaryPageFragment : Fragment() {
    private lateinit var binding: FragmentDiaryPageBinding
    private val viewModel: DiaryPageViewModel by viewModels()

    private lateinit var date: Date
    private lateinit var item: AgendaItem
    private lateinit var originalDescription: String

    private object BundleKeys {
        const val ARGS_DATE_KEY = "ARG_DATE_KEY"
        const val ARGS_ITEM_KEY = "ARG_ITEM_KEY"
        const val ARGS_ORIGINAL_DESCRIPTION_KEY = "ARG_ORIGINAL_DESCRIPTION_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date = Date(it.getLong(BundleKeys.ARGS_DATE_KEY))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiaryPageBinding.inflate(layoutInflater)

        val txt = "${date.getDayName(requireContext())} - ${date.formatDate()}"
        binding.diaryDay.text = txt

        binding.fabSave.setOnClickListener {
            val newDescription = binding.bodyText.text.toString()
            if (newDescription != originalDescription) {
                viewModel.saveDiaryEntry(
                    item.copy(descripcion = newDescription).also {
                        item = it
                    }
                )
            }

            updateFab()
        }

        binding.bodyText.addTextChangedListener(
            afterTextChanged = {
                updateFab()
            }
        )

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success<*> -> {
                            Log.v("DiaryPageFragment", "Success")

                            binding.progressBar.visibility = View.GONE
                            val data = uiState.data.map { it as AgendaItem }

                            item = data.firstOrNull() ?: AgendaItem(
                                DateTimeUtils.today.zeroTime(),
                                viewModel.userInfoProvider.currentUser.pkUsuario,
                                ""
                            )

                            originalDescription = item.descripcion

                            binding.bodyText.setText(item.descripcion)
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                context,
                                "Error fetching diary entry from DB",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }

        viewModel.fetchData(date)

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.v("DiaryPageFragment", "onSaveInstanceState")
        super.onSaveInstanceState(outState)
        outState.putString(BundleKeys.ARGS_ORIGINAL_DESCRIPTION_KEY, originalDescription)
        outState.putLong(BundleKeys.ARGS_DATE_KEY, date.time)
        outState.putString(
            BundleKeys.ARGS_ITEM_KEY,
            viewModel.gson.toJson(item, AgendaItem::class.java)
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.v("DiaryPageFragment", "onViewStateRestored")
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            originalDescription = it.getString(BundleKeys.ARGS_ORIGINAL_DESCRIPTION_KEY, "")
            date = Date(it.getLong(BundleKeys.ARGS_DATE_KEY))
            item = viewModel.gson.fromJson(
                it.getString(BundleKeys.ARGS_ITEM_KEY),
                AgendaItem::class.java
            )
        }
    }

    private fun updateFab() {
        binding.fabSave.apply {
            if (!::originalDescription.isInitialized) return
            if (binding.bodyText.text.toString() == originalDescription) hide()
            else show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(date: Date) =
            DiaryPageFragment().apply {
                arguments = Bundle().apply {
                    putLong(BundleKeys.ARGS_DATE_KEY, date.time)
                }
            }
    }
}