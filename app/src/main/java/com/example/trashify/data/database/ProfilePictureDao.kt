package com.example.trashify.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProfilePictureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profilePicture: ProfilePicture)

    @Query("SELECT * FROM profile_picture ORDER BY id DESC LIMIT 1")
    fun getLatest(): LiveData<ProfilePicture?>
}
