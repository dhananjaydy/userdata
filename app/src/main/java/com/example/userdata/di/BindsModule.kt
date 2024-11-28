package com.example.userdata.di

import com.example.userdata.data.repository.UserDataRepository
import com.example.userdata.data.repository.UserDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {

    @Binds
    @Singleton
    abstract fun bindUserDataRepo(userDataRepositoryImpl: UserDataRepositoryImpl): UserDataRepository

}