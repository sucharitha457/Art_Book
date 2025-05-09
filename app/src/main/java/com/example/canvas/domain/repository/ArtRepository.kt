package com.example.canvas.domain.repository

import com.example.canvas.data.local.ArtEntity
import kotlinx.coroutines.flow.Flow

interface ArtRepository {
    fun getArts(): Flow<List<ArtEntity>>
    suspend fun getArtById(id: Int): ArtEntity?
    suspend fun insertArt(art: ArtEntity)
    suspend fun deleteArt(art: Int)
    suspend fun updateArt(art: ArtEntity)
}