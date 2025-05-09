package com.example.canvas.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtDao {
    @Query("SELECT * FROM art_table")
    fun getAllArt(): Flow<List<ArtEntity>>

    @Query("SELECT * FROM art_table WHERE id = :id")
    suspend fun getArtById(id: Int): ArtEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArt(art: ArtEntity)

    @Update
    suspend fun updateArt(art: ArtEntity)

    @Query("DELETE FROM art_table WHERE id = :art")
    suspend fun deleteArt(art: Int)

}