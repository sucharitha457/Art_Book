package com.example.canvas.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canvas.data.local.ArtEntity
import com.example.canvas.domain.usecase.ArtUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewmodel @Inject constructor(
    val usecase: ArtUsecase
):ViewModel() {
    fun saveArt(art : ArtEntity){
        viewModelScope.launch {
            usecase.insertArt(art)
        }
    }
    fun updateNote(art : ArtEntity){
        viewModelScope.launch {
            usecase.updateArt(art)
        }
    }
    suspend fun getNote(artId: Int): ArtEntity? {
        return usecase.getArtById(artId)
    }
    fun deleteNote(art : Int){
        viewModelScope.launch {
            usecase.deleteArt(art)
        }
    }
}