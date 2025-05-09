package com.example.canvas.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.canvas.domain.usecase.ArtUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    val usecase: ArtUsecase
): ViewModel() {
    fun getArts() = usecase.getArts()
}