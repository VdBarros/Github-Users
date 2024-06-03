package com.vinib.githubusers.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vinib.githubusers.domain.models.User
import com.vinib.githubusers.domain.models.UserRepository

class Converters {

    @TypeConverter
    fun userListToJson(value: List<User>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToUserList(value: String): List<User> {
        return Gson().fromJson(value, object : TypeToken<List<User>>() {}.type)
    }

    @TypeConverter
    fun userRepositoryListToJson(value: List<UserRepository>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToUserRepositoryList(value: String): List<UserRepository> {
        return Gson().fromJson(value, object : TypeToken<List<UserRepository>>() {}.type)
    }
}