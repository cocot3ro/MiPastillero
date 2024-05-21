package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(/*private val checkFinishedUseCase: CheckFinishedUseCase*/) : ViewModel() {
    // TODO: Implement the ViewModel and inject the use cases

    fun checkFinished() {
        viewModelScope.launch {
//            checkFinishedUseCase()
        }
    }
}