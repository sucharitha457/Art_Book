package com.example.canvas.domain.model

import com.example.canvas.presentation.screens.DrawingPath

data class ArtData (
    val id : Int = 0,
    val title : String,
    val createdTime : Long,
    val data : List<DrawingPath>
)