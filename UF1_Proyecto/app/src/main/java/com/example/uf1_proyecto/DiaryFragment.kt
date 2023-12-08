package com.example.uf1_proyecto

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.uf1_proyecto.databinding.FragmentDiaryBinding
import java.util.Calendar

class DiaryFragment : Fragment() {
    private var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!

    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        _pillboxViewModel = PillboxViewModel.getInstance(requireContext())
        val view = binding.root

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        setHasOptionsMenu(true)

        val navHostFragment =
            (activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.calendarFragment,
                R.id.activeMedFragment,
                R.id.favoriteFragment,
                R.id.diaryFragment
            ),
            binding.drawerLayout
        )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.fabPrev.setOnClickListener {
            pillboxViewModel.diaryPrevDay()
            changeToRenderer()
        }

        binding.fabNext.setOnClickListener {
            pillboxViewModel.diaryNextDay()
            changeToRenderer()
        }

        initRenderer()

        return view
    }

    private fun initRenderer() {
        val inflater = LayoutInflater.from(requireContext())

        val renderer =
            inflater.inflate(
                R.layout.diary_renderer,
                binding.diaryLayout,
                false
            ) as LinearLayout

        @Suppress("SetTextI18n")
        renderer.findViewById<TextView>(R.id.diary_date).text =
            "${pillboxViewModel.getTodayAsDayOfWeek(requireContext())} - ${
                pillboxViewModel.millisToDate(
                    pillboxViewModel.getDiaryCurrDate()
                )
            }"

        renderer.findViewById<TextView>(R.id.diary_text).text = pillboxViewModel.getDiaryText()

        renderer.findViewById<ImageButton>(R.id.diary_edit_button).setOnClickListener {
            changeToEditor()
        }

        binding.diaryLayout.addView(renderer)
    }

    private fun initEditor() {
        val inflater = LayoutInflater.from(requireContext())

        val editor =
            inflater.inflate(
                R.layout.diary_editor,
                binding.diaryLayout,
                false
            ) as LinearLayout

        @Suppress("SetTextI18n")
        editor.findViewById<TextView>(R.id.diary_date).text =
            "${pillboxViewModel.getTodayAsDayOfWeek(requireContext())} - ${
                pillboxViewModel.millisToDate(
                    pillboxViewModel.getDiaryCurrDate()
                )
            }"

        editor.findViewById<EditText>(R.id.diary_text).setText(pillboxViewModel.getDiaryText())

        editor.findViewById<ImageButton>(R.id.diary_delete_button).setOnClickListener {
            changeToRenderer()
        }

        editor.findViewById<ImageButton>(R.id.diary_save_button).setOnClickListener {
            val text = editor.findViewById<EditText>(R.id.diary_text).text.toString()
            if (text != pillboxViewModel.getDiaryText()) {
                if (pillboxViewModel.insertIntoAgenda(text)) {
                    changeToRenderer()
                    Toast.makeText(
                        requireContext(), getString(R.string.saveDiaryOk), Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(), getString(R.string.saveDiaryFail), Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        binding.diaryLayout.addView(editor)
    }

    private fun changeToEditor() {
        binding.diaryLayout.removeAllViews()
        initEditor()
    }

    private fun changeToRenderer() {
        binding.diaryLayout.removeAllViews()
        initRenderer()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_diary, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, year, monthOfYear, dayOfMonth ->
                        pillboxViewModel.setDiaryCurrDate(
                            pillboxViewModel.createDate(
                                year, monthOfYear, dayOfMonth
                            )
                        )
                        changeToRenderer()
                    },
                    // Establece la fecha actual como predeterminada
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                )

                datePickerDialog.show()
                true
            }

            R.id.today -> {
                pillboxViewModel.setDiaryCurrDate(pillboxViewModel.getTodayAsMillis())
                changeToRenderer()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}