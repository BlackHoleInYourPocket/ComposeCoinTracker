package com.scz.cointracker.di

import android.content.Context
import androidx.room.Room
import com.scz.cointracker.room.CoinDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context, CoinDatabase::class.java, "CoinDatabase"
        ).build()

    @Singleton
    @Provides
    fun provideDatabase(database: CoinDatabase) = database.coinDao()
}