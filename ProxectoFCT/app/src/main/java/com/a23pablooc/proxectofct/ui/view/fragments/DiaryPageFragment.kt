package com.a23pablooc.proxectofct.ui.view.fragments

import android.icu.util.Calendar
import android.os.Bundle
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
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatDate
import com.a23pablooc.proxectofct.core.DateTimeUtils.getDayName
import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.databinding.FragmentDiaryPageBinding
import com.a23pablooc.proxectofct.domain.model.AgendaItem
import com.a23pablooc.proxectofct.ui.view.states.UiState
import com.a23pablooc.proxectofct.ui.viewmodel.DiaryPageViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class DiaryPageFragment : Fragment() {
    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var userInfoProvider: UserInfoProvider

    private lateinit var binding: FragmentDiaryPageBinding
    private val viewModel: DiaryPageViewModel by viewModels()

    private lateinit var savedDate: Date
    private lateinit var savedItem: AgendaItem
    private var isRestoredFromState = false

    private object BundleKeys {
        const val ARGS_DATE_KEY = "ARG_DATE_KEY"
        const val ARGS_ITEM_KEY = "ARG_ITEM_KEY"
        const val ARGS_ORIGINAL_DESCRIPTION_KEY = "ARG_ORIGINAL_DESCRIPTION_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            savedDate = Date(it.getLong(BundleKeys.ARGS_DATE_KEY))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiaryPageBinding.inflate(layoutInflater)

        val txt = "${savedDate.getDayName(requireContext())} - ${savedDate.formatDate()}"
        binding.diaryDay.text = txt

        binding.fabSave.setOnClickListener { onSave() }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success<*> -> {
                            binding.progressBar.visibility = View.GONE
                            val data = uiState.data.map { it as AgendaItem }

                            savedItem = data.firstOrNull() ?: AgendaItem(
                                Calendar.getInstance().apply {
                                    set(Calendar.HOUR_OF_DAY, 0)
                                    set(Calendar.MINUTE, 0)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }.time,
                                userInfoProvider.currentUser.pkUsuario,
                                ""
                            )

                            if (!isRestoredFromState) {
                                binding.bodyText.setText(savedItem.descripcion)
                            }
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

        viewModel.fetchData(savedDate)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.bodyText.addTextChangedListener(
            afterTextChanged = {
                updateFab()
            }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            BundleKeys.ARGS_ORIGINAL_DESCRIPTION_KEY,
            binding.bodyText.text.toString()
        )
        outState.putLong(BundleKeys.ARGS_DATE_KEY, savedDate.time)
        outState.putString(BundleKeys.ARGS_ITEM_KEY, gson.toJson(savedItem, AgendaItem::class.java))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            val savedText = it.getString(BundleKeys.ARGS_ORIGINAL_DESCRIPTION_KEY)
            if (savedText != null) {
                binding.bodyText.setText(savedText)
                isRestoredFromState = true
            }

            this.savedDate = Date(it.getLong(BundleKeys.ARGS_DATE_KEY))

            val item = it.getString(BundleKeys.ARGS_ITEM_KEY)
            if (item != null) {
                savedItem = gson.fromJson(item, AgendaItem::class.java)
            }

            updateFab()
        }
    }

    private fun onSave() {
        val newDescription = binding.bodyText.text.toString().trim()
        if (newDescription != savedItem.descripcion) {
            viewModel.saveDiaryEntry(
                savedItem.copy(descripcion = newDescription).also {
                    savedItem = it
                }
            )
        }

        updateFab()
    }

    private fun updateFab() {
        binding.fabSave.apply {
            if (binding.bodyText.text.toString().trim() == savedItem.descripcion) hide()
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
