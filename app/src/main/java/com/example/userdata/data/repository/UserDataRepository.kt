package com.example.userdata.data.repository

import com.example.userdata.data.local.UserDataDao
import com.example.userdata.data.local.UserDataEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val userDataDao: UserDataDao
) : UserDataRepository {

    override fun getAllNotifications(): Flow<List<UserDataEntity>> {
        return userDataDao.getAllUsers()
    }

    override suspend fun addUserData(user: UserDataEntity) {
        userDataDao.insertUser(user)
    }

}

interface UserDataRepository {

    fun getAllNotifications(): Flow<List<UserDataEntity>>
    suspend fun addUserData(user: UserDataEntity)
}

/* Repository pattern helps makes following a certain schema of queries / methods compulsory
*  helps out in testing and swapping whenever needed, wholesale changes wont be needed
* */