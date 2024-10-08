package com.cocot3ro.mipastillero.ui.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.core.DataStoreManager
import com.cocot3ro.mipastillero.databinding.FragmentSettingsBinding
import com.cocot3ro.mipastillero.ui.view.dialogs.DeleteDataDialogFragment
import com.cocot3ro.mipastillero.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class SettingsFragment : Fragment(), DeleteDataDialogFragment.OnDeleteDataListener {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var navController: NavController

    private lateinit var mapSettings: MutableMap<String, Any>
    private lateinit var changes: MutableSet<String>

    private var loadingJob: Job? = null

    private object BundleKeys {
        const val MAP_SETTINGS_KEY = "map_settings_key"
        const val CHANGES_KEY = "changes"
    }

    private object ChangesKeys {
        const val USE_IMAGES = "use_images"
        const val USE_HIGH_QUALITY_IMAGES = "use_high_quality_images"
        const val USE_NOTIFICATIONS = "use_notifications"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (changes.isEmpty()) {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                } else {
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.discard))
                        .setMessage(getString(R.string.ensure_discard_changes))
                        .setPositiveButton(getString(R.string.discard)) { _, _ ->
                            changes.clear()
                            isEnabled = false
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.toolbar.setupWithNavController(navController)

        binding.ibManageUsers.setOnClickListener {
            navController.popBackStack()
            navController.navigate(R.id.manageUsersFragment)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            loadingJob?.join()

            binding.swUseImages.setOnCheckedChangeListener { _, isChecked ->
                mapSettings[DataStoreManager.PreferencesKeys.USE_IMAGES] = isChecked
                binding.clHdImages.visibility = if (isChecked) View.VISIBLE else View.GONE

                toggleChange(ChangesKeys.USE_IMAGES)
                updateFab()
            }

            binding.swHdImages.setOnCheckedChangeListener { _, isChecked ->
                mapSettings[DataStoreManager.PreferencesKeys.USE_HIGH_QUALITY_IMAGES] =
                    isChecked

                toggleChange(ChangesKeys.USE_HIGH_QUALITY_IMAGES)
                updateFab()
            }

            binding.swUseNotifications.setOnCheckedChangeListener { _, isChecked ->
                mapSettings[DataStoreManager.PreferencesKeys.USE_NOTIFICATIONS] = isChecked

                toggleChange(ChangesKeys.USE_NOTIFICATIONS)
                updateFab()
            }

            binding.btnDeleteInfo.setOnClickListener {
                DeleteDataDialogFragment().show(childFragmentManager, DeleteDataDialogFragment.TAG)
            }

            binding.fabSave.setOnClickListener {
                viewModel.saveSettings(mapSettings)
                changes.clear()
                updateFab()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            BundleKeys.MAP_SETTINGS_KEY,
            viewModel.gson.toJson(mapSettings, MutableMap::class.java)
        )
        outState.putStringArrayList(BundleKeys.CHANGES_KEY, ArrayList(changes))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        loadingJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            savedInstanceState?.let {
                mapSettings = (viewModel.gson.fromJson(
                    it.getString(BundleKeys.MAP_SETTINGS_KEY),
                    MutableMap::class.java
                )?.map { (k, v) -> k as String to v!! }?.toMap()?.toMutableMap()
                    ?: viewModel.loadSettings())

                changes = it.getStringArrayList(BundleKeys.CHANGES_KEY)?.toMutableSet()
                    ?: mutableSetOf()
            } ?: run {
                mapSettings = viewModel.loadSettings()
                changes = mutableSetOf()
            }

            withContext(Dispatchers.Main) {
                loadSettings()
                updateFab()
            }
        }
    }

    override fun onDeleteData() {
        viewModel.deleteData()
        navController.popBackStack(R.id.usersFragment, false)
    }

    private fun loadSettings() {
        val useImages = (mapSettings[DataStoreManager.PreferencesKeys.USE_IMAGES]) as Boolean

        val useHdImages =
            mapSettings[DataStoreManager.PreferencesKeys.USE_HIGH_QUALITY_IMAGES] as Boolean

        val useNotifications =
            mapSettings[DataStoreManager.PreferencesKeys.USE_NOTIFICATIONS] as Boolean

        binding.swUseImages.isChecked = useImages

        binding.tvUseImagesDescription.text =
            if (useImages) getString(R.string.use_images_on)
            else getString(R.string.use_images_off)

        binding.swHdImages.isChecked = useHdImages
        binding.clHdImages.visibility = if (useImages) View.VISIBLE else View.GONE

        binding.tvHdImagesDescription.text =
            if (useHdImages) getString(R.string.use_hd_images_on)
            else getString(R.string.use_hd_images_off)

        binding.swUseNotifications.isChecked = useNotifications

        binding.tvUseNotificationsDescription.text =
            if (useNotifications) getString(R.string.use_notifications_on)
            else getString(R.string.use_notifications_off)
    }

    private fun toggleChange(value: String) {
        if (changes.contains(value)) changes.remove(value)
        else changes.add(value)
    }

    private fun updateFab() {
        if (changes.isEmpty()) binding.fabSave.hide()
        else binding.fabSave.show()
    }
}
