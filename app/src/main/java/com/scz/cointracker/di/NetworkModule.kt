package com.scz.cointracker.di

import com.google.gson.GsonBuilder
import com.scz.cointracker.network.CoinService
import com.scz.cointracker.network.model.CoinDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideCoinDtoMapper(): CoinDtoMapper {
        return CoinDtoMapper()
    }

    @Singleton
    @Provides
    fun provideCoinService(): CoinService {
        return Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/api/v3/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(CoinService::class.java)
    }
}