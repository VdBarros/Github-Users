package com.vinib.githubusers.data.api

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.vinib.githubusers.data.local.GithubUsersRoomDatabase
import com.vinib.githubusers.domain.models.User
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class UsersRemoteMediator(
    private val githubUsersRoomDatabase: GithubUsersRoomDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, User>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, User>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    lastItem?.id ?: 1
                }
            }

            val users = apiService.getUsers(
                since = loadKey,
                pageCount = state.config.pageSize
            )

            githubUsersRoomDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    githubUsersRoomDatabase.usersDao().clearAll()
                }
                githubUsersRoomDatabase.usersDao().upsertAll(users)
            }

            MediatorResult.Success(
                endOfPaginationReached = users.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}