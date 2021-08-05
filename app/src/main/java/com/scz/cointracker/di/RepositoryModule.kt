package com.scz.cointracker.di

import com.scz.cointracker.network.CoinService
import com.scz.cointracker.network.model.CoinDtoMapper
import com.scz.cointracker.repository.CoinRepository
import com.scz.cointracker.repository.CoinRepository_Impl
import com.scz.cointracker.room.CoinDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCoinRepository(
        dao: CoinDao,
        coinService: CoinService,
        coinDtoMapper: CoinDtoMapper
    ): CoinRepository {
        return CoinRepository_Impl(dao, coinService, coinDtoMapper)
    }
}