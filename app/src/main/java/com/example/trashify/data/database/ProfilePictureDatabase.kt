package com.example.trashify.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProfilePicture::class], version = 1, exportSchema = false)
abstract class ProfilePictureDatabase : RoomDatabase() {
    abstract fun profilePictureDao(): ProfilePictureDao

    companion object {
        @Volatile
        private var INSTANCE: ProfilePictureDatabase? = null

        fun getDatabase(context: Context): ProfilePictureDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProfilePictureDatabase::class.java,
                    "profile_picture_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
