package com.example.userdata.data.local

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data_table")
data class UserDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val age: Int,
    val dob: String,
    val address: String
)

@Stable
data class UserDataItem(
    val id: Long,
    val name: String,
    val age: Int,
    val dob: String,
    val address: String
)

/* marked with stable so that the system can know that we do not need observers on this for state changes and hence reduce load*/


fun UserDataEntity.toUserDataItem() =
    UserDataItem(
        id = id,
        name = name,
        age = age,
        dob = dob,
        address = address
    )