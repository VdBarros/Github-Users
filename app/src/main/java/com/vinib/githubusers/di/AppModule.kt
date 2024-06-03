package com.vinib.githubusers.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vinib.githubusers.BuildConfig
import com.vinib.githubusers.data.api.ApiClient
import com.vinib.githubusers.data.api.ApiService
import com.vinib.githubusers.data.api.UsersRemoteMediator
import com.vinib.githubusers.data.local.GithubUsersRoomDatabase
import com.vinib.githubusers.data.repository.DefaultGithubRepository
import com.vinib.githubusers.domain.models.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @IntoSet
    fun provideHttpLoggingInterceptor(logLevel: HttpLoggingInterceptor.Level): Interceptor {
        return HttpLoggingInterceptor().setLevel(logLevel)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptors: Set<@JvmSuppressWildcards Interceptor>): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .connectTimeout(90, TimeUnit.SECONDS)
        for (interceptor in interceptors) {
            okHttpClientBuilder.addInterceptor(interceptor)
        }
        return okHttpClientBuilder.build()
    }


    @Provides
    @Singleton
    fun provideApiEndpoint(): String {
        return BuildConfig.API_ENDPOINT
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls().create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson,
        apiEndpoint: String
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(apiEndpoint)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiClient(apiService: ApiService): ApiClient {
        return ApiClient(apiService)
    }

    @Provides
    @Singleton
    fun provideLogLevel(): HttpLoggingInterceptor.Level {
        return if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideGithubRepository(apiClient: ApiClient): DefaultGithubRepository {
        return DefaultGithubRepository(apiClient)
    }

    @Provides
    @Singleton
    fun provideGithubUsersRoomDatabase(@ApplicationContext context: Context): GithubUsersRoomDatabase {
        return Room.databaseBuilder(
            context,
            GithubUsersRoomDatabase::class.java,
            "githubUsers.db"
        ).build()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideUserPager(
        githubUsersRoomDatabase: GithubUsersRoomDatabase,
        apiService: ApiService
    ): Pager<Int, User> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = UsersRemoteMediator(
                githubUsersRoomDatabase, apiService
            ),
            pagingSourceFactory = {
                githubUsersRoomDatabase.usersDao().pagingSource()
            }
        )
    }
}