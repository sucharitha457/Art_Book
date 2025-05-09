package com.example.canvas.data.repository

import com.example.canvas.data.local.ArtDao
import com.example.canvas.data.local.ArtEntity
import com.example.canvas.domain.repository.ArtRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ArtRepositoryImpl @Inject constructor(
    private val artDao: ArtDao
): ArtRepository {
    override fun getArts(): Flow<List<ArtEntity>> {
        return artDao.getAllArt()
    }

    override suspend fun getArtById(id: Int): ArtEntity? {
        return artDao.getArtById(id)
    }

    override suspend fun insertArt(art: ArtEntity) {
        return artDao.insertArt(art)
    }

    override suspend fun deleteArt(art: Int) {
        return artDao.deleteArt(art)
    }

    override suspend fun updateArt(art: ArtEntity) {
        return artDao.updateArt(art)
    }
}