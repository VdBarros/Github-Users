package com.vinib.githubusers.di

import android.content.Context
import com.vinib.githubusers.GithubUsersApplication
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ApplicationBindingModule::class
    ]
)

interface AppComponent {

    fun inject(githubUsersApplication: GithubUsersApplication): GithubUsersApplication

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}