package com.vinib.githubusers.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vinib.githubusers.domain.models.User

@Dao
interface UsersDao {

    @Upsert
    suspend fun upsertAll(users: List<User>)

    @Query("SELECT * FROM user")
    fun pagingSource(): PagingSource<Int, User>

    @Query("DELETE FROM user")
    suspend fun clearAll()
}