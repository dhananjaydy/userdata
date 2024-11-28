package com.example.userdata.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDataDao {

    @Insert
    suspend fun insertUser(user: UserDataEntity)

    @Query("SELECT * FROM user_data_table")
    fun getAllUsers(): Flow<List<UserDataEntity>>

}