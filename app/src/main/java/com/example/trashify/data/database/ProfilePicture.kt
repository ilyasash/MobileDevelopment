package com.example.trashify.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_picture")
data class ProfilePicture(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val pictureUri: String
)
