package com.example.userdata.di

import android.content.Context
import androidx.room.Room
import com.example.userdata.data.local.UserDataDao
import com.example.userdata.data.local.UserDataDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule  {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): UserDataDatabase {

        val instance = Room.databaseBuilder(
            context,
            UserDataDatabase::class.java,
            "user_data_table"
        ).build()

        return instance
    }

    @Provides
    @Singleton
    fun provideUserDao(database: UserDataDatabase): UserDataDao {
        return database.userDataDao()
    }
}

