package com.a23pablooc.proxectofct.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.databinding.FragmentDiaryBinding
import com.a23pablooc.proxectofct.utils.DateTimeUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

class DiaryFragment : Fragment() {
    private var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var pagerAdapter: DiaryPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        navController =
            ((activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        pagerAdapter = DiaryPagerAdapter(requireActivity())

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.GONE

        setHasOptionsMenu(true)

        val builder = AppBarConfiguration.Builder(navController.graph)
        val appBarConfiguration = builder.build()
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.toolbar.setOnMenuItemClickListener { menuItem -> menuItemSelected(menuItem) }

        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.setCurrentItem(DiaryPagerAdapter.START_POSITION, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        pagerAdapter.reload()
    }

    private fun search() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->

                val date = DateTimeUtils.createDate(
                    year, monthOfYear, dayOfMonth
                )

                binding.viewPager.setCurrentItem(pagerAdapter.search(date), true)

            },
            // Establece la fecha actual como predeterminada
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun today() {
        binding.viewPager.setCurrentItem(DiaryPagerAdapter.START_POSITION, true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar_diary, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun menuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.search -> {
                search()
                true
            }

            R.id.today -> {
                today()
                true
            }

            else -> false
        }
    }

}