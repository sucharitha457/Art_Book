package com.example.canvas.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "art_table")
data class ArtEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title : String,
    val createdTime : Long,
    val data: List<SerializableDrawingPath>
)