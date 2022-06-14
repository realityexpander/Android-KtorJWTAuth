package com.realityexpander.androidktorjwtauth.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.realityexpander.androidktorjwtauth.auth.AuthApi
import com.realityexpander.androidktorjwtauth.auth.AuthRepository
import com.realityexpander.androidktorjwtauth.auth.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return Retrofit.Builder()
//            .baseUrl("https://localhost:8080/") // use local ip, 192.168.0.186
            .baseUrl("https://a44d-187-225-129-71.ngrok.io/") // use ngrok
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideSharedPrefs(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authApi: AuthApi, sharedPrefs: SharedPreferences): AuthRepository {
        return AuthRepositoryImpl(authApi, sharedPrefs)
    }
}