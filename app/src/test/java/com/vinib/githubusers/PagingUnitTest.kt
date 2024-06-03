package com.vinib.githubusers

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.vinib.githubusers.data.api.ApiService
import com.vinib.githubusers.data.api.UsersRemoteMediator
import com.vinib.githubusers.data.local.GithubUsersRoomDatabase
import com.vinib.githubusers.data.local.UsersDao
import com.vinib.githubusers.domain.models.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executor

@ExperimentalCoroutinesApi
class DeleteContactUnitTest {
    private lateinit var githubApi: ApiService
    private lateinit var database: GithubUsersRoomDatabase
    private lateinit var usersDao: UsersDao
    private lateinit var usersRemoteMediator: UsersRemoteMediator

    @Before
    fun setup() {
        githubApi = mockk()
        database = mockk()
        usersDao = mockk()
        usersRemoteMediator = UsersRemoteMediator(database, githubApi)
        val mockTransactionExecutor = Executor { it.run() }
        every { database.transactionExecutor } returns mockTransactionExecutor
        every { database.usersDao() } returns usersDao
        every { database.suspendingTransactionId } returns ThreadLocal<Int>()
        every { database.beginTransaction() } returns Unit
        every { database.endTransaction() } returns Unit
        every { database.setTransactionSuccessful() } returns Unit
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun refreshLoadReturnsErrorResultWhenIOExceptionOccurs() = runTest {
        coEvery { githubApi.getUsers(any(), any()) } throws IOException()
        val result = usersRemoteMediator.load(
            LoadType.REFRESH, PagingState(
                listOf(),
                null,
                PagingConfig(10),
                10
            )
        )
        assert(result is RemoteMediator.MediatorResult.Error)
        coVerify(exactly = 1) { githubApi.getUsers(any(), any()) }
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun refreshLoadReturnsSuccessResultWhenApiSuccess() = runTest {
        val users = listOf(
            User(
                id = 6671,
                avatarUrl = null,
                url = null,
                login = null,
                usersFollowing = listOf(),
                usersFollowers = listOf(),
                repos = listOf()
            )
        )
        coEvery { githubApi.getUsers(any(), any()) } returns users
        coEvery { usersDao.clearAll() } returns Unit
        coEvery { usersDao.upsertAll(any()) } returns Unit
        val result = usersRemoteMediator.load(
            LoadType.REFRESH, PagingState(
                listOf(),
                null,
                PagingConfig(10),
                10
            )
        )
        assert(result is RemoteMediator.MediatorResult.Success)
        coVerify(exactly = 1) { githubApi.getUsers(any(), any()) }
    }
}
