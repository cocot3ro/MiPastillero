package com.a23pablooc.proxectofct.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MedInfoViewModel @Inject constructor() : ViewModel() {

    fun openUrl(context: Context, url: Uri) {
        val browserIntent = Intent(Intent.ACTION_VIEW, url)
        context.startActivity(browserIntent)
    }
}