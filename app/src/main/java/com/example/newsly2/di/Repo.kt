package com.example.newsly2.di

import org.captaindmitro.domain.model.Repository
import org.captaindmitro.data.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class Repo {

    @Binds
    abstract fun bindRepository(repImpl: RepositoryImpl): Repository

}