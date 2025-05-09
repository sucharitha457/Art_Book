package com.example.canvas.di

import com.example.canvas.data.repository.ArtRepositoryImpl
import com.example.canvas.domain.repository.ArtRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindArtRepository(
        impl: ArtRepositoryImpl
    ): ArtRepository
}
