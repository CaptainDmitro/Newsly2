package org.captaindmitro.newsly2.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.captaindmitro.data.repository.RepositoryImpl
import org.captaindmitro.domain.repositories.Repository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Repo {

    @Singleton
    @Binds
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository

}