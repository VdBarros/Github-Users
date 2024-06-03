package com.vinib.githubusers.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vinib.githubusers.domain.models.User

@Database(entities = [User::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GithubUsersRoomDatabase : RoomDatabase() {

    abstract fun usersDao(): UsersDao
}