package com.a23pablooc.proxectofct.ui.view.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.databinding.FragmentDiaryBinding
import com.a23pablooc.proxectofct.ui.view.adapters.DiaryViewPagerAdapter
import com.a23pablooc.proxectofct.ui.viewmodel.DiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class DiaryFragment : Fragment() {
    private lateinit var binding: FragmentDiaryBinding
    private val viewModel: DiaryViewModel by viewModels()

    private lateinit var diaryViewPagerAdapter: DiaryViewPagerAdapter

    private var datePickerDialog: DatePickerDialog? = null

    private companion object BundleKeys {
        private const val SHOWING_DATE_PICKER_DIALOG = "showingDatePickerDialog"
        private const val DATE_PICKER_DIALOG_DATE = "datePickerDialogDate"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiaryBinding.inflate(layoutInflater)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(findNavController())

        diaryViewPagerAdapter = DiaryViewPagerAdapter(this)

        binding.viewPager.apply {
            adapter = diaryViewPagerAdapter
            offscreenPageLimit = 1
            setCurrentItem(DiaryViewPagerAdapter.START_POSITION, false)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                return menuInflater.inflate(R.menu.menu_toolbar_diary, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.search -> {
                        search()
                        true
                    }

                    R.id.today -> {
                        binding.viewPager.setCurrentItem(
                            DiaryViewPagerAdapter.START_POSITION,
                            true
                        )
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            if (it.getBoolean(SHOWING_DATE_PICKER_DIALOG)) {
                val date = Date(it.getLong(DATE_PICKER_DIALOG_DATE))
                search(date)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SHOWING_DATE_PICKER_DIALOG, datePickerDialog?.isShowing ?: false)
        val date = datePickerDialog?.let {
            Calendar.getInstance().apply {
                set(Calendar.YEAR, datePickerDialog?.datePicker?.year ?: 0)
                set(Calendar.MONTH, datePickerDialog?.datePicker?.month ?: 0)
                set(Calendar.DAY_OF_MONTH, datePickerDialog?.datePicker?.dayOfMonth ?: 0)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time
        }
        outState.putLong(DATE_PICKER_DIALOG_DATE, date?.time ?: 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        datePickerDialog = datePickerDialog?.let {
            it.dismiss()
            null
        }
    }

    private fun search(date: Date? = null) {
        val calendar = Calendar.getInstance().apply {
            time = date ?: DateTimeUtils.now
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val offset = viewModel.calculateOffset(year, monthOfYear, dayOfMonth)
                binding.viewPager.setCurrentItem(
                    DiaryViewPagerAdapter.START_POSITION + offset,
                    true
                )
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).also {
            it.show()
        }
    }
}