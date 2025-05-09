package com.example.canvas.domain.usecase

import com.example.canvas.data.local.ArtEntity
import com.example.canvas.domain.repository.ArtRepository
import javax.inject.Inject

class ArtUsecase @Inject constructor(
    val artRepository: ArtRepository
){
    fun getArts() = artRepository.getArts()
    suspend fun getArtById(id: Int) = artRepository.getArtById(id)
    suspend fun insertArt(art: ArtEntity) = artRepository.insertArt(art)
    suspend fun deleteArt(art: Int) = artRepository.deleteArt(art)
    suspend fun updateArt(art: ArtEntity) = artRepository.updateArt(art)
}