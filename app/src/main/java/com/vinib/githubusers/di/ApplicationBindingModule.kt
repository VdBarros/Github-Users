package com.vinib.githubusers.di

import com.vinib.githubusers.data.repository.DefaultGithubRepository
import com.vinib.githubusers.domain.repository.GithubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationBindingModule {

    @Binds
    abstract fun bindGithubRepository(
        impl: DefaultGithubRepository
    ): GithubRepository
}